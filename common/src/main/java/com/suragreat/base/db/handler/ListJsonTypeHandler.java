package com.suragreat.base.db.handler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.suragreat.base.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ListJsonTypeHandler<E> extends BaseTypeHandler<List<E>> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<E> set, JdbcType jdbcType)
            throws SQLException {
        String data = JsonUtils.toJSON(set);
        preparedStatement.setString(i, data);
    }

    @Override
    public List<E> getNullableResult(ResultSet rs, String s) throws SQLException {
        return convert(rs.getString(s));
    }

    @Override
    public List<E> getNullableResult(ResultSet rs, int i) throws SQLException {
        return convert(rs.getString(i));
    }

    @Override
    public List<E> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return convert(callableStatement.getString(i));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<E> convert(String string) {
        if (StringUtils.isEmpty(string)) {
            return Collections.EMPTY_LIST;
        }
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return JsonUtils.toTList(string, (Class) params[0]);
    }

}
