package com.suragreat.biz.wechat;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

public class TokenContext {
    private static ThreadLocal<WxMpUser> userThreadLocal = new ThreadLocal<>();

    public static void setUser(WxMpUser user) {
        userThreadLocal.set(user);
    }

    public static WxMpUser getUser() {
        return userThreadLocal.get();
    }

 }
