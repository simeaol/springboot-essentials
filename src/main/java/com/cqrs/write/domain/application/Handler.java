package com.cqrs.write.domain.application;

import com.cqrs.write.domain.application.Command;

public interface Handler<T extends Command> {
    void handle(T command);
}
