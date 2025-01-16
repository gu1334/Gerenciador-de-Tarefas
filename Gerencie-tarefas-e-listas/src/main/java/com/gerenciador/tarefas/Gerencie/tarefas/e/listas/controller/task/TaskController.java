package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task;

import com.fasterxml.jackson.annotation.JsonView;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.view.dto.Views;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.service.TaskService;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.view.dto.CreateTaskDTO;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.task.Task;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @JsonView(Views.PublicWithCreateAt.class)
    public ResponseEntity<Task> createTask(@RequestBody @Valid CreateTaskDTO dto, JwtAuthenticationToken token) {
        Task createdTask = taskService.createTask(dto, token);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Task>> getTasks(JwtAuthenticationToken token) {
        List<Task> tasks = taskService.getTasks(token);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/search")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam String status, JwtAuthenticationToken token) {
        List<Task> tasks = taskService.getTasksByStatus(status, token);
        return ResponseEntity.ok(tasks);
    }


    @PutMapping("/{id}")
    @JsonView(Views.PublicWithUpdateAt.class)
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody @Valid CreateTaskDTO dto, JwtAuthenticationToken token) {
        Task updatedTask = taskService.updateTask(id, dto, token);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, JwtAuthenticationToken token) {
        String message = taskService.deleteTask(id, token);
        return ResponseEntity.ok(message);
    }
}
