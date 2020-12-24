package com.cqrs.write.domain.core;

import java.util.UUID;

public interface WriteCardRepository {

    void save(Card card);

    void findCardById(UUID externalId);

}
