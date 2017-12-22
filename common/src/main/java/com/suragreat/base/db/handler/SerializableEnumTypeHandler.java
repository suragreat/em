package com.suragreat.base.db.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.suragreat.base.constant.SerializableEnum;

public class SerializableEnumTypeHandler extends BaseTypeHandler<SerializableEnum> {

    private Class<SerializableEnum> type;

    public SerializableEnumTypeHandler() {
    }

    public SerializableEnumTypeHandler(Class<SerializableEnum> type) {
        if (type == null)
            throw new IllegalArgumentException("Type argument cannot be null");
        this.type = type;
    }

    private SerializableEnum convert(String status) {
        if (status != null) {
            SerializableEnum[] objs = type.getEnumConstants();
            for (SerializableEnum em : objs) {
                if (status.equals(em.getCode())) {
                    return em;
                }
            }
        }
        return null;
    }

    @Override
    public SerializableEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convert(rs.getString(columnName));
    }

    @Override
    public SerializableEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convert(rs.getString(columnIndex));
    }

    @Override
    public SerializableEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convert(cs.getString(columnIndex));
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, SerializableEnum enumObj, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, enumObj.getCode());

    }

}
