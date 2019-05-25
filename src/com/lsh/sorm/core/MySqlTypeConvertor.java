package com.lsh.sorm.core;

/**
 * mysql数据库实现的TypeConvertor类、用于数据库字段类型和Java类型的相互转换
 */
public class MySqlTypeConvertor implements TypeConvertor {

    @Override
    public String datebaseType2JavaType(String columnType) {

        int i = columnType.lastIndexOf(" ");
        if (i != -1) {
            columnType = columnType.substring(0, i);
            System.out.println("数据截取" + columnType);
        }
        if ("varchar".equalsIgnoreCase(columnType)
                || "char".equalsIgnoreCase(columnType)) {
            return "String";
        } else if ("int".equalsIgnoreCase(columnType)
                || "tinyint".equalsIgnoreCase(columnType)
                || "smallint".equalsIgnoreCase(columnType)
                || "integer".equalsIgnoreCase(columnType)
                || "BIT".equalsIgnoreCase(columnType)) {
            return "Integer";
        } else if ("bigint".equalsIgnoreCase(columnType)) {
            return "Long";
        } else if ("double".equalsIgnoreCase(columnType)
                || "float".equalsIgnoreCase(columnType)) {
            return "Double";
        } else if ("clob".equalsIgnoreCase(columnType)) {
            return "java.sql.CLob";
        } else if ("blob".equalsIgnoreCase(columnType)) {
            return "java.sql.BLob";
        } else if ("date".equalsIgnoreCase(columnType)) {
            return "java.sql.Date";
        } else if ("time".equalsIgnoreCase(columnType)) {
            return "java.sql.Time";
        } else if ("timestamp".equalsIgnoreCase(columnType)) {
            return "java.sql.Timestamp";
        } else if ("TEXT".equalsIgnoreCase(columnType)) {
            return "String";
            // return "StringBuilder";
        }
        System.out.println(columnType + "类型转换错误！请检查Sorm以及代码");
        return null;
    }

    /**
     * @param javaDatatype Java数据类型转数据库类型
     * @return 转换好的 数据库类型字符串
     */
    @Override
    public String JavaType2DatabaseType(String javaDatatype) {
        return null;
    }

}
