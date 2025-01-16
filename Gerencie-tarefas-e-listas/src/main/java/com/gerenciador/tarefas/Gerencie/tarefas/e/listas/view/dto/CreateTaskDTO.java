package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.view.dto;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.task.Priority;
import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.model.task.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskDTO(
        @NotBlank
        String title,
        String description,
        @NotNull
        Status status,
        @NotNull
        Priority priority) {
}
