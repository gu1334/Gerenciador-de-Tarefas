package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception.user;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception.TechnicalException;

public class UsuarioJaCadastradoException extends TechnicalException {
    public UsuarioJaCadastradoException(String username) {
        super("001", String.format("Usuário %s já cadastrado", username));
    }
}
