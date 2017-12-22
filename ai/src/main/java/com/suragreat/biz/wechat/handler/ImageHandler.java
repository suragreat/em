package com.suragreat.biz.wechat.handler;

import com.suragreat.biz.ai.constant.ActionEnum;
import com.suragreat.biz.ai.sdk.tencent.util.ImageUtil;
import com.suragreat.biz.ai.service.ImageClassifyService;
import com.suragreat.biz.ai.service.ActionCacheService;
import com.suragreat.biz.ai.service.ImageEffectService;
import com.suragreat.biz.wechat.builder.ImageBuilder;
import com.suragreat.biz.wechat.builder.TextBuilder;
import com.suragreat.base.util.JsonUtils;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.common.util.fs.FileUtils;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Component
public class ImageHandler extends AbstractHandler {
    @Autowired
    private ImageClassifyService imageClassifyService;

    @Autowired
    private ImageEffectService imageEffectService;

    @Autowired
    private ActionCacheService menuCacheService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        String content;
        if (WxConsts.XmlMsgType.EVENT.equals(wxMessage.getMsgType())) {
            menuCacheService.setActionCache(wxMessage);
            content = JsonUtils.toJSON(wxMessage);
        } else {
            ActionEnum action = menuCacheService.getActionCache(wxMessage);
            if (action != null) {
                switch (action) {
                    case DISH_DETECT:
                        content = imageClassifyService.dishDetect(wxMessage.getPicUrl());
                        break;
                    case FACE_MERGE:
                        content= imageEffectService.faceMergeUrl(2, wxMessage.getPicUrl());
                        InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(content));
                        WxMpMaterial material = new WxMpMaterial();
                        material.setName("result");
                        try {
                            material.setFile( FileUtils.createTmpFile(is, UUID.randomUUID().toString(), "jpg"));
                        } catch (IOException e) {
                            throw new WxErrorException(null, e);
                        }
                        WxMpMaterialUploadResult result = wxMpService.getMaterialService().materialFileUpload("image", material);
                        IOUtils.closeQuietly(is);
                        logger.info(result.getUrl());
                        return new ImageBuilder().build(result.getMediaId(), wxMessage,wxMpService);
                    default:
                        logger.error("action is not matched");
                        content = JsonUtils.toJSON(wxMessage);
                }
            } else {
                logger.error("action is null");
                content = JsonUtils.toJSON(wxMessage);
            }
        }
        return new TextBuilder().build(content, wxMessage, wxMpService);
    }
}
