package com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception;

import com.gerenciador.tarefas.Gerencie.tarefas.e.listas.config.exception.user.UsuarioJaCadastradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsuarioJaCadastradoException.class)
    public final ResponseEntity<Object> handleUsuarioJaCadastradoException(UsuarioJaCadastradoException ex) {
        var errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleGenericException(Exception ex) {
        var errorResponse = new ErrorResponse("500", "Erro interno no servidor");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
