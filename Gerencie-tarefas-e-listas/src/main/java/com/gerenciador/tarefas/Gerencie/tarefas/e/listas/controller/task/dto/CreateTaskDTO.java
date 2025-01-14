package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto;

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
