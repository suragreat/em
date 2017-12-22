package com.suragreat.biz.game.controller;

import com.suragreat.base.controller.BaseController;
import com.suragreat.base.model.ResponseContainer;
import com.suragreat.base.model.ResponseListContainer;
import com.suragreat.base.model.ResponseObjectContainer;
import com.suragreat.biz.game.HttpConstant;
import com.suragreat.biz.game.constant.RoleEnum;
import com.suragreat.biz.game.model.Game;
import com.suragreat.biz.game.service.GameService;
import com.suragreat.biz.wechat.TokenContext;
import com.suragreat.biz.wechat.service.UserCacheStore;
import com.suragreat.util.JwtUtils;
import io.jsonwebtoken.Claims;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.suragreat.biz.game.HttpConstant.PAGE_ROOT_PATH;

@Controller
@RequestMapping(value = HttpConstant.URL_PREFIX)
public class GameController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(GameController.class);
    @Autowired
    private GameService gameService;

    @Autowired
    private UserCacheStore userCacheService;

    @RequestMapping(value = "/game/{gameId}/invite", method = RequestMethod.GET)
    public void joinByQR(@PathVariable String gameId, @RequestParam String code, @RequestParam String state, HttpServletResponse response) {
        try {
            gameService.addPlayer(gameId, TokenContext.getUser());
        } catch (Exception e) {
            logger.warn("ignore add player exception", e);
        }
        try {
            response.sendRedirect(PAGE_ROOT_PATH + "score?id=" + gameId);
        } catch (IOException e) {
            logger.error("failed to sendRedirect", e);
        }
    }

    @RequestMapping(value = "/game", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseListContainer<Game> getGames() {
        return successList(gameService.getGames());
    }

    @RequestMapping(value = "/game", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseListContainer createGame(@RequestBody Game game) {

        List<Game> list = gameService.createGame(game);
        gameService.addPlayer(game.getId(), TokenContext.getUser());
        return successList(list);
    }


    @RequestMapping(value = "/game/{gameId}/close", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseListContainer<Game> closeGame(@PathVariable String gameId) {
        return successList(gameService.deleteGame(gameId));
    }

    @RequestMapping(value = "/game/{gameId}/quit", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseContainer quitGame(@PathVariable String gameId, @RequestBody List<String> players) {
        if (players != null) {
            players.forEach(p -> {
                gameService.removePlayer(gameId, p);
                Claims claims = JwtUtils.getClaimsFromToken(p);
                if (claims != null) {
                    WxMpUser user = userCacheService.getUser(claims.get("user", String.class));
                    if (user != null) {
                        gameService.removePlayer(gameId, user);
                    }
                }
            });
        }
        return success();
    }

    @RequestMapping(value = "/game/{gameId}/join", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseContainer joinGame(@PathVariable String gameId, @RequestBody Map<String, String> map) {
        gameService.addPlayer(gameId, map.get("player"));
        return success();
    }

    @RequestMapping(value = "/game/{gameId}/player", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseListContainer<String> getPlayers(@PathVariable String gameId) {
        return successList(gameService.getPlayers(gameId));
    }

    @RequestMapping(value = "/game/{gameId}/score", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseObjectContainer<Map<String, List<?>>> getScores(@PathVariable String gameId) {
        return success(gameService.getScoreAsMap(gameId));
    }

    @RequestMapping(value = "/game/{gameId}/score", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseContainer score(@PathVariable String gameId, @RequestBody Map<String, Integer> round) {
        gameService.scoreRound(gameId, round);
        return success();
    }

    @RequestMapping(value = "/game/{gameId}/score/undo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseContainer unscore(@PathVariable String gameId) {
        gameService.unscoreRound(gameId);
        return success();
    }

    @RequestMapping(value = "/game/{gameId}/role", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseListContainer<RoleEnum> getRole(@PathVariable String gameId) {
        return successList(gameService.getRole(gameId));
    }

    @RequestMapping(value = "/player", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseListContainer<String> getPlayers() {
        return successList(gameService.getPlayers());
    }

}
