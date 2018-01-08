package com.suragreat.biz.ai.service;

import com.suragreat.base.constant.ServerErrorEnum;
import com.suragreat.biz.ai.sdk.tencent.NlpApi;
import com.suragreat.biz.ai.sdk.tencent.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public class NlpService {
    @Autowired
    private NlpApi nlpApi;

    public String ask(String session, String question) {
        try {
            ApiResponse<Map> resp = nlpApi.textchat(session, question);
            return (String) resp.getData().get("answer");
        } catch (UnsupportedEncodingException e) {
            ServerErrorEnum.BACKEND_ERROR.throwsException();
            return null;
        }
    }
}
