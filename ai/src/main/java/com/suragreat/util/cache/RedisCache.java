package com.suragreat.util.cache;

import com.suragreat.base.redis.RedisPool;
import com.suragreat.base.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RedisCache {
    @Autowired
    protected RedisPool pool;

    protected abstract String name();
}
