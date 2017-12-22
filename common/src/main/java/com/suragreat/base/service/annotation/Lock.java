package com.suragreat.base.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Lock {
    String value();

    long keepMills() default 30000;

    LockFailAction action() default LockFailAction.GIVEUP;

    public enum LockFailAction {
        GIVEUP, RETRY;
    }

    long sleepMills() default 100;

    long maxWaitMills() default 30000;
}
