package com.petshop.petshop_api.infra;

import com.petshop.petshop_api.exception.CredencialInvalidaException;
import com.petshop.petshop_api.exception.RecursoEmUsoException;
import com.petshop.petshop_api.exception.RecursoJaExistenteException;
import com.petshop.petshop_api.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<RestError> recursoNaoEncontrado(RecursoNaoEncontradoException e) {

        var restError = new RestError(HttpStatus.NOT_FOUND, e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restError);
    }

    @ExceptionHandler(RecursoJaExistenteException.class)
    public ResponseEntity<RestError> recursoJaExistente(RecursoJaExistenteException e) {

        var restError = new RestError(HttpStatus.CONFLICT, e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(restError);
    }

    @ExceptionHandler(RecursoEmUsoException.class)
    public ResponseEntity<RestError> recursoEmUso (RecursoEmUsoException e) {

        var restError = new RestError(HttpStatus.CONFLICT, e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(restError);
    }

    @ExceptionHandler(CredencialInvalidaException.class)
    public ResponseEntity<RestError> credencialInvalida(CredencialInvalidaException e) {

        var restError = new RestError(HttpStatus.UNAUTHORIZED, e.getMessage());

        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(restError);
    }

}
