package com.suragreat.biz.game.service;

import com.suragreat.base.constant.ServerErrorEnum;
import com.suragreat.base.service.annotation.Lock;
import com.suragreat.base.service.annotation.LockKey;
import com.suragreat.biz.game.model.Score;
import com.suragreat.util.cache.RedisHashCache;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ScoreCacheStore extends RedisHashCache {
    public final static String CACHE_NAME = "Score";

    @Lock(CACHE_NAME)
    public Score addScore(@LockKey String gameId, Map<String, Integer> round) {
        Score score = getScore(gameId);
        score.addRound(round);
        put(gameId, score);
        return score;
    }

    public Score getScore(String gameId) {
        Score result = get(gameId, Score.class);
        if (result == null) {
            result = new Score();
        }
        return result;
    }

    @Override
    protected String name() {
        return CACHE_NAME;
    }

    @Lock(CACHE_NAME)
    public Score removeScore(@LockKey String gameId) {
        Score score = getScore(gameId);
        score.removeRound();
        put(gameId, score);
        return score;
    }

    @Lock(CACHE_NAME)
    public Score addPlayer(@LockKey String gameId, String player) {
        Score score = getScore(gameId);
        if (score.getPlayers().contains(player)) {
            ServerErrorEnum.PLAYER_ALEADY_JOININ.throwsException(player);
        }
        score.addPlayer(player);
        put(gameId, score);
        return score;
    }


    @Lock(CACHE_NAME)
    public Score removePlayer(@LockKey String gameId, String player) {
        Score score = getScore(gameId);
        score.removePlayer(player);
        put(gameId, score);
        return score;
    }
}
