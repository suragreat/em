package com.suragreat.biz.wechat.controller;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMediaImgUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * <pre>
 *  注意：此contorller 实现WxMpMenuService接口，仅是为了演示如何调用所有菜单相关操作接口，
 *      实际项目中无需这样，根据自己需要添加对应接口即可
 * </pre>
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@RestController
@RequestMapping("/wechat/misc")
public class WxMiscController {

    @Autowired
    private WxMpService wxService;


    @GetMapping("/uploadMediaImg")
    public WxMediaImgUploadResult uploadMediaImg(@RequestParam String file) throws WxErrorException {
        WxMediaImgUploadResult result = wxService.getMaterialService().mediaImgUpload(new File(file));
        return result;
    }

    @GetMapping("/uploadMediaFile")
    public WxMpMaterialUploadResult uploadMedia(@RequestParam String file) throws WxErrorException {
        WxMpMaterial material = new WxMpMaterial();
        material.setFile(new File(file));
        material.setName("name");
        WxMpMaterialUploadResult result = wxService.getMaterialService().materialFileUpload("image", material);
        return result;
    }
    @GetMapping("/uploadNews")
    public WxMpMaterialUploadResult uploadNews() throws WxErrorException {
        WxMpMaterialNews news = new WxMpMaterialNews();
        WxMpMaterialNews.WxMpMaterialNewsArticle art = new WxMpMaterialNews.WxMpMaterialNewsArticle();
        art.setAuthor("author");
        art.setContent("content");
        art.setTitle("title");
        art.setThumbMediaId("TYdo0V7vD25B7RRkyx7DuqjX8Iz5mvSiCX9wCAoSEIwORk17t74s8Pxv93CyDFzO");
        art.setShowCoverPic(true);
        art.setContentSourceUrl("http://101.132.148.33/");
        news.addArticle(art);
        WxMpMaterialUploadResult result = wxService.getMaterialService().materialNewsUpload(news);
        return result;
    }

    @GetMapping("/accessToken")
    public String accessToken() throws WxErrorException {
       return wxService.getAccessToken();
    }
}
