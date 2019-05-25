package com.lsh.sorm.core;

import java.util.List;

/**
 * mysql数据库实现的Query类
 */
public class MySqlQuery extends Query {


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