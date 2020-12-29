package com.cqrs.write.observable;

import com.cqrs.write.domain.application.Command;
import com.cqrs.common.AbstractInternalEvent;

public class CommandEvent extends AbstractInternalEvent<Command> {
    public CommandEvent(Command command){
        super(command);
    }

    @Override
    public Type getType() {
        return Type.COMMAND;
    }
}
