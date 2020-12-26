package com.cqrs.read.domain.application;

import com.cqrs.read.domain.core.CardDto;

import java.util.UUID;

public class FindCardByIdQuery implements Query{
    private CardDto cardDto;
    private UUID externalId;

    public FindCardByIdQuery(String id){
        this.externalId = UUID.fromString(id);
    }

    public UUID getExternalId(){
        return externalId;
    }

    public void setResult(CardDto cardDto){
        this.cardDto = cardDto;
    }

    public CardDto getResult(){
        return cardDto;
    }

}
