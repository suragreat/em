package com.suragreat.biz.wechat.config;

import com.suragreat.base.db.util.DecryptDataSourceTool;
import com.suragreat.biz.wechat.handler.*;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static me.chanjar.weixin.common.api.WxConsts.*;

/**
 * wechat mp configuration
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Configuration
@ConditionalOnClass(WxMpService.class)
@ConfigurationProperties(prefix = "wechat.mp")
public class WechatMpConfiguration {
    private final static String KEY_TXT = "n_N6,uo!tZnWbGg@";

    private String appId;

    private String secret;

    private String token;


    private String aesKey;

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = DecryptDataSourceTool.decryptAES(appId, KEY_TXT);
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = DecryptDataSourceTool.decryptAES(secret, KEY_TXT);
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = DecryptDataSourceTool.decryptAES(token, KEY_TXT);
    }

    public String getAesKey() {
        return this.aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = DecryptDataSourceTool.decryptAES(aesKey, KEY_TXT);
    }

    @Autowired
    protected LogHandler logHandler;
    @Autowired
    protected NullHandler nullHandler;
    @Autowired
    protected KfSessionHandler kfSessionHandler;
    @Autowired
    protected StoreCheckNotifyHandler storeCheckNotifyHandler;
    @Autowired
    private LocationHandler locationHandler;
    @Autowired
    private MenuHandler menuHandler;
    @Autowired
    private MsgHandler msgHandler;
    @Autowired
    private UnsubscribeHandler unsubscribeHandler;
    @Autowired
    private SubscribeHandler subscribeHandler;
    @Autowired
    private ImageHandler imageHandler;

    @Bean
    @ConditionalOnMissingBean
    public WxMpConfigStorage configStorage() {
        WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
        configStorage.setAppId(getAppId());
        configStorage.setSecret(getSecret());
        configStorage.setToken(getToken());
        configStorage.setAesKey(getAesKey());
        return configStorage;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMpService wxMpService(WxMpConfigStorage configStorage) {
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.okhttp.WxMpServiceImpl();
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.jodd.WxMpServiceImpl();
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.apache.WxMpServiceImpl();
        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage);
        return wxMpService;
    }

    @Bean
    public WxMpMessageRouter router(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        //newRouter.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION)
                .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION)
                .handler(this.kfSessionHandler)
                .end();
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION)
                .handler(this.kfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(WxMpEventConstants.POI_CHECK_NOTIFY)
                .handler(this.storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(MenuButtonType.CLICK).handler(this.getMenuHandler()).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(MenuButtonType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.SUBSCRIBE).handler(this.getSubscribeHandler())
                .end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.UNSUBSCRIBE)
                .handler(this.getUnsubscribeHandler()).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.LOCATION).handler(this.getLocationHandler())
                .end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION)
                .handler(this.getLocationHandler()).end();

        // 接收图片消息
        newRouter.rule().async(false).msgType(XmlMsgType.IMAGE).handler(this.getImageHandler()).end();
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.PIC_PHOTO_OR_ALBUM).handler(this.getImageHandler()).end();
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.PIC_WEIXIN).handler(this.getImageHandler()).end();
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.PIC_SYSPHOTO).handler(this.getImageHandler()).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
                .event(EventType.SCAN).handler(this.getScanHandler()).end();

        // 默认
        newRouter.rule().async(false).handler(this.getMsgHandler()).end();

        return newRouter;
    }

    protected MenuHandler getMenuHandler() {
        return this.menuHandler;
    }

    protected SubscribeHandler getSubscribeHandler() {
        return this.subscribeHandler;
    }

    protected UnsubscribeHandler getUnsubscribeHandler() {
        return this.unsubscribeHandler;
    }

    protected AbstractHandler getLocationHandler() {
        return this.locationHandler;
    }

    protected MsgHandler getMsgHandler() {
        return this.msgHandler;
    }

    protected AbstractHandler getScanHandler() {
        return null;
    }

    protected ImageHandler getImageHandler() {
        return imageHandler;
    }

}
