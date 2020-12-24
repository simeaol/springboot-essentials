package com.cqrs.common;

import com.cqrs.read.domain.application.Query;
import com.cqrs.read.domain.application.Resolver;
import com.cqrs.read.observable.QueryEvent;
import com.cqrs.write.domain.application.Command;
import com.cqrs.write.domain.application.Handler;
import com.cqrs.write.observable.CommandEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ServiceBus {

    private final ApplicationContext context;
    private final ApplicationEventPublisher publisher;

    public ServiceBus(ApplicationContext context, ApplicationEventPublisher publisher) {
        this.context = context;
        this.publisher = publisher;
    }

    public void execute(Command command) {
        CommandEvent event = new CommandEvent(command);
        execute(event);
    }

    public void execute(Query query) {
        QueryEvent event = new QueryEvent(query);
        execute(event);
    }

    public void execute(InternalEvent event) {
        try {
            run(event);
        } catch (Exception e) {
            throw e; //Terror-Driven-Development
        } finally {
            publisher.publishEvent(event);
        }
    }

    private void run(InternalEvent event) {
        String beanName = event.getSource().getClass().getSimpleName().substring(0, 1).toLowerCase()
                .concat(event.getSource().getClass().getSimpleName().substring(1));
        switch (event.getType()) {
            case COMMAND: {
                var handlerBeanName = beanName.replace("Command", "Handler");
                Handler<Command> handler = (Handler) context.getBean(handlerBeanName);
                handler.handle((Command) event.getSource());
                break;
            }
            case QUERY: {
                var resolverBeanName = beanName.replace("Query", "Resolver");
                Resolver<Query> resolver = (Resolver) context.getBean(resolverBeanName);
                resolver.resolve((Query) event.getSource());
                break;
            }
            default:
                throw new ServiceBusInvalidOperationException(
                        new Error(String.format("Service bus doesn't recognize Object of type %s",
                                event.getClass().getCanonicalName())));
        }
    }

}
