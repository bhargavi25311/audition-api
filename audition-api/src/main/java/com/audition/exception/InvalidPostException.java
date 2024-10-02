package com.audition.exception;

public class InvalidPostException extends RuntimeException {
    private static final long serialVersionUID = 1L;

 public InvalidPostException(final String message) {
 super(message);
 }
}
