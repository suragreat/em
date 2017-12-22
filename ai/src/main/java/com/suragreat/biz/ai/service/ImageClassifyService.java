package com.suragreat.biz.ai.service;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.aip.nlp.AipNlp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.suragreat.base.constant.ServerErrorEnum;
import com.suragreat.base.util.JsonUtils;
import com.suragreat.biz.ai.model.AipContainer;
import com.suragreat.biz.ai.model.Dish;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

@Service
public class ImageClassifyService {
    public static final String NO_DISH = "哎呦，恕我眼拙，这个菜没见过啊";
    public static final String ONE_DISH_HIGH_CALORIE = "吃得太好啦，%s有%s大卡呢";
    public static final String ONE_DISH_NORMAL = "您的饮食太健康啦，%s含有%s卡路里，正好补充能量";
    public static final String TWO_DISH = "您吃的大餐我有点看不准，是%s(%s卡路里)还是%s(%s卡路里)?";
    private static Logger logger = LoggerFactory.getLogger(ImageClassifyService.class);
    @Autowired
    private AipImageClassify imageClassify;
    @Autowired
    private AipNlp aipNlp;

    public String dishDetect(String url) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("top_num", "2");
        byte[] file = readImage(url);
        JSONObject res = imageClassify.dishDetect(file, options);
        AipContainer<Dish> dishContainer = JsonUtils.toT(res.toString(), new TypeReference<AipContainer<Dish>>() {
        }, PropertyNamingStrategy.SNAKE_CASE);
        boolean hasCalorie = false;
        String result = null;
        if (dishContainer.getResultNum() == 1) {
            Dish d = dishContainer.getResult().get(0);
            hasCalorie = hasCalorie(d);
            if (hasCalorie) {
                result = getDishDescription(d);
            }
        } else if (dishContainer.getResultNum() == 2) {
            Dish d1 = dishContainer.getResult().get(0);
            Dish d2 = dishContainer.getResult().get(1);
            boolean d1HasCalorie = hasCalorie(d1);
            boolean d2HasCalorie = hasCalorie(d2);
            hasCalorie = d1HasCalorie || d2HasCalorie;
            if (d1HasCalorie) {
                if (d2HasCalorie) {
                    if (Double.valueOf(d1.getProbability()) > 4 * Double.valueOf(d2.getProbability()) || wordSim(d1.getName(), d2.getName())) {
                        result = getDishDescription(d1);
                    } else {
                        result = String.format(TWO_DISH, d1.getName(), d1.getCalorie(), d2.getName(), d2.getCalorie());
                    }
                } else {
                    result = getDishDescription(d1);
                }
            } else {
                if (d2HasCalorie) {
                    result = getDishDescription(d2);
                }
            }
        }
        if (!hasCalorie) {
            result = NO_DISH;
        }

        return result;
    }

    private boolean wordSim(String name1, String name2) {
        //try {
        //    JSONObject response = aipNlp.wordSimEmbedding(name1, name2);
        //} catch (Exception e) {
        //}
        return false;
    }

    private boolean hasCalorie(Dish d) {
        return d.isHasCalorie() && !"0".equals(d.getCalorie());
    }

    private String getDishDescription(Dish d) {
        String result;
        if (Double.valueOf(d.getCalorie()) >= 150) {
            result = String.format(ONE_DISH_HIGH_CALORIE, d.getName(), d.getCalorie());
        } else {
            result = String.format(ONE_DISH_NORMAL, d.getName(), d.getCalorie());
        }
        return result;
    }

    private byte[] readImage(String url) {
        try {
            return IOUtils.toByteArray(new URL(url));
        } catch (IOException e) {
            logger.error("Failed to read image from url", e);
            ServerErrorEnum.BACKEND_ERROR.throwsException();
            return null;
        }
    }
}
