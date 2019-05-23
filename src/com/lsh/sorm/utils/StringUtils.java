package com.lsh.sorm.utils;

import com.lsh.sorm.bean.ColumnInfo;
import com.lsh.sorm.bean.JavaFieldGetSet;
import com.lsh.sorm.bean.TableInfo;
import com.lsh.sorm.core.DBManager;
import com.lsh.sorm.core.TypeConvertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装了字符串常用的操作
 *
 * @author LSH
 */
public class StringUtils {

    /**
     * 将目标字符串首字母变为大写
     *
     * @param str 目标字符串
     * @return 首字母变为大写的字符串
     */
    public static String firstChar2UpperCase(String str) {
        return str.toUpperCase().substring(0, 1) + str.substring(1);
    }

    /**
     * 根据表信息生成Java类的源代码
     *
     * @param tableInfo     表信息
     * @param typeConvertor 数据类型转化器
     * @return Java类的源代码
     */
    public static String createJavaSrc(TableInfo tableInfo, TypeConvertor typeConvertor) {

        Map<String, ColumnInfo> columns = tableInfo.getColumns();//取出所有字段
        List<JavaFieldGetSet> javaFields = new ArrayList<>();//创建字段集合源码
        for (ColumnInfo column : columns.values()) {
            //遍历所有字段
            //并获取没个字段的源码
            //并存入集合
            javaFields.add(JavaFileUtils.createFieldGetSetSRC(column, typeConvertor));

        }
        //开始生成源码
        StringBuilder StrBd = new StringBuilder();

        //生成package信息
        StrBd.append("package " + DBManager.getConf().getPoPackage() + ";\n\n");

        //生成import信息
        StrBd.append("import java.sql.*;\n");
        StrBd.append("import java.util.*;\n");

        //生成类声明信息
        StrBd.append("public class " + firstChar2UpperCase(tableInfo.getTname()) + " {\n\n");

        //生成属性信息
        for (JavaFieldGetSet f : javaFields) {
            StrBd.append(f.getFieldInfo());
        }
        StrBd.append("\n\n");

        //生成get方法信息
        for (JavaFieldGetSet f : javaFields) {
            StrBd.append(f.getGetInfo());
        }

        //生成set方法信息
        for (JavaFieldGetSet f : javaFields) {
            StrBd.append(f.getSetInfo());
        }

        //生成结束符
        StrBd.append("}\n");

//        System.out.println(StrBd.toString());

        return StrBd.toString();
    }
}
