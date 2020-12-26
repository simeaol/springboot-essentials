package com.cqrs.read.domain.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(Error error){
        super(error);
    }
}
