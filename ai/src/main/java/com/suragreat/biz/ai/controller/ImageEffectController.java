package com.suragreat.biz.ai.controller;


import com.suragreat.base.controller.BaseController;
import com.suragreat.base.model.ResponseObjectContainer;
import com.suragreat.biz.ai.service.ImageEffectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1"}, produces = {"application/json"})
public class ImageEffectController extends BaseController {
    @Autowired
    private ImageEffectService imageEffectService;

    @RequestMapping(value = {"/face_merge"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResponseObjectContainer<String> dishDetect(@RequestParam String url, @RequestParam int id) {
        return success(imageEffectService.faceMergeUrl(id,url));
    }


}
