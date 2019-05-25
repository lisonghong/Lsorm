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
     * @param conn      Connection连接
     * @param Ps        也进行参数拼接的 PreparedStatement 对象
     * @param resultSet ResultSet对象查询得到的结果集
     * @param clazz     反射对象class
     * @return 查询成功对象，可能是List 可能是Object，根据调用方法而定
     */
    public Object doExecute(Connection conn, PreparedStatement Ps, ResultSet resultSet, Class clazz);
}

