package com.cqrs.write.domain.core;

public interface WriteCardRepository {

    void save(Card card);

    void update(Card card);

}
