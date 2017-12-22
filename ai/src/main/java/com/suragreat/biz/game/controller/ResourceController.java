package com.suragreat.biz.game.controller;


import com.suragreat.base.controller.BaseController;
import com.suragreat.base.model.ResponseContainer;
import com.suragreat.base.model.ResponseListContainer;
import com.suragreat.base.model.ResponseObjectContainer;
import com.suragreat.biz.game.HttpConstant;
import com.suragreat.biz.game.model.Swiper;
import com.suragreat.biz.game.service.GameService;
import com.suragreat.biz.game.service.SwiperCacheStore;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import static com.suragreat.biz.game.HttpConstant.PAGE_ROOT_PATH;

@RestController
@RequestMapping(value = {HttpConstant.URL_PREFIX}, produces = {"application/json"})
public class ResourceController extends BaseController {
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private SwiperCacheStore swiperService;

    @Autowired(required = false)
    private GameService urlChecker;
    private Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @RequestMapping(value = {"/wechat/redirect"}, method = {RequestMethod.POST})
    public ResponseObjectContainer<String> getWechatRedirectUrl(@RequestBody Map<String, String> map) {
        String state = "QR";
        String scope = "snsapi_userinfo";
        String url = map.get("url");
        if (urlChecker != null) {
            urlChecker.checkUrl(url);
        }
        try {
            url = URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
        }

        String appid = wxMpService.getWxMpConfigStorage().getAppId();
        return success(String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect", appid, url, scope, state));
    }

    @RequestMapping(value = {"/swiper"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResponseListContainer<Swiper> getSwipers(HttpServletRequest request) {
        return successList(swiperService.getSwipers());
    }

    @RequestMapping(value = {"/swiper"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseContainer setSwipers(@RequestBody Set<Swiper<?>> swipers) {
        swiperService.setSwipers(swipers);
        return success();
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public void index(HttpServletResponse response, @RequestParam String state) {
        try {
            String url = "/";
            if ("game".equalsIgnoreCase(state)) {
                url = PAGE_ROOT_PATH + "game";
            } else if ("face".equalsIgnoreCase(state)) {
                url = PAGE_ROOT_PATH + "face";
            }
            response.sendRedirect(url);
        } catch (IOException e) {
            logger.error("failed to sendRedirect", e);
        }
    }
}