package com.cqrs.write.adapter.in;

public class WriteCardExceptionHandler extends RuntimeException{
    public WriteCardExceptionHandler(Error error){
        super(error);
    }
}
