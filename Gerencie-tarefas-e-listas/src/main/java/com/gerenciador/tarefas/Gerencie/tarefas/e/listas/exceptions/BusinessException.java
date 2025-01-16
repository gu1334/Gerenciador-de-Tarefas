package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.exceptions;

public class BusinessException extends ErrorResponse {
    public BusinessException(String code, String message) {
        super(code, message);
    }
}
