package com.cqrs.common;

public abstract class InternalEvent<T> {
    private T t;
    public InternalEvent(T t){
        this.t = t;
    }

    public abstract Type getType();

    public T getSource(){
        return this.t;
    }

    public enum Type{
        COMMAND,QUERY;
    }
}
