package com.suragreat.em.biz.consumer.controller;


import com.suragreat.base.service.annotation.Lock;
import com.suragreat.em.biz.address.model.Area;
import com.suragreat.base.controller.BaseController;
import com.suragreat.base.model.ResponseListContainer;
import com.suragreat.em.biz.address.service.AreaCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1"}, produces = {"application/json"})
public class AddressController extends BaseController {
    @Autowired
    private AreaCacheService areaCacheService;

    @RequestMapping(value = {"/address/province"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    @Lock("abc")
    public ResponseListContainer<Area> getAllProvinces() {
        return successList(areaCacheService.getAllProvinces());
    }

    @RequestMapping(value = {"/address/province/{id}"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResponseListContainer<Area> getAllCitysOfProvince(@PathVariable String id) {
        return successList(areaCacheService.getAllCitysOfProvince(id));
    }

    @RequestMapping(value = {"/address/city/{id}"}, method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public ResponseListContainer<Area> getAllAreasOfCity(@PathVariable String id) {
        return successList(areaCacheService.getAllAreasOfCity(id));
    }
}
