package com.cqrs.read.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CardDto {
    private @JsonProperty("id") UUID id;
    private @JsonProperty("name") String name;
}
