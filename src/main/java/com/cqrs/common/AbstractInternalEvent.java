package com.cqrs.common;

public abstract class AbstractInternalEvent<T> {
    private T t;
    private Exception exception;

    public AbstractInternalEvent(T t){
        this.t = t;
    }

    public abstract Type getType();

    public T getSource(){
        return this.t;
    }

    public Exception getException(){
        return exception;
    }

    public void setException(Exception e){
        exception = e;
    }



    public enum Type{
        COMMAND,QUERY;
    }
}
