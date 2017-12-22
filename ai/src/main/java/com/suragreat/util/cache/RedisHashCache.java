package com.suragreat.util.cache;

import com.suragreat.base.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class RedisHashCache extends RedisCache {
    protected <T> void put(String field, T val) {
        put(name(), field, val);
    }

    protected <T> void put(String key, String field, T val) {
        if (val != null) {
            String value;
            if (String.class.isAssignableFrom(val.getClass())) {
                value = (String) val;
            } else if (val.getClass().isEnum()) {
                value = ((Enum) val).name();
            } else {
                value = JsonUtils.toJSON(val);
            }
            pool.hset(key, field, value);
        }
    }

    protected <T> T get(String field, Class<T> clz) {
        return get(name(), field, clz);
    }

    protected <T> T get(String key, String field, Class<T> clz) {
        String val = pool.hget(key, field);
        if (StringUtils.isNoneBlank(val) && clz != null) {
            if (String.class.isAssignableFrom(clz)) {
                return (T) val;
            } else if (clz.isEnum()) {
                T[] data = clz.getEnumConstants();
                for (T t : data) {
                    Enum enm = (Enum) t;
                    if (val.equals(enm.name())) {
                        return t;
                    }
                }
            } else {
                return (T) JsonUtils.toT(val, clz);
            }

        }
        return null;
    }

    protected <T> List<T> getAll(Class<T> clz) {
        Map<String, String> map = pool.hgetAll(name());
        List<T> result = null;
        if (map != null) {
            result = new ArrayList<>();
            for (String s : map.values()) {
                result.add(JsonUtils.toT(s, clz));
            }
        }
        return result;
    }

    protected Long remove(String key, String field) {
        return pool.hdel(key, field);
    }

    protected Long remove(String field) {
        return remove(name(), field);
    }

    protected Set<String> getKeys(String key) {
        return pool.hkeys(key);
    }

    protected Set<String> getKeys() {
        return getKeys(name());
    }

}
