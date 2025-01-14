package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.controller.task.dto;

import java.time.Instant;

public record ResponseTaskDTO(
        Long id,
        String title,
        String description,
        Status status,
        Priority priority,
        Instant createdAt
) {
}
