package com.suragreat.biz.ai.sdk.tencent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suragreat.biz.ai.sdk.tencent.model.ApiResponse;
import com.suragreat.biz.ai.sdk.tencent.util.AuthSingature;
import com.suragreat.biz.ai.sdk.tencent.util.HttpUtil;
import com.suragreat.biz.ai.sdk.tencent.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BaseApi {
    protected static String appID = "";
    protected static String appKey = "";

    public BaseApi(String appID, String appKey) {
        this.appID = appID;
        this.appKey = appKey;
    }


    /**
     * 构建公共参数（不包含签名参数sign）
     *
     * @return 返回公共参数（不包含签名参数sign）
     */
    protected Map<String, String> buildCommonParam() {

        Map<String, String> params = new HashMap<String, String>();

        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

        params.put("app_id", appID);
        params.put("time_stamp", timeStamp);
        params.put("nonce_str", RandomUtil.generateRandomString(16));

        return params;
    }

    protected <T> ApiResponse<T> postApi(String url, Map<String, String> params) throws UnsupportedEncodingException {

        if (StringUtils.isEmpty(url)) {
            return null;
        }

        if (params == null || params.size() <= 3) {
            return null;
        } else {
            //如果sign参数不存在，那就签名并且加上sign参数
            if (params.get("sign") == null) {
                String sign = AuthSingature.sign(appKey, params);
                params.put("sign", sign);
            }
        }

        String jsonStr = HttpUtil.post(url, params);

        Gson gson = new Gson();
        Type type = new TypeToken<ApiResponse<T>>() {
        }.getType();

        return gson.fromJson(jsonStr, type);

    }
}
