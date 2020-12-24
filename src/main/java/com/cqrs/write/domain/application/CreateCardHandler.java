package com.cqrs.write.domain.application;

import com.cqrs.write.domain.core.Card;
import com.cqrs.write.domain.core.WriteCardRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CreateCardHandler implements Handler<CreateCardCommand> {

    private WriteCardRepository repository;

    public CreateCardHandler(WriteCardRepository repository){
        this.repository = repository;
    }

    @Override
    public void handle(CreateCardCommand command) {
        var card = Card.builder()
                .id(new Random(10000).nextInt())
                .externalId(command.getExternalId())
                .name(command.getName())
                .build();
        repository.save(card);
    }
}
