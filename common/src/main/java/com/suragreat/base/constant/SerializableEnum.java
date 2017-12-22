package com.suragreat.base.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public interface SerializableEnum {
    @JsonValue
    String getCode();

    String getDescription();

    @SuppressWarnings("unchecked")
    static <T extends Enum<T>> T valueOfEnum(Class<T> enumClass, String code) {
        if (code == null) {
            return null;
        }
        if (enumClass.isAssignableFrom(SerializableEnum.class))
            throw new IllegalArgumentException("illegal SerializableEnum type");
        T[] enums = enumClass.getEnumConstants();
        for (T t : enums) {
            SerializableEnum serializableEnum = (SerializableEnum) t;
            if (serializableEnum.getCode().equals(code))
                return (T) serializableEnum;
        }
        throw new IllegalArgumentException("cannot parse string: " + code + " to " + enumClass.getName());
    }
}
