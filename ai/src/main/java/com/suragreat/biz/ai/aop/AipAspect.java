package com.suragreat.biz.ai.aop;

import com.suragreat.base.constant.ServerErrorEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AipAspect {
    private static Logger logger = LoggerFactory.getLogger(AipAspect.class);


    @Around("bean(aip*)")
    public Object process(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = pjp.proceed();
        if (obj != null && JSONObject.class.isAssignableFrom(obj.getClass())) {
            JSONObject jsonObject = (JSONObject) obj;
            if (jsonObject.has("error_code")) {
                logger.error("Failed to call AIP interface, erro code: {}, error message: {}", jsonObject.get("error_code"), jsonObject.get("error_msg"));
                ServerErrorEnum.BACKEND_ERROR.throwsException();
            }
        }
        return obj;
    }
}
