package com.example.employeemgmt.exception;

/**
 * Custom exception to handle situations a request is sent for a id that does not exist.
 */
public class IdNotFoundException extends RuntimeException{
    public IdNotFoundException(String message){
        super(message);
    }
}
