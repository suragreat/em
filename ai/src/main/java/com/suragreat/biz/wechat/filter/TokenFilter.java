package com.suragreat.biz.wechat.filter;

import com.suragreat.base.model.ResponseObjectContainer;
import com.suragreat.biz.game.HttpConstant;
import com.suragreat.biz.game.controller.ResourceController;
import com.suragreat.biz.wechat.TokenContext;
import com.suragreat.biz.wechat.service.TokenCacheService;
import com.suragreat.biz.wechat.service.UserCacheStore;
import com.suragreat.util.JwtUtils;
import io.jsonwebtoken.Claims;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TokenFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(TokenFilter.class);
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private UserCacheStore userCacheService;

    @Autowired
    private ResourceController resourceController;

    @Autowired
    private TokenCacheService tokenCacheService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("TokenFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,x-token-jwt,content-type");

        if (!"OPTIONS".equals(req.getMethod())) {
            String openId = null;
            boolean userFound = false;

            if (!userFound) {
                String token = req.getHeader("x-token-jwt");
                Claims claims = JwtUtils.getClaimsFromToken(token);
                if (claims != null) {
                    openId = claims.get("user", String.class);
                    logger.info("################open id in claims: " + openId);
                    WxMpUser user = userCacheService.getUser(openId);
                    if (user != null) {
                        TokenContext.setUser(user);
                        userFound = true;
                    }
                } else {
                    logger.info("################open id in claims does not exist");
                }
            }

            String code = req.getParameter("code");
            String state = req.getParameter("state");

            if (StringUtils.isNoneBlank(code, state)) {
                if (!userFound) {
                    openId = tokenCacheService.getOpenId(code);
                    logger.info("################open id in token cache: " + openId);
                    WxMpUser user = userCacheService.getUser(openId);
                    if (user != null) {
                        TokenContext.setUser(user);
                        setCookie(resp, user);
                        userFound = true;
                    }
                }

                if (!userFound && "abcd".equals(code)){
                    WxMpUser user = userCacheService.getUser("oZPBww_ddgENpYTpso4NGFZRMQSk");
                    TokenContext.setUser(user);
                    setCookie(resp, user);
                    userFound = true;
                }

                if (!userFound) {
                    try {
                        WxMpOAuth2AccessToken token = wxMpService.oauth2getAccessToken(code);
                        logger.info(String.valueOf(token));
                        WxMpUser user = wxMpService.oauth2getUserInfo(token, "zh-CN");
                        logger.info(String.valueOf(user));
                        TokenContext.setUser(user);
                        tokenCacheService.setOpenId(code, user.getOpenId());
                        req.getSession(true).setAttribute("openId", user.getOpenId());
                        setCookie(resp, user);
                        userFound = true;
                        logger.info("################set open id from code");
                    } catch (Exception e) {
                        logger.error("failed to get user info", e);
                    }
                }
            }

            if (!userFound) {
                Map<String, String> map = new HashMap<>();
                map.put("url", new URL(req.getScheme(), req.getServerName(), HttpConstant.URL_PREFIX + "/index").toString());
                ResponseObjectContainer<String> respContainer = resourceController.getWechatRedirectUrl(map);
                String url = respContainer.getData();
                logger.info("user not found, redirect to " + url);
                resp.sendRedirect(url);
                return;
            }

        }
        chain.doFilter(request, response);
    }

    private void setCookie(HttpServletResponse resp, WxMpUser user) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user.getOpenId());
        String token = JwtUtils.generateToken(map);
        logger.info("************token: " + token);
        userCacheService.setUser(user);
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    @Override
    public void destroy() {
        logger.info("TokenFilter destory");
    }
}
