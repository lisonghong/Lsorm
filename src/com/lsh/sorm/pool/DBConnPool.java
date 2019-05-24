package com.lsh.sorm.pool;

import com.lsh.sorm.core.DBManager;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 连接池类
 */
public class DBConnPool {
    private static final int POOL_MAX_SIZE = DBManager.getConf().getPoolMaxSize();//最大连接数
    private static final int POOL_MIM_SIZE = DBManager.getConf().getPoolMinSize();//最小连接数
    public List<Connection> pool;//连接池对象

    private LinkedBlockingQueue<Thread> arrayBlockingQueue = new LinkedBlockingQueue(POOL_MAX_SIZE);//创建队列 100个队列


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
            Connection conn = DBManager.createConn();
            if (conn != null) {
                pool.add(conn);//创建连接对象，并放入连接池
                System.out.println("初始化连接池数量" + pool.size());
            }

        }
    }

    /**
     * 获取连接池对象
     *
     * @return Connection连接对象
     */
    public synchronized Connection getConnection() {

        if (pool.size() == 0) {
            Connection conn = DBManager.createConn();
            if (conn != null) {
                System.out.println("大小" + pool.size());
                return conn;
            }
            synchronized (Thread.currentThread()) {
                try {
                    arrayBlockingQueue.put(Thread.currentThread());//添加队列 开始排队
                    Thread.currentThread().wait();//阻塞
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (pool.size() > 0) {
            int last_index = pool.size() - 1;
            Connection Conn = pool.get(last_index);//获取最后一个连接
            pool.remove(last_index);//删除最后一个连接
            return Conn;
        } else {
            return getConnection();
        }
    }

    /**
     * 将连接对象，放回连接池（如果连接池对象超过最大连接数，则关闭连接）
     *
     * @param conn Connection连接对象
     */
    public void close(Connection conn) {
        if (pool.size() >= POOL_MAX_SIZE) {
            System.out.println("关闭oooo");
            //也达到最大连接数，关闭数据库连接
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {   //存入连接池
            pool.add(conn);
        }
        Thread poll = arrayBlockingQueue.poll();
        if (poll != null) {  //判断是否有排队对象
            synchronized (poll) {
                poll.notifyAll();
            }
        }
    }

}
