package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception;

public class BusinessException extends ErrorResponse {
    public BusinessException(String code, String message) {
        super(code, message);
    }
}
