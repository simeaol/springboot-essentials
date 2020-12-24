package com.cqrs.read.domain.application;

import com.cqrs.read.domain.core.CardDto;

import java.util.List;

public class ListAllCardsQuery implements Query {
    private List<CardDto> result;

    public List<CardDto> getResult(){
        return result;
    }

    public void setResult(List<CardDto> result){
        this.result = result;
    }

}
