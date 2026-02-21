package com.project.cinema.exceptions;

public class EmailNaoEncontradoException extends RuntimeException {
    public EmailNaoEncontradoException(String message) {
        super(message);
    }
}
