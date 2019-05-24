package com.lsh.sorm.core;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import com.lsh.sorm.bean.Configuration;
import com.lsh.sorm.pool.DBConnPool;

/**
 * 根据配置信息，维持连接对象的管理（增加连接池功能）
 *
 * @author LSH
 */
public class DBManager {
    public static Configuration conf;
    public static DBConnPool dbConnPool = null;

    static {// 静态代码块只有在类初始话的时候执行，只执行一次
        Properties pros = new Properties();
        try {
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        conf = new Configuration();
        conf.setDriver(pros.getProperty("driver"));
        conf.setUrl(pros.getProperty("url"));
        conf.setUser(pros.getProperty("user"));
        conf.setPwd(pros.getProperty("pwd"));
        conf.setUsingDB(pros.getProperty("usingDB"));
        conf.setSrcPath(pros.getProperty("srcPath"));
        conf.setPoPackage(pros.getProperty("poPackage"));
        conf.setQueryClass(pros.getProperty("queryClass"));
        conf.setPoolMinSize(Integer.parseInt(pros.getProperty("poolMinSize")));
        conf.setPoolMaxSize(Integer.parseInt(pros.getProperty("poolMaxSize")));

        System.out.println(TableContext.class);//加载TableContext
    }

    /**
     * 创建新的连接对象
     *
     * @return 连接对象
     */
    public static Connection createConn() {
        try {
            Class.forName(conf.getDriver());
            return DriverManager.getConnection(conf.getUrl(), conf.getUser(), conf.getPwd());//直接建立连接，后期增加连接池处理提高效率
        } catch (SQLNonTransientConnectionException e) {
            System.out.println("超过数据库允许最大连接数！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过连接池获取对象
     *
     * @return 连接对象
     */
    public static Connection getConn() {
        if (dbConnPool == null) {
            dbConnPool = new DBConnPool();
        }
        return dbConnPool.getConnection();
    }
    /*
    public static Connection getConn() {
        try {
            Class.forName(conf.getDriver());
            return DriverManager.getConnection(conf.getUrl(), conf.getUser(), conf.getPwd());//直接建立连接，后期增加连接池处理提高效率
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/


    public static Configuration getConf() {
        return conf;
    }

    /**
     * 关闭相应的连接对象
     *
     * @param ps   PreparedStatement对象
     * @param conn Connection对象
     */
    public static void close(PreparedStatement ps, Connection conn) {
        close(ps);
        close(conn);
    }

    /**
     * 关闭相应的连接对象
     *
     * @param resultSet ResultSet对象
     * @param ps        PreparedStatement对象
     * @param conn      Connection对象
     */
    public static void close(ResultSet resultSet, PreparedStatement ps, Connection conn) {
        close(resultSet);
        close(ps);
        close(conn);
    }

    /**
     * 关闭相应的连接对象
     *
     * @param resultSet ResultSet对象
     * @param conn      Connection对象
     */
    public static void close(ResultSet resultSet, Connection conn) {
        close(resultSet);
        close(conn);
    }

    /**
     * 关闭相应的连接对象
     *
     * @param resultSet ResultSet对象
     * @param ps        PreparedStatement对象
     */
    public static void close(ResultSet resultSet, PreparedStatement ps) {
        close(resultSet);
        close(ps);

    }


    public static void close(Connection conn) {
//        if (conn != null) {
//            try {
//                conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
        dbConnPool.close(conn);
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
