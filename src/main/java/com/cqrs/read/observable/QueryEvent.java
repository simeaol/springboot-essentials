package com.cqrs.read.observable;

import com.cqrs.read.domain.application.Query;
import com.cqrs.common.AbstractInternalEvent;

public class QueryEvent extends AbstractInternalEvent<Query> {

    public QueryEvent(Query query){
        super(query);
    }

    @Override
    public Type getType() {
        return Type.QUERY;
    }
}
