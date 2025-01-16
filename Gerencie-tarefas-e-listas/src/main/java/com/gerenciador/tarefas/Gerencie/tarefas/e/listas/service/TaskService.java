package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.service;


import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.task.Status;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.exceptions.TechnicalException;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.exceptions.UserNotFoundException;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.view.dto.CreateTaskDTO;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.task.Task;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.repository.task.TaskRepository;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Role;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.user.Users;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private Users getUserFromToken(JwtAuthenticationToken token) {
        return userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(() -> new UserNotFoundException());
    }

    private boolean isAdmin(Users user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));
    }

    public Task createTask(CreateTaskDTO dto, JwtAuthenticationToken token) {
        Users user = getUserFromToken(token);
        Task task = new Task();
        task.setUser(user);
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setPriority(dto.priority());

        try {
            return taskRepository.save(task);
        } catch (Exception e) {
            throw new TechnicalException("003", "Erro ao criar a tarefa: " + e.getMessage());
        }
    }

    public List<Task> getTasks(JwtAuthenticationToken token) {
        Users user = getUserFromToken(token);
        try {
            return isAdmin(user) ? taskRepository.findAll() : taskRepository.findByUser(user);
        } catch (Exception e) {
            throw new TechnicalException("004", "Erro ao buscar as tarefas: " + e.getMessage());
        }
    }

    public Task updateTask(Long id, CreateTaskDTO dto, JwtAuthenticationToken token) {
        Users user = getUserFromToken(token);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        if (isAdmin(user) || task.getUser().getUserId().equals(user.getUserId())) {
            task.setTitle(dto.title());
            task.setDescription(dto.description());
            task.setStatus(dto.status());
            task.setPriority(dto.priority());
            task.setUpdateAtDateTimeStamp(Instant.now());

            try {
                return taskRepository.save(task);
            } catch (Exception e) {
                throw new TechnicalException("005", "Erro ao atualizar a tarefa: " + e.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }
    }
    public List<Task> getTasksByStatus(String status, JwtAuthenticationToken token) {
        Users user = getUserFromToken(token);
        boolean isAdmin = isAdmin(user);

        Status currentStatus;
        try {
            currentStatus = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TechnicalException("007", "Status inválido: " + status);
        }

        try {
            return isAdmin
                    ? taskRepository.findByStatus(currentStatus)
                    : taskRepository.findByStatusAndUser(currentStatus, user);
        } catch (Exception e) {
            throw new TechnicalException("008", "Erro ao buscar tarefas pelo status: " + e.getMessage());
        }
    }

    public String deleteTask(Long id, JwtAuthenticationToken token) {
        Users user = getUserFromToken(token);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        if (isAdmin(user) || task.getUser().getUserId().equals(user.getUserId())) {
            try {
                taskRepository.deleteById(id);
                return "Tarefa " + id + " deletada com sucesso.";
            } catch (Exception e) {
                throw new TechnicalException("006", "Erro ao deletar a tarefa: " + e.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
        }
    }
}
