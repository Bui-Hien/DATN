package com.buihien.datn.exception;

public class ConflictDataException extends RuntimeException {

    public ConflictDataException(String message) {
        super(message);
    }

    public ConflictDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
