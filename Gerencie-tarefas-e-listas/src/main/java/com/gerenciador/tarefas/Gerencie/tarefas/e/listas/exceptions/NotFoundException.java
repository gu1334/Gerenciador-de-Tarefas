package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.exceptions;

public class NotFoundException extends ErrorResponse {
    public NotFoundException(String code, String message) {
        super(code, message);
    }
}
