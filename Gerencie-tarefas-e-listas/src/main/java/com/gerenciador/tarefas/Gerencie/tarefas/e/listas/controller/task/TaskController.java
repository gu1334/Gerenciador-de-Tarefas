package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto.CreateTaskDTO;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto.ResponseTaskDTO;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto.Status;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Role;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Task;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.repository.TaskRepository;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ResponseTaskDTO> createTask(@RequestBody CreateTaskDTO dto, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));

        var task = new Task();
        task.setUser(user.get());
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setPriority(dto.priority());

        taskRepository.save(task);

        var response = new ResponseTaskDTO(
                task.getTaskId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getCreateDateTimeStamp()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ResponseTaskDTO>> getTasks(JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        var isAdmin = user.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        List<Task> tasks;

        if (isAdmin) {
            tasks = taskRepository.findAll();
        } else {
            tasks = taskRepository.findByUser(user);
        }

        List<ResponseTaskDTO> response = tasks.stream()
                .map(task -> new ResponseTaskDTO(
                        task.getTaskId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getCreateDateTimeStamp()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResponseTaskDTO>> getTasksByStatus(@RequestParam(required = false) String status, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        var isAdmin = user.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        Status currentStatus;
        try {
            currentStatus = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Task> tasks;

        if (isAdmin) {
            tasks = taskRepository.findByStatus(currentStatus);
        } else {
            tasks = taskRepository.findByStatusAndUser(currentStatus, user);
        }

        List<ResponseTaskDTO> response = tasks.stream()
                .map(task -> new ResponseTaskDTO(
                        task.getTaskId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getCreateDateTimeStamp()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Long taskId, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.get().getRoles()
                .stream().anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if (isAdmin || task.getUser().getUserId().equals(UUID.fromString(token.getName()))) {
            taskRepository.deleteById(taskId);
            return ResponseEntity.ok("Task: " + taskId + " successfully deleted");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
