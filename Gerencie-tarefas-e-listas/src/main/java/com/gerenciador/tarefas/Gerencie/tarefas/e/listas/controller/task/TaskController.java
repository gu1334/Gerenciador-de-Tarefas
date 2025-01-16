package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task;

import com.fasterxml.jackson.annotation.JsonView;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception.TechnicalException;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception.user.UserNotFoundException;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto.CreateTaskDTO;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto.Status;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Role;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Task;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.repository.TaskRepository;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.repository.UserRepository;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.view.Views;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
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
    @JsonView(Views.PublicWithCreateAt.class)
    public ResponseEntity<Task> createTask(@RequestBody CreateTaskDTO dto, JwtAuthenticationToken token) {
        try {
            // Busca o usuário pelo ID do token, verificando se existe
            var user = userRepository.findById(UUID.fromString(token.getName()))
                    .orElseThrow(() -> new UserNotFoundException());

            // Criação da tarefa
            var task = new Task();
            task.setUser(user);
            task.setTitle(dto.title());
            task.setDescription(dto.description());
            task.setStatus(dto.status());
            task.setPriority(dto.priority());

            // Salva a tarefa no repositório
            taskRepository.save(task);

            // Retorna a resposta com a tarefa criada
            return ResponseEntity.ok(task);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (Exception e) {
            throw new TechnicalException("003", "Failed to create task: " + e.getMessage());
        }
    }


    @GetMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Task>> getTasks(JwtAuthenticationToken token) {

        try {

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
            return ResponseEntity.ok(tasks);

        }catch (Exception e) {
            throw new TechnicalException("003", "Failed to find task: " + e.getMessage());
        }
    }

    //List by status
    @GetMapping("/search")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam(required = false) String status, JwtAuthenticationToken token) {
     try {
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
         return ResponseEntity.ok(tasks);
     }catch (Exception e) {
         throw new TechnicalException("003", "Failed to find task: " + e.getMessage());
     }
    }

    @PutMapping("/{id}")
    @JsonView(Views.PublicWithUpdateAt.class)
    public ResponseEntity<Task> putTask(@PathVariable ("id") Long taskId, @RequestBody @Valid CreateTaskDTO dto, JwtAuthenticationToken token){

        try {
            var user = userRepository.findById(UUID.fromString(token.getName()));
            var task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            var isAdmin = user.get().getRoles()
                    .stream().anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));
            if (isAdmin || task.getUser().getUserId().equals(UUID.fromString(token.getName()))) {
                task.setTitle(dto.title());
                task.setDescription(dto.description());
                task.setStatus(dto.status());
                task.setPriority(dto.priority());
                task.setUpdateAtDateTimeStamp(Instant.now());
                taskRepository.save(task);
                return ResponseEntity.ok(task);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }catch (Exception e) {
            throw new TechnicalException("006", "Failed to change task: " + e.getMessage());
        }



    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable("id") Long taskId, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.get().getRoles()
                .stream().anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        try{
            if (isAdmin || task.getUser().getUserId().equals(UUID.fromString(token.getName()))) {
                taskRepository.deleteById(taskId);
            }
        } catch (Exception e) {
            throw new TechnicalException("006", "Failed to delete task: " + e.getMessage());
        }
        return ResponseEntity.ok("Task: " + taskId + " successfully deleted");
    }
}
