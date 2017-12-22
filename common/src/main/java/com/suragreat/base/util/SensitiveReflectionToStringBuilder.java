package com.suragreat.base.util;

import java.lang.reflect.Field;

import com.suragreat.base.annotation.IgnoreSensitive;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.suragreat.base.annotation.MaskSensitive;

public class SensitiveReflectionToStringBuilder extends ReflectionToStringBuilder {

    public <T> SensitiveReflectionToStringBuilder(final T object, final ToStringStyle style, final StringBuffer buffer,
            final Class<? super T> reflectUpToClass, final boolean outputTransients, final boolean outputStatics) {
        super(object, style, buffer, reflectUpToClass, outputTransients, outputStatics);
    }

    @Override
    protected Object getValue(Field field) throws IllegalArgumentException, IllegalAccessException {
        Object value = field.get(this.getObject());
        do {
            if (value == null)
                break;
            IgnoreSensitive ignore = field.getAnnotation(IgnoreSensitive.class);
            if (ignore != null) {
                value = "<ignored>";
                break;
            }

            MaskSensitive ann = field.getAnnotation(MaskSensitive.class);
            if (ann == null)
                break;
            value = getMD5MaskStr(value.toString());
        } while (false);
        return value;
    }

    private static String getMD5MaskStr(String str) {
        String salt = "abcdefg";
        return CryptUtil.md5(str, salt);
    }

    public static String reflectionToString(Object object, ToStringStyle style) {
        return SensitiveReflectionToStringBuilder.toString(object, style);
    }

    public static String toString(Object object, ToStringStyle style) {
        return SensitiveReflectionToStringBuilder.toString(object, style, false, false, null);
    }

    public static <T> String toString(final T object, final ToStringStyle style, final boolean outputTransients,
            final boolean outputStatics, final Class<? super T> reflectUpToClass) {
        return new SensitiveReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients,
                outputStatics).toString();
    }
}
