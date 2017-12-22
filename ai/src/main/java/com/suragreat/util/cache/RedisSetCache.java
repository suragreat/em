package com.suragreat.util.cache;

import com.suragreat.base.util.JsonUtils;

import java.util.HashSet;
import java.util.Set;

public abstract class RedisSetCache extends RedisCache {
    protected <T> void put(T val) {
        if (val != null) {
            pool.sadd(name(), JsonUtils.toJSON(val));
        }
    }

    protected <T> Set<T> getAll(Class<T> clz) {
        Set<String> set = pool.smembers(name());
        Set<T> result = null;
        if (set != null) {
            result = new HashSet<>();
            for (String s : set) {
                result.add(JsonUtils.toT(s, clz));
            }
        }
        return result;
    }

}
