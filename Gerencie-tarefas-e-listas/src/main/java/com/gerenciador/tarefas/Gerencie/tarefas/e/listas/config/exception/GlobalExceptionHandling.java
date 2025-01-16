package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception.user.UsuarioJaCadastradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsuarioJaCadastradoException.class)
    public final ResponseEntity<Object> handleUsuarioJaCadastradoException(UsuarioJaCadastradoException ex) {
        var errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // Tratamento para erro 401 - Falha de autenticação
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public final ResponseEntity<Object> handleAuthenticationException(AuthenticationCredentialsNotFoundException ex) {
        var errorResponse = new ErrorResponse("401", "Acesso negado");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // Tratamento para erro 403 - Acesso proibido
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        var errorResponse = new ErrorResponse("403", "Você não tem permissão para acessar este recurso");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleGenericException(Exception ex) {
        var errorResponse = new ErrorResponse("500", "Erro interno no servidor");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

