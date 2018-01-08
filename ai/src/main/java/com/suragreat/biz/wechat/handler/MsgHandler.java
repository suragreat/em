package com.suragreat.biz.wechat.handler;


import com.suragreat.base.util.CryptUtil;
import com.suragreat.base.util.JsonUtils;
import com.suragreat.biz.ai.service.NlpService;
import com.suragreat.biz.wechat.builder.TextBuilder;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {
    @Autowired
    private NlpService nlpService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        try {
            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
                    && weixinService.getKefuService().kfOnlineList()
                    .getKfOnlineList().size() > 0) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
                        .fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        //TODO 组装回复消息
        String content;
        if ("whoami".equalsIgnoreCase(wxMessage.getContent()) || "whoru".equalsIgnoreCase(wxMessage.getContent())) {
            content = "I'm Wayne. Nice to meet you.";
        } else {
            content = nlpService.ask(CryptUtil.md5(wxMessage.getFromUser(), "abc"), wxMessage.getContent());
        }
        if (StringUtils.isBlank(content)) {
            content = "收到信息内容：" + JsonUtils.toJSON(wxMessage);
        }

        return new TextBuilder().build(content, wxMessage, weixinService);

    }

}
