package com.suragreat.biz.ai.model;

import com.suragreat.base.model.SerializableObject;

public class Template extends SerializableObject{
    private String key;
    private String value;
    private String url;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
