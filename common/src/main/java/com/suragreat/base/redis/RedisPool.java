package com.suragreat.base.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.*;
import java.util.Map.Entry;

@Component
@ConditionalOnProperty(prefix = "spring.redis", name = "host")
public class RedisPool {
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    public Jedis getResource() {
        RedisConnection jedisConnection = jedisConnectionFactory.getConnection();
        return (Jedis) jedisConnection.getNativeConnection();
    }

    /**
     * 是否存在.
     *
     * @param key the key
     * @return true, if exist
     */
    public boolean exist(String key) {
        Jedis resource = getResource();
        try {
            return resource.exists(key);
        } finally {
            resource.close();
        }
    }

    /**
     * 设置key的值为value.
     *
     * @param key   the key
     * @param value the value
     * @return
     */
    public String set(String key, String value) {
        Jedis resource = getResource();
        try {
            return resource.set(key, value);
        } finally {
            resource.close();
        }
    }

    public String setnex(String key, String value, long mills) {
        Jedis resource = getResource();
        try {
            return resource.set(key, value, "nx", "px", mills);
        } finally {
            resource.close();
        }
    }

    /**
     * 设置key的值为value, 超时时间为seconds.
     *
     * @param key     the key
     * @param value   the value
     * @param seconds the seconds
     * @return
     */
    public String setex(String key, String value, int seconds) {
        Jedis resource = getResource();
        try {
            return resource.setex(key, seconds, value);
        } finally {
            resource.close();
        }
    }

    /**
     * SET类型赋值.
     *
     * @param key     the key
     * @param members the members
     * @return
     */
    public Long sadd(String key, String... members) {
        Jedis resource = getResource();
        try {
            return resource.sadd(key, members);
        } finally {
            resource.close();
        }
    }

    public Set<String> smembers(String key) {
        Jedis resource = getResource();
        try {
            return resource.smembers(key);
        } finally {
            resource.close();
        }
    }

    /**
     * 获取集合的成员数.
     *
     * @param key the key
     * @return the long
     */
    public Long scard(String key) {
        Jedis resource = getResource();
        try {
            return resource.scard(key);
        } finally {
            resource.close();
        }
    }

    /**
     * 验证成员member是否在key对应的缓存中存在.
     *
     * @param key    the key
     * @param member the member
     * @return true, if successful
     */
    public boolean sismember(String key, String member) {
        Jedis resource = getResource();
        try {
            return resource.sismember(key, member);
        } finally {
            resource.close();
        }
    }

    /**
     * 随机获取一个SET中的值并将该值从SET中移除.
     *
     * @param key the key
     * @return the string
     */
    public String spop(String key) {
        Jedis resource = getResource();
        try {
            return resource.spop(key);
        } finally {
            resource.close();
        }
    }

    /**
     * 随机获取n个SET中的值并将该值从SET中移除.
     *
     * @param key the key
     * @return the string
     */
    public Set<String> spop(String key, long count) {
        Jedis resource = getResource();
        try {
            return resource.spop(key, count);
        } finally {
            resource.close();
        }
    }

    /**
     * 取得缓存中key对应的值.
     *
     * @param key the key
     * @return the string
     */
    public String getString(String key) {
        Jedis resource = getResource();
        try {
            return resource.get(key);
        } finally {
            resource.close();
        }
    }

    /**
     * 删除缓存key.
     *
     * @param key the key
     * @return
     */
    public Long del(String key) {
        Jedis resource = getResource();
        try {
            return resource.del(key);
        } finally {
            resource.close();
        }
    }

    /**
     * Decr by.
     *
     * @param key the key
     * @param num the num
     * @return the long
     */
    public Long decrBy(String key, long num) {
        Jedis resource = getResource();
        try {
            return resource.decrBy(key, num);
        } finally {
            resource.close();
        }
    }

    /**
     * Incr by.
     *
     * @param key the key
     * @param num the num
     * @return the long
     */
    public Long incrBy(String key, long num) {
        Jedis resource = getResource();
        try {
            return resource.incrBy(key, num);
        } finally {
            resource.close();
        }
    }

    /**
     * 设置过期时间.
     *
     * @param key     the key
     * @param seconds the seconds
     * @return
     */
    public Long expire(String key, int seconds) {
        Jedis resource = getResource();
        try {
            return resource.expire(key, seconds);
        } finally {
            resource.close();
        }
    }

    /**
     * 设置HASH中的值.
     *
     * @param key   the key
     * @param field the field
     * @param value the value
     * @return
     */
    public Long hset(String key, String field, String value) {
        Jedis resource = getResource();
        try {
            return resource.hset(key, field, value);
        } finally {
            resource.close();
        }
    }

    /**
     * 设置HASH中的值和过期时间.
     *
     * @param key     the key
     * @param field   the field
     * @param value   the value
     * @param seconds the seconds
     */
    public void hset(String key, String field, String value, int seconds) {
        hset(key, field, value);
        expire(key, seconds);
    }

    /**
     * 设置HASH中的值和过期时间.
     *
     * @param key  the key
     * @param hash the hash
     * @return
     */
    public String hmset(String key, Map<String, String> hash) {
        Jedis resource = getResource();
        try {
            return resource.hmset(key, hash);
        } finally {
            resource.close();
        }
    }

