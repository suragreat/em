package com.suragreat.base.constant;

public enum ServerErrorEnum implements ServerExceptionThrowable {
    HTTP_REDIRECT(302, "%s"),

    INTERNAL_ERROR(5000, "服务器内部错误"),
    ILLEGAL_PARAMETER(5001, "非法参数: %s"),
    UPDATE_FAILED_DUE_TO_SYNC(5002, "同步更新未成功更新, 请刷新后重试"),
    GET_LOCK_FAILED(5003, "获取同步锁失败, 请稍后重试"),
    BACKEND_ERROR(5004, "后台请求错误"),
    ILLGAL_CONFIGURATION(5005, "配置参数错误或缺失: %s"),
    PERMISSION_NOT_ALLOWED(5006, "您无权操作"),

    INVALID_AREA_ID(6001, "非法的地址ID: %s"),

    PLAYER_ALEADY_JOININ(7001, "玩家[%s]已经加入了"),
    GAME_NOT_EXIST(7002, "游戏未创建或已关闭"),
    ;

    private final int code;
    private final String message;

    ServerErrorEnum(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
