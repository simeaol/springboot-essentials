package com.cqrs.common;

public class ServiceBusInvalidOperationException extends RuntimeException{

    public ServiceBusInvalidOperationException(Error error){
        super(error);
    }
}
