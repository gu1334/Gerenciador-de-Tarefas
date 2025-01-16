package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.exceptions;

public class UserNotFoundException extends TechnicalException {

    public UserNotFoundException() {
        super("004", "User not found: ");
    }
}
