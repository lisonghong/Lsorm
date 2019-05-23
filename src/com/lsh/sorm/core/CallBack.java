package com.lsh.sorm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * CallBack  查询回调类
 */
public interface CallBack {

    /**
     * 查询回调方法
     *
     * @param conn
     * @param Ps
     * @param resultSet
     * @return
     */
    public Object doExecute(Connection conn, PreparedStatement Ps, ResultSet resultSet,Class clazz);
}

