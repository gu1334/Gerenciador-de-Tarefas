package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.exceptions;

public class TechnicalException extends ErrorResponse {
    public TechnicalException(String code, String message) {
        super(code, message);
    }
}
