package com.suragreat.biz.ai.sdk.tencent;

import com.suragreat.biz.ai.sdk.tencent.model.ApiResponse;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class NlpApi extends BaseApi {
    private static final String textchatUrl = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";


    public NlpApi(String appID, String appKey) {
        super(appID, appKey);
    }


    public ApiResponse<Map> textchat(String session, String question) throws UnsupportedEncodingException {

        Map<String, String> params = buildCommonParam();

        params.put("question", question);
        params.put("session", session);

        ApiResponse<Map> ApiResponse = postApi(textchatUrl, params);


        return ApiResponse;

    }
}
