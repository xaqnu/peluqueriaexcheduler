package com.unax.peluqueriascheduler.dbfilters;

import org.jooq.Condition;
import org.jooq.DSLContext;

public interface Filter {
    Condition toCondition(DSLContext dsl);
}
