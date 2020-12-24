package com.cqrs.write.domain.exception;

public class CardNotFoundException extends RuntimeException{
    public CardNotFoundException(Error error){
       super(error);
    }
}
