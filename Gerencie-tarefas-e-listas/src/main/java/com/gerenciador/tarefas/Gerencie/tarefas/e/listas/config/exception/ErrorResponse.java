package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception;

public class ErrorResponse extends RuntimeException{
    private String code;
    private String message;

    public ErrorResponse(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
