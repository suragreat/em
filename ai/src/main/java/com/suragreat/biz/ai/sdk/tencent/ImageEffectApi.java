package com.suragreat.biz.ai.sdk.tencent;

import com.suragreat.biz.ai.sdk.tencent.model.ApiResponse;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ImageEffectApi extends BaseApi {
    private static final String facemergeUrl = "https://api.ai.qq.com/fcgi-bin/ptu/ptu_facemerge";

    private static final String facestickerUrl = "https://api.ai.qq.com/fcgi-bin/ptu/ptu_facesticker";

    private static final String facedecorationUrl = "https://api.ai.qq.com/fcgi-bin/ptu/ptu_facedecoration";

    private static final String imgfilterUrl = "https://api.ai.qq.com/fcgi-bin/ptu/ptu_imgfilter";

    private static final String facecosmeticUrl = "https://api.ai.qq.com/fcgi-bin/ptu/ptu_facecosmetic";

    public ImageEffectApi(String appID, String appKey) {
        super(appID, appKey);
    }


    public ApiResponse<Map> facemerge(int modelId, String base64Image) throws UnsupportedEncodingException {

        Map<String, String> params = buildCommonParam();

        params.put("model", String.valueOf(modelId));
        params.put("image", base64Image);

        ApiResponse<Map> ApiResponse = postApi(facemergeUrl, params);


        return ApiResponse;

    }


    public ApiResponse<Map> facesticker(int stickerId, String base64Image) throws UnsupportedEncodingException {

        Map<String, String> params = buildCommonParam();

        params.put("sticker", String.valueOf(stickerId));
        params.put("image", base64Image);

        ApiResponse<Map> ApiResponse = postApi(facestickerUrl, params);

        return ApiResponse;

    }

    public ApiResponse<Map> facedecoration(int decorationId, String base64Image) throws UnsupportedEncodingException {

        Map<String, String> params = buildCommonParam();

        params.put("decoration", String.valueOf(decorationId));
        params.put("image", base64Image);

        ApiResponse<Map> ApiResponse = postApi(facedecorationUrl, params);

        return ApiResponse;

    }

    public ApiResponse<Map> imgfilter(int filterId, String base64Image) throws UnsupportedEncodingException {

        Map<String, String> params = buildCommonParam();

        params.put("filter", String.valueOf(filterId));
        params.put("image", base64Image);

        ApiResponse<Map> ApiResponse = postApi(imgfilterUrl, params);

        return ApiResponse;

    }

    public ApiResponse<Map> facecosmetic(int cosmeticId, String base64Image) throws UnsupportedEncodingException {

        Map<String, String> params = buildCommonParam();

        params.put("filter", String.valueOf(cosmeticId));
        params.put("image", base64Image);

        ApiResponse<Map> ApiResponse = postApi(facecosmeticUrl, params);

        return ApiResponse;

    }
}
