package com.suragreat.biz.game.service;

import com.suragreat.biz.game.model.Swiper;
import com.suragreat.util.cache.RedisSetCache;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SwiperCacheStore extends RedisSetCache {
    private final static String CACHE_NAME = "resource";
    private final static String[] SWIPER_IMGS = {
            "https://s1.ax1x.com/2017/12/13/qdedU.jpg",
            "https://s1.ax1x.com/2017/12/13/qdKJJ.jpg",
            "https://s1.ax1x.com/2017/12/13/qdZZT.jpg"
    };

    public Set<Swiper> getSwipers() {
        Set<Swiper> result = getAll(Swiper.class);
        if (result == null) {
            result = new HashSet<>();
        }
        if (result.size() == 0) {
            for (String img : SWIPER_IMGS) {
                Swiper<String> s = new Swiper<>();
                s.setImg(img);
                s.setUrl("javascript:");
                result.add(s);
            }
        }
        return result;
    }

    public void setSwipers(Set<Swiper<?>> swipers) {
        if (swipers != null) {
            swipers.forEach(s -> {
                put(s);
            });
        }
    }

    @Override
    protected String name() {
        return CACHE_NAME;
    }
}
