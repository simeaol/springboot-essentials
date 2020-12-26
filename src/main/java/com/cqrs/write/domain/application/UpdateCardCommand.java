package com.cqrs.write.domain.application;

import com.cqrs.write.adapter.in.WriteCardController;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateCardCommand implements Command{
    private UUID id;
    private WriteCardController.CardDto cardDto;

    public UpdateCardCommand(String id, WriteCardController.CardDto cardDto){
        this.id = UUID.fromString(id);
        this.cardDto =  cardDto;
    }
}
