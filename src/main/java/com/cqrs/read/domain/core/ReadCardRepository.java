package com.cqrs.read.domain.core;

import java.util.List;
import java.util.Optional;

public interface ReadCardRepository {

    List<CardDto> findAll();

    Optional<CardDto> findById(String id);
}
