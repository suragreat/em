package com.suragreat.base.model;

import io.swagger.annotations.ApiModelProperty;

public class ResponseObjectContainer<T> extends ResponseContainer {
    private static final long serialVersionUID = 3545126724325402466L;
    @ApiModelProperty(value = "响应数据")
    private T data;

    public ResponseObjectContainer() {
    }

    public ResponseObjectContainer(T data) {
        setData(data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
