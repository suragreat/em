package com.suragreat.biz.game.service;

import com.suragreat.base.constant.ServerErrorEnum;
import com.suragreat.biz.game.constant.RoleEnum;
import com.suragreat.biz.game.model.Game;
import com.suragreat.biz.game.model.Score;
import com.suragreat.biz.wechat.TokenContext;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GameService {

    private static final java.lang.String GAME_ID_REGEX = "\\w+-\\w+-\\w+-\\w+-\\w+";
    @Autowired
    private GameCacheStore gameCacheService;

    public List<Game> getGames() {
        return gameCacheService.getGames();
    }

    public List<Game> createGame(Game game) {
        game.setCreateTime(new Timestamp(System.currentTimeMillis()));
        game.setCreator(getPlayer(TokenContext.getUser()));
        game.setId(UUID.randomUUID().toString());
        return gameCacheService.addGame(game);
    }

    public List<Game> deleteGame(String gameId) {
        checkCreator(gameId);
        gameCacheService.removeScore(gameId);
        return gameCacheService.removeGame(gameId);
    }

    public Score getScore(String gameId) {
        return gameCacheService.getScore(gameId);
    }

    public Map<String, List<?>> getScoreAsMap(String gameId) {
        Game game = gameCacheService.getGame(gameId);
        if (game == null) {
            ServerErrorEnum.GAME_NOT_EXIST.throwsException();
        }
        Score score = getScore(gameId);
        Map<String, List<?>> result = new HashMap<>();
        result.put("name", score.getPlayers());
        result.put("scores", score.getData());
        result.put("role", getRole(game, score));
        return result;
    }

    private List<RoleEnum> getRole(Game game, Score score) {
        String player = getPlayer(TokenContext.getUser());
        List<RoleEnum> result = new ArrayList<>();
        if (StringUtils.isNoneBlank(player)) {
            if (player.equals(game.getCreator())) {
                result.add(RoleEnum.CREATOR);
            }
            for (String p : score.getPlayers()) {
                if (player.equals(p)) {
                    result.add(RoleEnum.PLAYER);
                    break;
                }
            }
        }
        return result;
    }


    public Score scoreRound(String gameId, Map<String, Integer> round) {
        checkPlayer(gameId);
        return gameCacheService.addScore(gameId, round);
    }


    public Score unscoreRound(String gameId) {
        checkPlayer(gameId);
        return gameCacheService.removeScore(gameId);
    }

    public List<String> getPlayers(String gameId) {
        Score score = getScore(gameId);
        return score.getPlayers();
    }

    public Score addPlayer(String gameId, WxMpUser player) {
        // does not check creator when creating game or join by QR
        return gameCacheService.addPlayer(gameId, getPlayer(player));
    }

    public Score addPlayer(String gameId, String player) {
        checkCreator(gameId);
        return gameCacheService.addPlayer(gameId, player);
    }

    public Score removePlayer(String gameId, WxMpUser player) {
        checkCreator(gameId);
        return gameCacheService.removePlayer(gameId, getPlayer(player));
    }

    public Score removePlayer(String gameId, String player) {
        checkCreator(gameId);
        return gameCacheService.removePlayer(gameId, player);
    }

    protected void checkCreator(String gameId) {
        Game game = gameCacheService.getGame(gameId);
        if (game == null) {
            ServerErrorEnum.GAME_NOT_EXIST.throwsException();
        }
        if (!game.getCreator().equals(getPlayer(TokenContext.getUser()))) {
            ServerErrorEnum.PERMISSION_NOT_ALLOWED.throwsException();
        }
    }

    protected void checkPlayer(String gameId) {
        Game game = gameCacheService.getGame(gameId);
        if (game == null) {
            ServerErrorEnum.GAME_NOT_EXIST.throwsException();
        }
        Score score = getScore(gameId);
        List<RoleEnum> roles = getRole(game, score);
        if (roles == null || roles.size() ==0) {
            ServerErrorEnum.PERMISSION_NOT_ALLOWED.throwsException();
        }

    }

    public String getPlayer(WxMpUser user) {
        if (user != null) {
            return StringUtils.split(user.getNickname(), " ")[0];
        }
        return null;
    }

    public Set<String> getPlayers() {
        return gameCacheService.getPlayers();
    }

    public List<RoleEnum> getRole(String gameId) {
        Game game = gameCacheService.getGame(gameId);
        Score score = getScore(gameId);
        return getRole(game, score);
    }

    public void checkUrl(String url) {
        Pattern pattern = Pattern.compile(GAME_ID_REGEX);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String id = matcher.group();
            checkCreator(id);
        }
    }
}
