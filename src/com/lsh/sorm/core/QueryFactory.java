package com.lsh.sorm.core;


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

    public static Query createQuery() {
        try {
            return (Query) query.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
