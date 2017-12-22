package com.suragreat.base.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtils {
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static PropertyNamingStrategy DEFAULT_PROPERTY_NAMING_STRATEGY;

    private static ThreadLocal<ObjectMapper> objMapperLocal = new ThreadLocal<ObjectMapper>() {
        @Override
        public ObjectMapper initialValue() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            DEFAULT_PROPERTY_NAMING_STRATEGY = mapper.getPropertyNamingStrategy();
            return mapper;
        }
    };

    public static String toJSON(Object value, PropertyNamingStrategy strategy) {
        boolean replace = false;
        try {
            if (strategy != null && strategy != DEFAULT_PROPERTY_NAMING_STRATEGY) {
                objMapperLocal.get().setPropertyNamingStrategy(strategy);
                replace = true;
            }
            return toJSON(value);
        } finally {
            if (replace) {
                objMapperLocal.get().setPropertyNamingStrategy(DEFAULT_PROPERTY_NAMING_STRATEGY);
            }
        }
    }

    public static String toJSON(Object value) {
        String result = null;
        try {
            result = objMapperLocal.get().writeValueAsString(value);
        } catch (Exception e) {
            logger.error("Failed toJSON", e);
        }
        // Fix null string
        if ("null".equals(result)) {
            result = null;
        }
        return result;
    }

    public static <T> T toT(String jsonString, Class<T> clazz, PropertyNamingStrategy strategy) {
        boolean replace = false;
        try {
            if (strategy != null && strategy != DEFAULT_PROPERTY_NAMING_STRATEGY) {
                objMapperLocal.get().setPropertyNamingStrategy(strategy);
                replace = true;
            }
            return toT(jsonString, clazz);
        } finally {
            if (replace) {
                objMapperLocal.get().setPropertyNamingStrategy(DEFAULT_PROPERTY_NAMING_STRATEGY);
            }
        }
    }

    public static <T> T toT(String jsonString, TypeReference<T> valueTypeRef, PropertyNamingStrategy strategy) {
        boolean replace = false;
        try {
            if (strategy != null && strategy != DEFAULT_PROPERTY_NAMING_STRATEGY) {
                objMapperLocal.get().setPropertyNamingStrategy(strategy);
                replace = true;
            }
            return toT(jsonString, valueTypeRef);
        } finally {
            if (replace) {
                objMapperLocal.get().setPropertyNamingStrategy(DEFAULT_PROPERTY_NAMING_STRATEGY);
            }
        }
    }

    public static <T> T toT(String jsonString, Class<T> clazz) {
        try {
            return objMapperLocal.get().readValue(jsonString, clazz);
        } catch (Exception e) {
            logger.error("Failed toT", e);
        }
        return null;
    }

    /**
     * List<Map<String,Integer>> list2=toT(json,new
     * TypeReference<List<Map<String,Integer>>>(){});
     *
     * @param jsonString
     * @param valueTypeRef
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toT(String jsonString, TypeReference<T> valueTypeRef) {
        try {
            return (T) objMapperLocal.get().readValue(jsonString, valueTypeRef);
        } catch (Exception e) {
            logger.error("Failed toT", e);
        }
        return null;
    }

    public static <T> List<T> toTList(String jsonString, Class<T> clazz, PropertyNamingStrategy strategy) {
        boolean replace = false;
        try {
            if (strategy != null && strategy != DEFAULT_PROPERTY_NAMING_STRATEGY) {
                objMapperLocal.get().setPropertyNamingStrategy(strategy);
                replace = true;
            }
            return toTList(jsonString, clazz);
        } finally {
            if (replace) {
                objMapperLocal.get().setPropertyNamingStrategy(DEFAULT_PROPERTY_NAMING_STRATEGY);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toTList(String jsonString, Class<T> clazz) {
        try {
            ObjectMapper mapper = objMapperLocal.get();
            return (List<T>) mapper.readValue(jsonString,
                    mapper.getTypeFactory().constructParametricType(List.class, clazz));
        } catch (Exception e) {
            logger.error("Failed toTList", e);
        }
        return null;
    }

    public static <T> Set<T> toTSet(String jsonString, Class<T> clazz, PropertyNamingStrategy strategy) {
        boolean replace = false;
        try {
            if (strategy != null && strategy != DEFAULT_PROPERTY_NAMING_STRATEGY) {
                objMapperLocal.get().setPropertyNamingStrategy(strategy);
                replace = true;
            }
            return toTSet(jsonString, clazz);
        } finally {
            if (replace) {
                objMapperLocal.get().setPropertyNamingStrategy(DEFAULT_PROPERTY_NAMING_STRATEGY);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> toTSet(String jsonString, Class<T> clazz) {
        try {
            ObjectMapper mapper = objMapperLocal.get();
            return (Set<T>) mapper.readValue(jsonString,
                    mapper.getTypeFactory().constructParametricType(Set.class, clazz));
        } catch (Exception e) {
            logger.error("Failed toTSet", e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String jsonString) {
        return toT(jsonString, Map.class);
    }

    /**
     * Description: 将一个 JavaBean 对象转化为一个  Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> convertBeanToMap(Object bean) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (bean == null) {
            return returnMap;
        }
        if (bean instanceof Map) {
            return (Map<String, Object>) bean;
        }
        Class type = bean.getClass();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        if (result instanceof String && StringUtils.isEmpty(result.toString())) {
                            continue;
                        }
                        returnMap.put(propertyName, result);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("将 JavaBean 转化为 Map失败！");
        }
        return returnMap;
    }
}
