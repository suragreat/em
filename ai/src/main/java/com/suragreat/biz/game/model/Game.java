package com.suragreat.biz.game.model;

import com.suragreat.base.model.SerializableObject;
import org.hibernate.validator.constraints.NotBlank;

import java.sql.Timestamp;

public class Game extends SerializableObject {
    private String id;
    @NotBlank(message = "名称不能为空")
    private String name;
    private String creator;
    private Timestamp createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
