package com.cqrs.write.observable;

import com.cqrs.write.domain.application.Command;
import com.cqrs.common.InternalEvent;

public class CommandEvent extends InternalEvent<Command> {
    public CommandEvent(Command command){
        super(command);
    }

    @Override
    public Type getType() {
        return Type.COMMAND;
    }
}
