package com.cqrs.read.domain.application;

import com.cqrs.read.adapter.out.ReadCardRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
public class ListAllCardsResolver implements Resolver<ListAllCardsQuery> {
    private ReadCardRepositoryImpl readRepository;

    public ListAllCardsResolver(ReadCardRepositoryImpl readRepository){
        this.readRepository = readRepository;
    }

    @Override
    public void resolve(ListAllCardsQuery query) {
        query.setResult(readRepository.findAll());
    }
}
