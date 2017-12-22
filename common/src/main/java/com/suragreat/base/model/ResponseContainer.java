package com.suragreat.base.model;

import com.suragreat.base.exception.ServerException;

import io.swagger.annotations.ApiModelProperty;

public class ResponseContainer extends SerializableObject {
    public static int SUCCESS = 200;
    private static final long serialVersionUID = 6638812898175787289L;
    @ApiModelProperty(value = "状态码: 200表示成功, 其他响应码均表示失败; 当响应码非200时, 响应信息中包含了错误描述信息", required = true, example="200")
   private int status = SUCCESS;
    @ApiModelProperty(value = "响应信息")
    private String message;
    @ApiModelProperty(value = "响应处理时间, 单位为毫秒")
    private Long tcost;

    public ResponseContainer() {
    }

    public ResponseContainer(ServerException e) {
        setStatus(e.getCode());
        setMessage(e.getMessage());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int code) {
        this.status = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTcost() {
        return tcost;
    }

    public void setTcost(Long tcost) {
        this.tcost = tcost;
    }
}
