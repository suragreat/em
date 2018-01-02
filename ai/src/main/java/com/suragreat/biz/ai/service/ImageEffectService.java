package com.suragreat.biz.ai.service;

import com.suragreat.base.constant.ServerErrorEnum;
import com.suragreat.biz.ai.model.Template;
import com.suragreat.biz.ai.sdk.tencent.ImageEffectApi;
import com.suragreat.biz.ai.sdk.tencent.model.ApiResponse;
import com.suragreat.biz.ai.sdk.tencent.util.ImageUtil;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImageEffectService {
    @Autowired
    private ImageEffectApi imageEffectApi;//= new ImageEffectApi("1106628622", "bHHt4QbzMVEULBVY");
    private List<Template> templates;
    private static final String[] TEMPLATE_NAME = {"奇迹 ", "压岁钱 ", "范蠡 ", "李白 ", "孙尚香 ", "花无缺 ", "西施 ", "杨玉环 ", "白浅 ", "凤九 ", "夜华 ", "年年有余 ", "新年萌萌哒 ", "王者荣耀荆轲 ", "王者荣耀李白 ", "王者荣耀哪吒 ", "王者荣耀王昭君 ", "王者荣耀甄姬 ", "王者荣耀诸葛亮 ", "赵灵儿 ", "李逍遥 ", "爆炸头 ", "村姑 ", "光头 ", "呵呵哒 ", "肌肉 ", "肉山 ", "机智 "};

    @PostConstruct
    public void init() {
        templates = new ArrayList<>();
        for (int i = 1; i <= TEMPLATE_NAME.length; i++) {
            Template t = new Template();
            t.setKey(String.valueOf(i));
            t.setValue(TEMPLATE_NAME[i - 1]);
            t.setUrl(String.format("https://yyb.gtimg.com/aiplat/ai/upload/doc/facemerge/%d.png", i));
            templates.add(t);
        }
    }

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

    public List<Template> getTemplates() {
        return templates;
    }


}
