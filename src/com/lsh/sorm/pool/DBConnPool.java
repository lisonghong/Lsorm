package com.lsh.sorm.pool;

import com.lsh.sorm.core.DBManager;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * 连接池类
 */
public class DBConnPool {
    private LinkedBlockingQueue<Thread> arrayBlockingQueue1 = new LinkedBlockingQueue(1);
    //5428 12262 10509 2591 3007 4491
    //4675  2605    2950    4401    3064
    private ArrayBlockingQueue<Thread> arrayBlockingQueue = new ArrayBlockingQueue(100);
    //100的   4045    8708    3677    4032   4287
    //10的   3566    2463    3070    5539   3095
    //5的    5845    3786    5690    3296    4993
    //2的   2529     4793    2473    2975    4434
    //1的   3355     2271    4863    3735    5314
    private ConcurrentLinkedQueue<Thread> arrayBlockingQueue3 = new ConcurrentLinkedQueue();
    //4366  4631    7892       3484     4319


    public List<Connection> pool;//连接池对象
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
            synchronized (Thread.currentThread()) {
                try {
                    arrayBlockingQueue.add(Thread.currentThread());//添加队列 开始排队
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
