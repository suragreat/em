package com.suragreat.biz.game.service;

import com.suragreat.biz.game.model.Game;
import com.suragreat.biz.game.model.Score;
import com.suragreat.util.cache.RedisHashCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GameCacheStore extends RedisHashCache {
    private final static String CACHE_NAME = "Game";
    @Autowired
    private ScoreCacheStore scoreCacheStore;

    @Autowired
    private PlayerCacheStore playerCacheStore;

    public List<Game> getGames() {
        List<Game> result = getAll(Game.class);
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    public List<Game> addGame(Game game) {
        put(game.getId(), game);
        return getGames();
    }

    public List<Game> removeGame(String gameId) {
        remove(gameId);
        return getGames();
    }

    public Game getGame(String gameId) {
        return get(gameId, Game.class);
    }


    public Score getScore(String gameId) {
        return scoreCacheStore.getScore(gameId);
    }


    public Score addScore(String gameId, Map<String, Integer> round) {
        return scoreCacheStore.addScore(gameId, round);
    }

    @Override
    protected String name() {
        return CACHE_NAME;
    }

    public Score addPlayer(String gameId, String player) {
        playerCacheStore.addPlayer(player);
        return scoreCacheStore.addPlayer(gameId, player);
    }

    public Score removeScore(String gameId) {
        return scoreCacheStore.removeScore(gameId);
    }

    public Score removePlayer(String gameId, String player) {
        return scoreCacheStore.removePlayer(gameId, player);
    }

    public Set<String> getPlayers() {
        return playerCacheStore.getPlayers();
    }
}
