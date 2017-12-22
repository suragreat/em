package com.suragreat.base.controller;

import com.suragreat.base.model.ResponseContainer;
import com.suragreat.base.model.ResponseListContainer;
import com.suragreat.base.model.ResponseObjectContainer;
import com.suragreat.base.util.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class BaseController {

    protected ResponseContainer success() {
        return success(null);
    }

    protected <T> ResponseObjectContainer<T> success(T data) {
        ResponseObjectContainer<T> response = new ResponseObjectContainer<>(data);
        setResponse(response);
        return response;
    }

    protected <T> ResponseListContainer<T> successList(T[] data) {
        return successList(Arrays.asList(data));
    }

    protected <T> ResponseListContainer<T> successList(Set<T> data) {
        return successList(new ArrayList<>(data));
    }

    protected <T> ResponseListContainer<T> successList(List<T> data) {
        ResponseListContainer<T> response = new ResponseListContainer<>(data);
        setResponse(response);
        return response;
    }

    private void setResponse(ResponseContainer response) {
        LogUtil.logInThreadLocal(response);
        response.setTcost(LogUtil.getTimeCost());
    }

}
