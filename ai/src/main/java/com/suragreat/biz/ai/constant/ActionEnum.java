package com.suragreat.biz.ai.constant;

import com.suragreat.base.constant.SerializableEnum;

public enum ActionEnum implements SerializableEnum {
    DISH_DETECT("1", "DISH_DETECT"), FACE_MERGE("2", "FACE_MERGE"),;

    private final String code;
    private final String description;

    ActionEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
