package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception.user;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception.TechnicalException;

public class UserNotFoundException extends TechnicalException {

    public UserNotFoundException() {
        super("004", "User not found: ");
    }
}
