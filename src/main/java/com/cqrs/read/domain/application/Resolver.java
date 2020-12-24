package com.cqrs.read.domain.application;

public interface Resolver<T extends Query> {

    void resolve(T query);
}
