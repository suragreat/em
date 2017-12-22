package com.suragreat.biz.game.service;

import com.suragreat.util.cache.RedisSetCache;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PlayerCacheStore extends RedisSetCache {
    private final static String CACHE_NAME = "Player";

    @Override
    protected String name() {
        return CACHE_NAME;
    }

    public Set<String> getPlayers() {
        return getAll(String.class);
    }

    public void addPlayer(String player) {
        put(player);
    }
}
