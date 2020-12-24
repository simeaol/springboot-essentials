package com.cqrs.common;

import com.cqrs.write.domain.application.Command;
import com.cqrs.read.domain.application.Query;
import com.cqrs.write.domain.application.Handler;
import com.cqrs.read.domain.application.Resolver;
import com.cqrs.write.observable.CommandEvent;
import com.cqrs.read.observable.QueryEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ServiceBus {

    private final ApplicationContext context;
    private final ApplicationEventPublisher publisher;

    public ServiceBus(ApplicationContext context, ApplicationEventPublisher publisher){
        this.context = context;
        this.publisher = publisher;
    }

    public void execute(Command command){
        CommandEvent event = new CommandEvent(command);
        execute(event);
    }

    public void execute(Query query){
        QueryEvent event = new QueryEvent(query);
        execute(event);
    }

    public void execute(InternalEvent event){
        try{
            run(event);
        }catch (Exception e){
            throw e; //Terror-Driven-Development
        }
        finally {
            publisher.publishEvent(event);
        }
    }

    private void run(InternalEvent event) {
        String beanName = event.getSource().getClass().getSimpleName().toLowerCase();
        switch (event.getType()){
            case COMMAND:{
                Handler<Command> handler = (Handler) context.getBean(beanName);
                handler.handle((Command)event.getSource());
                break;
            }
            case QUERY:{
                Resolver<Query> resolver = (Resolver) context.getBean(beanName);
                resolver.resolve((Query)event.getSource());
                break;
            }
        }
    }


}
