package com.lsh.sorm.core;

import com.lsh.sorm.bean.Configuration;
import com.lsh.sorm.pool.DBConnPool;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 根据配置信息，维持连接对象的管理（增加连接池功能）
 *
 * @author LSH
 */
public class DBManager {
    public static final Configuration conf;

    static {// 静态代码块只有在类初始话的时候执行，只执行一次
        //加载配置文件
        Properties pros = new Properties();
        try {
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置配置文件值
        conf = new Configuration();

        conf.setUrl(pros.getProperty("url"));
        conf.setUser(pros.getProperty("user"));
        conf.setPwd(pros.getProperty("pwd"));
        conf.setUsingDB(pros.getProperty("usingDB"));

        String driver = pros.getProperty("driver");//获取配置的数据库驱动
        if (driver != null && !driver.equals("")) {
            conf.setDriver(driver);
        }

        String srcPath = pros.getProperty("srcPath");//获取配置的文件路径
        String poPackage = pros.getProperty("poPackage");//获取配置的po包路径
        if (srcPath != null && !srcPath.equals("")) {
            conf.setSrcPath(pros.getProperty("srcPath"));
        }
        if (poPackage != null && !poPackage.equals("")) {
            conf.setPoPackage(pros.getProperty("poPackage"));
        }


        String queryClass = pros.getProperty("queryClass");//获取配置的Query对象包路径 默认为 com.lsh.sorm.core.MySqlQuery
        if (queryClass != null && !queryClass.equals("")) {
            conf.setQueryClass(queryClass);
        }


        String poolMinSize = pros.getProperty("poolMinSize");//初始化最小连接数据
        String poolMaxSize = pros.getProperty("poolMaxSize");//最大空闲连接数据
        try {
            if (poolMinSize != null) {
                int i = Integer.parseInt(poolMinSize);
                conf.setPoolMinSize(i <= 0 ? 0 : i);
            }

        } catch (Exception e) {
            System.out.println("最小初始程序连接池配置参数有误、正确使用(poolMinSize=10最小初始程序池)！将使用默认配置(初始值为2、)");
        }

        try {
            if (poolMaxSize != null) {
                int i = Integer.parseInt(poolMaxSize);
                conf.setPoolMaxSize(i <= 0 ? 1 : i);
            }
        } catch (Exception e) {
            System.out.println("最大空闲程序连接池配置参数有误、正确使用(poolMaxSize=20最大空闲程序连接池)！将使用默认配置(空闲最大值为10、)");
        }
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
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过连接池获取连接对象
     *
     * @return 连接对象
     */
    public static Connection getConn() {
        return DBConnPool.dbConnPool.getConnection();
    }


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
        DBConnPool.dbConnPool.close(conn);
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
