package com.suragreat.biz.wechat.service;

import com.suragreat.util.cache.RedisHashCache;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class UserCacheStore extends RedisHashCache {
    private final static String CACHE_NAME = "User";

    public void setUser(WxMpUser user) {
        put(user.getOpenId(), user);
    }

    public WxMpUser getUser(String key) {
        if (StringUtils.isNoneBlank(key)) {
            return get(key, WxMpUser.class);
        }
        return null;
    }

    @Override
    protected String name() {
        return CACHE_NAME;
    }
}
