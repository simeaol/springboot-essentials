package com.cqrs.write.domain.application;

import com.cqrs.write.domain.core.Card;
import com.cqrs.write.domain.core.WriteCardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UpdateCardHandler implements Handler<UpdateCardCommand>{
    private WriteCardRepository repository;

    public UpdateCardHandler(WriteCardRepository repository){
        this.repository = repository;
    }

    @Override
    public void handle(UpdateCardCommand command) {
        Card card = Card.builder()
                .externalId(command.getId())
                .name(command.getCardDto().getName())
                .updatedAt(LocalDateTime.now())
                .build();
        repository.update(card);
    }
}
