package com.suragreat.biz.game.model;

import com.suragreat.base.model.SerializableObject;

public class Swiper<T> extends SerializableObject{
    private T url;
    private String img;
    private String title;

    public T getUrl() {
        return url;
    }

    public void setUrl(T url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
