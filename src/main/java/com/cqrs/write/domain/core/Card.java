package com.cqrs.write.domain.core;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class Card {
    private int id;
    private UUID externalId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
