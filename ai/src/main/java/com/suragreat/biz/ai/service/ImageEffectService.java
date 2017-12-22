package com.suragreat.biz.ai.service;

import com.suragreat.base.constant.ServerErrorEnum;
import com.suragreat.biz.ai.sdk.tencent.ImageEffectApi;
import com.suragreat.biz.ai.sdk.tencent.model.ApiResponse;
import com.suragreat.biz.ai.sdk.tencent.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;

@Service
public class ImageEffectService {
    @Autowired
    private ImageEffectApi imageEffectApi = new ImageEffectApi("1106628622", "bHHt4QbzMVEULBVY");

    public String faceMergeRaw(int modelId, String img) {
        String rawData = null;
        try {
            ApiResponse<Map> response = imageEffectApi.facemerge(modelId, img);
            rawData = (String) response.getData().get("image");
        } catch (Exception e) {
            ServerErrorEnum.BACKEND_ERROR.throwsException();
        }
        return rawData;
    }

    public String faceMergeUrl(int modelId, String url) {
        try {
            URL u = new URL(url);
            String img = ImageUtil.toBase64(u.openStream());
            return faceMergeRaw(modelId, img);
        } catch (Exception e) {
            ServerErrorEnum.BACKEND_ERROR.throwsException();
            return null;
        }
    }

    public static void main(String[] a) throws Exception {
        String raw = new ImageEffectService().faceMergeUrl(5, "http://mmbiz.qpic.cn/mmbiz_jpg/YE6zDC5FzEIUedIwHP4Rw1T07ghVMgic3kibbm7q47uGVKB6wAsvXMIibDxdhC2jDCsibAaOjxBR3kibvnAHvocshlw/0");
        ImageUtil.fromBase64(raw, "d:/tmp/d.jpg");
    }
}