    /**
     * 设置HASH中的值和过期时间.
     *
     * @param key     the key
     * @param hash    the hash
     * @param seconds the seconds
     */
    public void hmset(String key, Map<String, String> hash, int seconds) {
        hmset(key, hash);
        expire(key, seconds);
    }

    /**
     * 获取HASH中的值.
     *
     * @param key   the key
     * @param field the field
     * @return the string
     */
    public String hget(String key, String field) {
        Jedis resource = getResource();
        try {
            return resource.hget(key, field);
        } finally {
            resource.close();
        }
    }

    public Map<String, String> hgetAll(String key) {
        Jedis resource = getResource();
        try {
            return resource.hgetAll(key);
        } finally {
            resource.close();
        }
    }

    /**
     * 获取HASH中的值.
     *
     * @param key    the key
     * @param fields the fields
     * @return the list
     */
    public List<String> hmget(String key, String... fields) {
        Jedis resource = getResource();
        try {
            return resource.hmget(key, fields);
        } finally {
            resource.close();
        }
    }

    /**
     * 是否在HASH中存在.
     *
     * @param key   the key
     * @param field the field
     * @return the boolean
     */
    public Boolean hexist(String key, String field) {
        Jedis resource = getResource();
        try {
            return resource.hexists(key, field);
        } finally {
            resource.close();
        }
    }

    /**
     * 删除HASH中的键值.
     *
     * @param key    the key
     * @param fields the fields
     * @return
     */
    public Long hdel(String key, String... fields) {
        Jedis resource = getResource();
        try {
            return resource.hdel(key, fields);
        } finally {
            resource.close();
        }
    }

    public Long hincrBy(String key, String field, long num) {
        Jedis resource = getResource();
        try {
            return resource.hincrBy(key, field, num);
        } finally {
            resource.close();
        }
    }

    public Long hdecrBy(String key, String field, long num) {
        Jedis resource = getResource();
        try {
            return resource.hincrBy(key, field, -num);
        } finally {
            resource.close();
        }
    }

    public Set<String> hkeys(String key) {
        Jedis resource = getResource();
        try {
            return resource.hkeys(key);
        } finally {
            resource.close();
        }
    }

    public Long hlen(String key) {
        Jedis resource = getResource();
        try {
            return resource.hlen(key);
        } finally {
            resource.close();
        }
    }

    public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
        return hscan(key, cursor, 20);
    }

    public ScanResult<Entry<String, String>> hscan(String key, String cursor, int count) {
        Jedis resource = getResource();
        ScanParams sp = new ScanParams();
        sp.count(count);
        try {
            return resource.hscan(key, cursor, sp);
        } finally {
            resource.close();
        }
    }

    public Long zcard(String key) {
        Jedis resource = getResource();
        try {
            return resource.zcard(key);
        } finally {
            resource.close();
        }
    }

    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Jedis resource = getResource();
        try {
            return resource.zadd(key, scoreMembers);
        } finally {
            resource.close();
        }
    }

    public Long zadd(String key, double score, String member) {
        Jedis resource = getResource();
        try {
            return resource.zadd(key, score, member);
        } finally {
            resource.close();
        }
    }

    public Set<String> zrange(String key, long start, long end) {
        Jedis resource = getResource();
        try {
            return resource.zrange(key, start, end);
        } finally {
            resource.close();
        }
    }

    public Set<String> zrangeByScore(String key, String min, String max) {
        Jedis resource = getResource();
        try {
            return resource.zrangeByScore(key, min, max);
        } finally {
            resource.close();
        }
    }

    public Long lpush(String key, String... strings) {
        Jedis resource = getResource();
        try {
            return resource.lpush(key, strings);
        } finally {
            resource.close();
        }
    }

    public Long rpush(String key, String... strings) {
        Jedis resource = getResource();
        try {
            return resource.rpush(key, strings);
        } finally {
            resource.close();
        }
    }

    public Long llen(String key) {
        Jedis resource = getResource();
        try {
            return resource.llen(key);
        } finally {
            resource.close();
        }
    }

    public List<String> lrange(String key, long start, long end) {
        Jedis resource = getResource();
        try {
            return resource.lrange(key, start, end);
        } finally {
            resource.close();
        }
    }

    public String rpop(String key) {
        Jedis resource = getResource();
        try {
            return resource.rpop(key);
        } finally {
            resource.close();
        }
    }

    public String lpop(String key) {
        Jedis resource = getResource();
        try {
            return resource.lpop(key);
        } finally {
            resource.close();
        }
    }

    /**
     * Keys is disabled on product environment, use scan instead.
     *
     * @param keyPattern
     * @return
     */
    public Set<String> keys(String keyPattern) {
        Jedis resource = getResource();
        try {
            Set<String> keys = new HashSet<>();
            keys.addAll(getKeys(resource, keyPattern));
            return keys;
        } finally {
            resource.close();
        }
    }

    private Collection<? extends String> getKeys(Jedis j, String keyPattern) {
        String cursor = ScanParams.SCAN_POINTER_START;
        ScanParams params = new ScanParams();
        params.match(keyPattern).count(20);
        Set<String> data = new HashSet<>();
        do {
            ScanResult<String> result = j.scan(cursor, params);
            data.addAll(result.getResult());
            cursor = result.getStringCursor();
        } while (!"0".equals(cursor));
        return data;
    }
}
