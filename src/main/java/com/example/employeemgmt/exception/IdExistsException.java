package com.example.employeemgmt.exception;

/**
 * Custom exception class to handle instances of duplicate ids being registered.
 */
public class IdExistsException extends RuntimeException {
    public IdExistsException(String message) {
        super(message);
    }
}