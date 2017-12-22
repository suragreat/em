package com.suragreat.biz.game.constant;

import com.suragreat.base.constant.SerializableEnum;

public enum  RoleEnum implements SerializableEnum {
    CREATOR("1", "CREATOR"),
    PLAYER("2", "PLAYER"),
    ;

    private final String code;
    private final String description;

    RoleEnum(String code, String description) {
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
