package com.suragreat.biz.wechat.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TokenCacheService {
    private final static String CACHE_NAME = "Token";

    @Cacheable(cacheNames = CACHE_NAME, key = "#code")
    public String getOpenId(String code) {
        return null;
    }

    @CachePut(cacheNames = CACHE_NAME, key = "#code")
    public String setOpenId(String code, String openId) {
        return openId;
    }
}
