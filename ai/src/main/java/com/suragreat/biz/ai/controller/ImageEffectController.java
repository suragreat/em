package com.suragreat.biz.ai.controller;


import com.suragreat.base.constant.ServerErrorEnum;
import com.suragreat.base.controller.BaseController;
import com.suragreat.base.model.ResponseListContainer;
import com.suragreat.base.model.ResponseObjectContainer;
import com.suragreat.biz.ai.model.Template;
import com.suragreat.biz.ai.sdk.tencent.util.ImageUtil;
import com.suragreat.biz.ai.service.ImageEffectService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

@RestController
@RequestMapping(value = {"/page/face"}, produces = {"application/json"})
public class ImageEffectController extends BaseController {
    @Autowired
    private ImageEffectService imageEffectService;
    @Autowired
    private WxMpService wxMpService;

    @RequestMapping(value = {"/merge"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResponseObjectContainer<String> faceMerge(@RequestParam String url, @RequestParam int id) {
        return success(imageEffectService.faceMergeUrl(id, url));
    }

    @RequestMapping(value = {"/template/{id}/merge"}, method = {RequestMethod.POST})
    public ResponseObjectContainer<String> faceMerge(@PathVariable int id, @RequestBody Map<String, String> params) throws Exception {
        String sid = params.get("sid");
        if (StringUtils.isBlank(sid)) {
            ServerErrorEnum.ILLEGAL_PARAMETER.throwsException("url");
        }
        File file = wxMpService.getMaterialService().mediaDownload(sid);
        String data = ImageUtil.toBase64(new FileInputStream(file));
        return success(imageEffectService.faceMergeRaw(id, data));
    }

    @RequestMapping(value = {"/template"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResponseListContainer<Template> getTemplates() {
        return successList(imageEffectService.getTemplates());
    }


}
