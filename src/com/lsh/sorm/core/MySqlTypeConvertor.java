package com.lsh.sorm.core;

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

        System.out.println(columnType);
        return null;
    }

    @Override
    public String JavaType2DatabaseType(String javaDatatype) {
        // TODO 自动生成的方法存根
        return null;
    }

}
