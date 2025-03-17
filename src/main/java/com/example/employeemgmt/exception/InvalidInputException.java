package com.example.employeemgmt.exception;

/**
 * Custom exception to handle validation for values being entered for fields.
 */
public class InvalidInputException extends RuntimeException{
    public InvalidInputException(String message){
        super(message);
    }
}
