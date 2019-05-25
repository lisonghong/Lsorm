package com.lsh.sorm.core;

import com.lsh.sorm.bean.TableInfo;

import java.util.List;
import java.util.Map;

public class MySqlQuery extends Query {

    public static void main(String[] args)
    {
        Map<String, TableInfo> tables = TableContext.tables;
    }

    /**
     * @param sql      查询sql
     * @param pageNum  第几页的数据
     * @param pageSize 每页显示多少条记录
     * @param clazz    class 反射对象
     * @param params   参数
     * @return
     */
    @Override
    public List queryPagenate(String sql, int pageNum, int pageSize, Class clazz, Object[] params) {
        //当前页   乘  每页的数量 可对于  也显示页数的数量
        //也显示页数的数量    减去  每页的数量等于   当前页的开始
        int index = (pageNum * pageSize) - pageSize;//当前页开始位置索引
        StringBuilder StrB = new StringBuilder(sql);
        StrB.append("  LIMIT " + index + "," + pageSize + " ");//添加分页查询
        return queryRows(StrB.toString(), clazz, params);
    }
}