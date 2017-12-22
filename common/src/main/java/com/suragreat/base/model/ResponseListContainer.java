package com.suragreat.base.model;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class ResponseListContainer<T> extends ResponseContainer {
    private static final long serialVersionUID = 6631265210419592010L;
    @ApiModelProperty(value = "响应数据列表")
    private List<T> data = new ArrayList<>();

    public ResponseListContainer() {
    }

    public ResponseListContainer(List<T> data) {
        setData(data);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @ApiModelProperty(value = "响应数据列表长度")
    public int getSize() {
        return data != null ? data.size() : 0;
    }
}
