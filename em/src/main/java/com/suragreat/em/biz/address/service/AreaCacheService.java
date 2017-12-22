package com.suragreat.em.biz.address.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.suragreat.em.biz.address.model.Area;
import com.suragreat.em.biz.csv.AreaReader;
import com.suragreat.base.constant.ServerErrorEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = true)
@Cacheable(value = AreaCacheService.CACHE_KEY, keyGenerator = "keyGenerator")
public class AreaCacheService {
    public static final String CACHE_KEY = "AreaCacheService";
    @Autowired
    private AreaReader reader;
    private List<Area> areaList = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(AreaCacheService.class);

    public AreaCacheService() {
    }

    @PostConstruct
    public void init() {
        try {
            MappingIterator<Area> data = reader.read();
            data.forEachRemaining(area -> {
                areaList.add(area);
            });
        } catch (IOException e) {
            logger.error("failed to load area from csv", e);
        }
    }

    public Area[] getAllProvinces() {
        return areaList.parallelStream().filter(area -> StringUtils.isBlank(area.getCity())).toArray(Area[]::new);
    }

    public Area[] getAllCitysOfProvince(String id) {
        return areaList.parallelStream().filter(area -> !StringUtils.isBlank(area.getCity()) && area.getId().substring(0, 2).equals(id.substring(0, 2)) && StringUtils.isBlank(area.getArea())).toArray(Area[]::new);
    }

    public Area[] getAllAreasOfCity(String id) {
        Area city = findCityById(id);
        return areaList.parallelStream().filter(area -> !StringUtils.isBlank(area.getAreaCode()) && !StringUtils.isBlank(area.getArea()) && area.getAreaCode().equals(city.getAreaCode())).toArray(Area[]::new);
    }

    private Area findCityById(String id) {
        Optional<Area> opt = areaList.parallelStream().filter(area -> area.getId().equals(id) && !StringUtils.isBlank(area.getAreaCode())).findFirst();
        if (!opt.isPresent()) {
            ServerErrorEnum.INVALID_AREA_ID.throwsException(id);
        }
        return opt.get();
    }
}
