package com.suragreat.em.biz.address.model;

import com.suragreat.base.model.SerializableObject;
import org.apache.commons.lang3.StringUtils;

public class Area extends SerializableObject {
    private String id;
    private String province;
    private String city;
    private String area;
    private String areaCode;
    private String zipCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (StringUtils.isBlank(id)){
            id = null;
        }
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        if (StringUtils.isBlank(province)) {
            province = null;
        }
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (StringUtils.isBlank(city)) {
            city = null;
        }
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        if (StringUtils.isBlank(area)) {
            area = null;
        }
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        if (StringUtils.isBlank(areaCode)) {
            areaCode = null;
        }
        this.areaCode = areaCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        if (StringUtils.isBlank(zipCode)) {
            zipCode = null;
        }
        this.zipCode = zipCode;
    }
}