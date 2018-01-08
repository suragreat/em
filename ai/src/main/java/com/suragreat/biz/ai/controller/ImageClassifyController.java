package com.suragreat.biz.ai.controller;


import com.suragreat.base.controller.BaseController;
import com.suragreat.base.model.ResponseObjectContainer;
import com.suragreat.biz.ai.sdk.tencent.util.ImageUtil;
import com.suragreat.biz.ai.service.ImageClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = {"/v1"}, produces = {"application/json"})
public class ImageClassifyController extends BaseController {
    @Autowired
    private ImageClassifyService imageClassifyService;

    @RequestMapping(value = {"/dish"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResponseObjectContainer<String> dishDetect(@RequestParam String url) {
        return success(imageClassifyService.dishDetect(url));
    }

    @RequestMapping(value = {"/ocr_word"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResponseObjectContainer<String> ocrWord(@RequestParam String url) throws IOException {
        return success(imageClassifyService.ocrWord(ImageUtil.toByteArray(url)));
    }

}
