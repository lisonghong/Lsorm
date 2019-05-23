package com.lsh.sorm.pool;

import com.lsh.sorm.core.DBManager;

import java.security.PublicKey;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 连接池类
 */
public class DBConnPool {

    private List<Connection> pool;//连接池对象
    private static final int POOL_MAX_SIZE = DBManager.getConf().getPoolMaxSize();//最大连接数
    private static final int POOL_MIM_SIZE = DBManager.getConf().getPoolMinSize();//最小连接数



    public DBConnPool() {
        initPool();
    }

    /**
     * 连接池初始化
     */
    public void initPool() {
        if (pool == null) {
            pool = new ArrayList<Connection>();
        }
        while (pool.size() < POOL_MIM_SIZE) {
            pool.add(DBManager.createConn());//创建连接对象，并放入连接池
            System.out.println("初始化连接池数量" + pool.size());
        }
    }

    /**
     * 获取连接池对象
     *
     * @return Connection连接对象
     */
    public synchronized Connection getConnection() {
        int last_index = pool.size() - 1;
        Connection Conn = pool.get(last_index);//获取最后一个连接
        pool.remove(last_index);//删除最后一个连接
        return Conn;
    }

    /**
     * 将连接对象，放回连接池（如果连接池对象超过最大连接数，则关闭连接）
     *
     * @param conn Connection连接对象
     */
    public synchronized void close(Connection conn) {
        if (pool.size() >= POOL_MAX_SIZE) {
            //也达到最大连接数，关闭数据库连接
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            //存入连接池
            pool.add(conn);
        }

    }


}
