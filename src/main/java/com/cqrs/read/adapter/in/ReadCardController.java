package com.cqrs.read.adapter.in;

import com.cqrs.common.ServiceBus;
import com.cqrs.read.domain.application.ListAllCardsQuery;
import com.cqrs.read.domain.core.CardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/cards")
public class ReadCardController {
    private ServiceBus serviceBus;

    public ReadCardController(ServiceBus serviceBus){
        this.serviceBus = serviceBus;
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> listAll(){
        var query = new ListAllCardsQuery();
        serviceBus.execute(query);
        return ResponseEntity.ok(query.getResult());
    }
}
