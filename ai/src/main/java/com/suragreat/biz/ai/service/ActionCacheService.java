package com.suragreat.biz.ai.service;

import com.suragreat.base.util.JsonUtils;
import com.suragreat.biz.ai.constant.ActionEnum;
import com.suragreat.util.cache.RedisHashCache;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Set;

@Service
public class ActionCacheService extends RedisHashCache {
    protected static Logger logger = LoggerFactory.getLogger(ActionCacheService.class);

    public ActionEnum getMenuAction(WxMpXmlMessage wxMessage) {
        switch (wxMessage.getEventKey()) {
            case "ai_pic_weixin_dish":
            case "ai_pic_sysphoto_dish":
                return ActionEnum.DISH_DETECT;
            case "ai_pic_weixin_face_merge":
            case "ai_pic_sysphoto_face_merge":
                return ActionEnum.FACE_MERGE;
        }
        logger.warn("Action not matched for message: " + JsonUtils.toJSON(wxMessage));
        return null;
    }


    public void setActionCache(WxMpXmlMessage wxMessage) {
        logger.info("setActionCache");
        String key = getMessageKey(wxMessage);
        ActionEnum action = getMenuAction(wxMessage);
        put(key, getCreateTime(wxMessage), action);
        logger.info("setActionCache, key is " + key + ", action is " + action);
    }

    private String getCreateTime(WxMpXmlMessage wxMessage) {
        return String.valueOf(wxMessage.getCreateTime());
    }

    public ActionEnum getActionCache(WxMpXmlMessage wxMessage) {
        logger.info("getActionCache");
        String key = getMessageKey(wxMessage);

        long expiry = System.currentTimeMillis() + 2000;
        Set<String> times = getKeys(key);
        logger.info("getActionCache key is " + key + ", available keys are " + times);
        while (System.currentTimeMillis() <= expiry) {
            Iterator<String> iter = times.iterator();
            while (iter.hasNext()) {
                String entry = iter.next();
                if (Math.abs(Long.valueOf(entry) - wxMessage.getCreateTime()) < 2) {
                    ActionEnum val = get(key, entry, ActionEnum.class);
                    logger.info("getActionCache result: " + val);
                    remove(key, entry);
                    return val;
                }
            }
            sleep(100);
            times = getKeys(key);
        }

        logger.info("getActionCache is null");
        return null;
    }

    private void sleep(long i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getMessageKey(WxMpXmlMessage wxMessage) {
        return String.format("action-%s|%s", wxMessage.getFromUser(), wxMessage.getToUser());
    }

    @Override
    protected String name() {
        return null;
    }
}
