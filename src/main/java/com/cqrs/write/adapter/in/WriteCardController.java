package com.cqrs.write.adapter.in;

import com.cqrs.common.ServiceBus;
import com.cqrs.write.domain.application.CreateCardCommand;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("v1/cards")
public class WriteCardController {
    private ServiceBus serviceBus;

    public WriteCardController(ServiceBus serviceBus){
        this.serviceBus = serviceBus;
    }

    public ResponseEntity<String> create(@Valid @NotNull @PathVariable("id") String desiredId,
                                         @RequestBody CardDto cardDto){
        var command = new CreateCardCommand(desiredId, cardDto.getExternalId(), cardDto.getName());
        serviceBus.execute(command);

        return ResponseEntity.status(CREATED).build();
    }

    @Getter
    public class CardDto{
        @JsonProperty("id") String externalId;
        String name;
    }
}
