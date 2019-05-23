package com.lsh.sorm.core;

import com.lsh.sorm.bean.ColumnInfo;
import com.lsh.sorm.bean.TableInfo;
import com.lsh.sorm.utils.JDBCUtils;
import com.lsh.sorm.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public abstract class Query {

    public Object executeQueryTemplate(String sql, Object[] params, Class clazz, CallBack callBack) {
        //查询
        Connection conn = DBManager.getConn();//获取连接对象
        PreparedStatement Ps = null;
        ResultSet resultSet = null;
        try {
            Ps = conn.prepareStatement(sql);//通过sql获取Ps
            JDBCUtils.handleParams(Ps, params);   //给Ps设置参数
            resultSet = Ps.executeQuery();//执行查询
            return callBack.doExecute(conn, Ps, resultSet, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            DBManager.close(Ps, conn);
        }

    }


    /**
     * 直接执行一个DML
     *
     * @param sql    sql语句
     * @param params 参数
     * @return
     */

    public int executeDml(String sql, Object[] params) {
        Connection conn = DBManager.getConn();
        PreparedStatement Ps = null;
        int count = 0;
        try {
            Ps = conn.prepareStatement(sql);
            JDBCUtils.handleParams(Ps, params);
            System.out.println(Ps);
            count = Ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.close(Ps, conn);
        }
        return count;
    }

    /**
     * 将一个对象存储到数据中
     * 把对象不为null的属性往数据库中存储，对数字，如果为null，放0
     *
     * @param obj 要存储的对象
     * @return 成功执行条数
     */
    public int insert(Object obj) {

        //数据库插入操作 insert into Name(id,pwd) values(?,?)
        Class aClass = obj.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(aClass);
        StringBuilder sql = new StringBuilder("insert into " + tableInfo.getTname() + "(");
        Field[] fields = aClass.getDeclaredFields();
        List<Object> params = new ArrayList<>();
        int fieldNum = 0;
        for (Field field : fields) {
            String fieldName = field.getName();//属性名称
            Object fieldValus = ReflectUtils.invokeGet(fieldName, obj);
            if (fieldValus != null) {
                //开始拼接sql
                fieldNum++;//参数计数
                sql.append(fieldName + ",");
                params.add(fieldValus);
            }
        }
        sql.setCharAt(sql.length() - 1, ')');
        sql.append(" values(");
        for (int i = 0; i < fieldNum; i++) {
            sql.append("?,");
        }
        sql.setCharAt(sql.length() - 1, ')');
        return executeDml(sql.toString(), params.toArray());

    }

    /**
     * 删除clazz表示对应的表中记录（指定ID）
     *
     * @param clazz 跟这个表对应的class对象
     * @param id    主键的值
     * @return
     */
    public int delete(Class clazz, Object id) {
        TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();//获取主键删除
        String sql = "delete from " + tableInfo.getTname() + " where " + onlyPriKey.getName() + "=? ";
        return executeDml(sql, new Object[]{id});//执行语句并返回受影响的行数
    }

    /**
     * 删除对象在数据库中对应的记录（对象所在的类对应到表，对象的主键的值对应到记录）
     *
     * @param obj
     * @return
     */
    public int delete(Object obj) {
        Class aClass = obj.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(aClass);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
        //通过反射调用get方法
        Object id = ReflectUtils.invokeGet(onlyPriKey.getName(), obj);//反射get方法
        return delete(aClass, id);
    }

    /**
     * 更新对象对应的记录，并且更新指定的字段的值
     *
     * @param obj       所要更新的对象
     * @param fieldname 更新的属性内容
     * @return 执行sql语句后影响的记录行数
     */
    public int update(Object obj, String[] fieldname) {
        //obj{"user","pwd"}--> update 表名 set username=? , pwd=? where id=?
        Class aClass = obj.getClass();//获得类class
        List<Object> params = new ArrayList<>();
        TableInfo tableInfo = TableContext.poClassTableMap.get(aClass);
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();//获得主键
        StringBuilder sql = new StringBuilder();
        sql.append("update " + tableInfo.getTname() + " set ");
        for (String fname : fieldname) {
            Object o = ReflectUtils.invokeGet(fname, obj);
            if (o != null) {
                sql.append(fname + "=?,");
                params.add(o);
            }
        }
        sql.setCharAt(sql.length() - 1, ' ');
        sql.append("where " + onlyPriKey.getName() + "=?");
        Object id = ReflectUtils.invokeGet(onlyPriKey.getName(), obj);
        if (id != null) {
            params.add(id);
        } else {
            return -1;
        }
        return executeDml(sql.toString(), params.toArray());
    }

    /**
     * 更新所有的属性
     *
     * @param obj 通过ID 更新对象
     * @return
     */
    public int update(Object obj) {
        //通过ID 更新对象  update 表名 set username=? , pwd=? where id=?
        Class aClass = obj.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(aClass);//获取表信息，表结构
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();//获取主键信息；
        Field[] fields = aClass.getDeclaredFields();//获取对象所有的属性
        List<Object> params = new ArrayList<>();//参数
        StringBuilder sql = new StringBuilder("update " + tableInfo.getTname() + " set ");
        for (Field fname : fields) {
            String colname = fname.getName();
            Object colObj = ReflectUtils.invokeGet(colname, obj);
            if (colObj != null) {//列不等于null开始拼接sql
                sql.append(colname + "=?,");
                params.add(colObj);
            }
        }
        sql.setCharAt(sql.length() - 1, ' ');//替换最后一位

        sql.append("where " + onlyPriKey.getName() + "=?");
        params.add(ReflectUtils.invokeGet(onlyPriKey.getName(), obj));
        return executeDml(sql.toString(), params.toArray());
    }

    /**
     * 查询并返回多行记录，并将每行记录封装到clazz指定的类对象中
     *
     * @param sql    查询语句
     * @param clazz  封装数据的Javabenan类的Class对象
     * @param params sql的参数
     * @return 查询到的结果
     */
    public List queryRows(String sql, Class clazz, Object[] params) {

        return (List) executeQueryTemplate(sql, params, clazz, new CallBack() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement Ps, ResultSet resultSet, Class clazz) {
                List list = null;
                try {
                    ResultSetMetaData metaData = resultSet.getMetaData();//获取列信息，列参数等等
                    while (resultSet.next()) {//查询是否有数据
                        if (list == null) {
                            list = new ArrayList();
                        }
                        Object obj = clazz.newInstance();//反射创建对象

                        for (int i = 0; i < metaData.getColumnCount(); i++) {
                            //列操作 循环列，并设置
                            String columnLabel = metaData.getColumnLabel(i + 1);//获取列明   username
                            Object setobj = resultSet.getObject(i + 1);//列的值    第几个列
                            if (setobj != null) {//保存值不为null
                                System.out.println(setobj+"        "+columnLabel);
                                ReflectUtils.invokeSet(columnLabel, setobj, obj);//通过set方法设置    通过列执行set方法
                            }

                        }
                        list.add(obj);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return list;
            }
        });


    }

    /**
     * 查询并返回一行记录，并将该行记录封装到clazz指定的类对象中
     *
     * @param sql    查询语句
     * @param clazz  封装数据的Javabenan类的Class对象
     * @param params sql的参数
     * @return 查询到的结果
     */
    public Object queryUniqueRow(String sql, Class clazz, Object[] params) {
        List list = queryRows(sql, clazz, params);
        return (list == null) ? null : list.get(0);
    }


    /**
     * 查询并返回一个值（一行一列），并将该值返回
     *
     * @param sql    查询语句
     * @param params sql的参数
     * @return 查询到的结果
     */
    public Object queryValue(String sql, Object[] params) {

        return executeQueryTemplate(sql, params, null, new CallBack() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement Ps, ResultSet resultSet, Class clazz) {
                Object objValue = null;
                try {
                    while (resultSet.next()) {//查询是否有数据
                        objValue = resultSet.getObject(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return objValue;
            }
        });
    }


    /**
     * 查询并返回一个数字（一行一列），并将该值返回
     *
     * @param sql    查询语句
     * @param params sql的参数
     * @return 查询到的结果
     */
    public Number queryNumber(String sql, Object[] params) {
        return (Number) queryValue(sql, params);
    }

    /**
     * 分页查询
     *
     * @param pageNum 第几页的数据
     * @param siez    每页显示多少条记录
     * @return
     */
    public abstract Object queryPagenate(int pageNum, int siez);


}
