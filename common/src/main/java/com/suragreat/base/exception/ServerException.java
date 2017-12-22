package com.suragreat.base.exception;

import com.suragreat.base.constant.ServerErrorEnum;
import com.suragreat.base.constant.ServerExceptionThrowable;

public class ServerException extends RuntimeException {
    private static final long serialVersionUID = -4555238804426162641L;
    private final int code;

    public ServerException(ServerExceptionThrowable error, Object... args) {
        super(String.format(error.getMessage(), args));
        this.code = error.getCode();
    }

    public ServerException(ServerExceptionThrowable error, Throwable t, Object... args) {
        super(String.format(error.getMessage(), args), t);
        this.code = error.getCode();
    }

    public int getCode() {
        return code;
    }

}
