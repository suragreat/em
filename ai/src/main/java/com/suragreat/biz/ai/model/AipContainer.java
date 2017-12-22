package com.suragreat.biz.ai.model;

import com.suragreat.base.model.SerializableObject;

import java.util.List;

public class AipContainer<T> extends SerializableObject {
    private List<T> result;
    private long logId;
    private int resultNum;
    public AipContainer() {
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public int getResultNum() {
        return resultNum;
    }

    public void setResultNum(int resultNum) {
        this.resultNum = resultNum;
    }
}
