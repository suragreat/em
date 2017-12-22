package com.suragreat.base.service.aop;

import com.suragreat.base.constant.ServerErrorEnum;
import com.suragreat.base.redis.RedisPool;
import com.suragreat.base.service.annotation.Lock;
import com.suragreat.base.service.annotation.Lock.LockFailAction;
import com.suragreat.base.service.annotation.LockKey;
import com.suragreat.base.util.AppUtil;
import com.suragreat.base.util.LogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Aspect
@Component
public class LockAspect {
    private static final String SEPARATOR = ".";

    private static final String OK = "OK";

    @Autowired(required = false)
    private RedisPool pool;

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(LockAspect.class);

    @Around("execution(* com.suragreat..*(..)) && @annotation(com.suragreat.base.service.annotation.Lock)")
    public Object lock(ProceedingJoinPoint pjp) throws Throwable {
        if (pool != null) {
            return processWithRedis(pjp);
        } else {
            return processWithReentrantLock(pjp);
        }
    }

    private Object processWithReentrantLock(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = null;
        ReentrantLock lock = new ReentrantLock();
        Lock lockInfo = getLockInfo(pjp);
        lock.tryLock(lockInfo.maxWaitMills(), TimeUnit.MILLISECONDS);
        try {
            obj = pjp.proceed();
        } finally {
            lock.unlock();
        }
        return obj;
    }

    private Object processWithRedis(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = null;
        Lock lockInfo = getLockInfo(pjp);
        String key = lockInfo.value();
        key = getLockKey(pjp, key);
        String value = UUID.randomUUID().toString();
        boolean locked = false;
        long startMills = System.currentTimeMillis();
        long expMills = lockInfo.keepMills();
        long stopMills = startMills + lockInfo.maxWaitMills();
        while (!locked) {
            locked = lock(key, value, expMills);
            if (locked) {
                try {
                    obj = pjp.proceed();
                } finally {
                    unlock(key, value);
                }
            } else {
                if (needRetry(lockInfo)) {
                    if (System.currentTimeMillis() < stopMills) {
                        sleep(lockInfo);
                    } else {
                        ServerErrorEnum.GET_LOCK_FAILED.throwsException();
                    }
                } else {
                    ServerErrorEnum.GET_LOCK_FAILED.throwsException();
                }
            }
        }
        return obj;
    }

    private String getLockKey(ProceedingJoinPoint pjp, String key) {
        Object[] args = pjp.getArgs();
        String prefix = AppUtil.getAppCode();
        if (prefix == null) {
            prefix = "";
        }
        StringBuilder result = new StringBuilder(prefix);
        result.append(SEPARATOR).append(key);
        if (args != null && args.length > 0) {
            SortedMap<Integer, String> keys = getLockKeys(pjp, args);

            if (keys.size() > 0) {
                for (String k : keys.values()) {
                    result.append(SEPARATOR).append(k);
                }
            }
        }
        return result.toString();
    }

    private SortedMap<Integer, String> getLockKeys(ProceedingJoinPoint pjp, Object[] args) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Annotation[][] paramAnnotationArrays = methodSignature.getMethod().getParameterAnnotations();

        SortedMap<Integer, String> keys = new TreeMap<>();
        for (int ix = 0; ix < paramAnnotationArrays.length; ix++) {
            LockKey lockKey = getAnnotation(LockKey.class, paramAnnotationArrays[ix]);
            if (lockKey != null) {
                Object arg = args[ix];
                if (arg != null) {
                    keys.put(lockKey.value(), arg.toString());
                }
            }
        }
        return keys;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Annotation> T getAnnotation(final Class<T> annotationClass,
                                                          final Annotation[] annotations) {
        if (annotations != null && annotations.length > 0) {
            for (final Annotation annotation : annotations) {
                if (annotationClass.equals(annotation.annotationType())) {
                    return (T) annotation;
                }
            }
        }

        return null;
    }

    private void sleep(Lock lockInfo) {
        try {
            Thread.sleep(lockInfo.sleepMills());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void unlock(String key, String value) {
        Jedis jedis = pool.getResource();
        try {
            jedis.watch(key);
            String val = jedis.get(key);
            if (value.equals(val)) {
                Transaction transaction = jedis.multi();
                transaction.del(key);
                transaction.exec();
            }
            jedis.unwatch();
        } catch (Exception e) {
            logger.error("解锁失败, key = {}", key);
        } finally {
            jedis.close();
        }
    }

    private boolean lock(String key, String value, long expMills) {
        boolean locked;
        String val = pool.setnex(key, value, expMills);
        locked = OK.equalsIgnoreCase(val);
        return locked;
    }

    private boolean needRetry(Lock lockInfo) {
        return LockFailAction.RETRY.equals(lockInfo.action());
    }

    private Lock getLockInfo(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(Lock.class);
    }
}
