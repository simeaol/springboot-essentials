package com.cqrs.read.domain.core;

import java.util.List;

public interface ReadCardRepository {

    List<CardDto> findAll();
}
