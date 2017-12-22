package com.suragreat.base.exception;

import com.suragreat.base.constant.ServerErrorEnum;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.suragreat.base.model.ResponseContainer;
import com.suragreat.base.util.LogUtil;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler({ TypeMismatchException.class })
    public ResponseContainer typeMismatchHandler(TypeMismatchException exception) {
        String detailMsg = "值 '" + exception.getValue() + "' 不是 '" + exception.getRequiredType() + "' 类型";
        if (exception instanceof MethodArgumentTypeMismatchException) {
            detailMsg = "字段 '" + ((MethodArgumentTypeMismatchException) exception).getName() + "' 非法, " + detailMsg;
        }
        ResponseContainer result = new ResponseContainer(
                new ServerException(ServerErrorEnum.ILLEGAL_PARAMETER, detailMsg, exception));
        setResponse(result);
        return result;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseContainer methodArgumentNotValidHandler(MethodArgumentNotValidException exception) throws Exception {
        String detailMsg = "";
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            detailMsg += "字段 '" + error.getField() + "' 非法: " + error.getDefaultMessage() + "; ";
        }

        ResponseContainer result = new ResponseContainer(
                new ServerException(ServerErrorEnum.ILLEGAL_PARAMETER, detailMsg, exception));
        setResponse(result);
        return result;
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseContainer httpMessageConversionHandler(HttpMessageConversionException exception) throws Exception {
        String detailMsg = "HTTP请求报文不合法";

        ResponseContainer result = new ResponseContainer(
                new ServerException(ServerErrorEnum.ILLEGAL_PARAMETER, detailMsg, exception));
        setResponse(result);
        return result;
    }

    private void setResponse(ResponseContainer result) {
        LogUtil.logInThreadLocal(result);
        result.setTcost(LogUtil.getTimeCost());
    }

}
