package com.cqrs.write.adapter.in;

import com.cqrs.common.ServiceBus;
import com.cqrs.write.domain.application.CreateCardCommand;
import com.cqrs.write.domain.application.UpdateCardCommand;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}")
    public ResponseEntity<String> create(@Valid @NotNull @PathVariable("id") String desiredId,
                                         @RequestBody CardDto cardDto){
        var command = new CreateCardCommand(desiredId, cardDto.getExternalId(), cardDto.getName());
        serviceBus.execute(command);

        return ResponseEntity.status(CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@NotNull @PathVariable("id") String externalId, @NotNull @RequestBody CardDto cardDto){
        Assert.notNull(externalId, "ID cannot be null");

        var command = new UpdateCardCommand(externalId, cardDto);
        serviceBus.execute(command);
        return ResponseEntity.ok().build();
    }



    @Getter
    public class CardDto{
        @JsonProperty("id") String externalId;
        String name;
    }
}
