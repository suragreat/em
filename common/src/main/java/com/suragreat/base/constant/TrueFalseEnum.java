package com.suragreat.base.constant;

public enum TrueFalseEnum implements SerializableEnum {
    TRUE(true), FALSE(false),;

    private final String code;
    private final boolean value;

    TrueFalseEnum(boolean value) {
        this.value = value;
        this.code = value ? "1" : "0";
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return name().toLowerCase();
    }

    public boolean value(){
        return value;
    }
    
    public static boolean isTrue(TrueFalseEnum enm) {
        return enm != null && enm.value;
    }

    public static TrueFalseEnum valueOf(boolean val) {
        return val ? TRUE : FALSE;
    }

}
