package com.softplan.codechallenge.domain.exception;

public class PessoaNaoEncontradaException extends RuntimeException {

    public PessoaNaoEncontradaException(String message) {
        super(message);
    }
}
