package com.cqrs.read.domain.application;

import com.cqrs.read.domain.core.CardDto;
import com.cqrs.read.domain.core.ReadCardRepository;
import com.cqrs.read.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindCardByIdResolver implements Resolver<FindCardByIdQuery> {
    private ReadCardRepository repository;

    public FindCardByIdResolver(ReadCardRepository repository){
        this.repository = repository;
    }

    @Override
    public void resolve(FindCardByIdQuery query) {
        repository.findById(query.getExternalId().toString())
                .ifPresentOrElse(query::setResult ,() -> new ResourceNotFoundException(new Error("Card not found!")));
    }
}
