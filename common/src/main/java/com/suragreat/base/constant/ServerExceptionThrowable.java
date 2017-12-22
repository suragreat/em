package com.suragreat.base.constant;

import com.suragreat.base.exception.ServerException;

public interface ServerExceptionThrowable {
    default void throwsException(Object... args) {
        throw new ServerException(this, args);
    }

    String getMessage();

    int getCode();
}
