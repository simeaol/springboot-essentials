package com.cqrs.common;

import com.cqrs.write.domain.exception.CardNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogListener {

    @Async //Make onEvent to be executed in separated Thread without blocking the trigger action
    @EventListener(AbstractInternalEvent.class) //if class don't specified, all events will be logged
    void onEvent(AbstractInternalEvent event){
        if(event.getSource() instanceof CardNotFoundException){
            log.warn(((CardNotFoundException) event.getSource()).getMessage());
        }
        else if(event.getSource() instanceof RuntimeException){
            log.error(((RuntimeException) event.getSource()).getMessage());
        }else{
            log.info(event.getSource().toString());
        }
    }
}
