package com.lsh.sorm.core;

/**
 * Query 工厂类 可以通过配置文件 queryClass=com.lsh.sorm.core.MySqlQuery 指定Query的子类
 * 默认com.lsh.sorm.core.MySqlQuery
 *
 * @author LSH
 */
public class QueryFactory {
    private static Query query;

    static {
        try {
            Class aClass = Class.forName(DBManager.conf.getQueryClass());//通过反射
            query = (Query) aClass.newInstance();//创建对象;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private QueryFactory() {
    }

    /**
     * 创建Query对象工厂
     *
     * @return Query数据库执行对象
     */
    public static Query createQuery() {
        try {
            return (Query) query.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
