package com.lsh.sorm.pool;

import com.lsh.sorm.core.DBManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 连接池类 单例设计
 */
public class DBConnPool {
    /**
     * 最小连接数据，在初始化化时按此大小初始化
     */
    public static final int POOL_MIM_SIZE = DBManager.getConf().getPoolMinSize();//最小连接数

    /**
     * 最大连接数，最终能保存下来的连接数，当连接池数量大于和等于该数时，关闭归还的连接对象
     */
    public static final int POOL_MAX_SIZE = DBManager.getConf().getPoolMaxSize();//最大连接数

    /**
     * 单例对象、静态常量对象、
     */
    public static final DBConnPool dbConnPool = new DBConnPool();
    /**
     * 连接池集合、静态常量对象、
     */
    public final List<Connection> pool;

    /**
     * 当连接池池不够用时加入队列等待（阻塞先进先出）数据连接池、当别人归还连接时，可以唤醒集合队列的线程对象
     */
    private LinkedBlockingQueue<Thread> arrayBlockingQueue = new LinkedBlockingQueue(POOL_MAX_SIZE);//创建队列 100个队列

    private DBConnPool() {
//        TableContext.updateJavaPOFile();
        pool = new ArrayList<>();//连接池对象
        initPool();
    }

    /**
     * 连接池初始化 线程安全
     */
    public void initPool() {
        if (pool.size() < POOL_MIM_SIZE) {
            synchronized (pool) {
                while (pool.size() < POOL_MIM_SIZE) {
                    Connection conn = DBManager.createConn();
                    if (conn != null) {
                        pool.add(conn);//创建连接对象，并放入连接池
                        System.out.println("初始化连接池数量" + pool.size() + "...");
                    } else {
                        System.out.println("也达到数据库连接数据最大值、连接池也初始化完全" + pool.size() + "连接...");
                        break;
                    }
                }
                System.out.println("初始化连接池完成、连接池大小" + pool.size() + "...");
            }
        }
    }

    /**
     * 获取连接池对象  当连接池为空时 将新建数据库连接使用 线程安全
     *
     * @return Connection连接对象
     */
    public synchronized Connection getConnection() {
        if (pool.size() == 0) {
            Connection conn = DBManager.createConn();
            if (conn != null) {
                return conn;
            }
            synchronized (Thread.currentThread()) {
                if (pool.size() == 0) {
                    try {
                        arrayBlockingQueue.put(Thread.currentThread());//添加队列 开始排队
                        Thread.currentThread().wait();//阻塞
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
     * 将连接对象，放回连接池（如果连接池对象超过最大连接数POOL_MAX_SIZE，则关闭连接）
     *
     * @param conn Connection连接对象
     */
    public void close(Connection conn) {
        if (pool.size() < POOL_MAX_SIZE) {
            //存入连接池
            synchronized (pool) {
                if (pool.size() < POOL_MAX_SIZE) {
                    pool.add(conn);
                }
            }

        } else {
            //也达到连接池最大连接数，关闭数据库连接
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        Thread poll = arrayBlockingQueue.poll();
        if (poll != null) {  //判断是否有排队对象
            synchronized (poll) {
                poll.notifyAll();
            }
        }
    }

}
