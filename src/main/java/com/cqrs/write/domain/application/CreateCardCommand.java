package com.cqrs.write.domain.application;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;
import java.util.UUID;

@Getter
@Builder
public class CreateCardCommand implements Command {
    @NotNull
    @Pattern(regexp = "UUID_FORMAT", message = "INVALID_UUID")
    private final UUID externalId;

    private String id;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;

    public CreateCardCommand(String desiredId, String externalId, String name){
        this.id = desiredId;
        this.externalId = UUID.fromString(externalId);
        this.name = name;
    }
}
