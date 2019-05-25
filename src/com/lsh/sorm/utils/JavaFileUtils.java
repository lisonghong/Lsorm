package com.lsh.sorm.utils;

import com.lsh.sorm.bean.ColumnInfo;
import com.lsh.sorm.bean.JavaFieldGetSet;
import com.lsh.sorm.bean.TableInfo;
import com.lsh.sorm.core.DBManager;
import com.lsh.sorm.core.MySqlTypeConvertor;
import com.lsh.sorm.core.TypeConvertor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 封装了生成Java文件（源代码）常用的操作
 *
 * @author LSH
 */
public class JavaFileUtils {

    /**
     * 根据字段信息生成Java属性信息。如varchar username --> private String username
     *
     * @param column    字段信息
     * @param convertor 类型转化器
     * @return Java属性和set/get方法源码
     */
    public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column, TypeConvertor convertor) {
        JavaFieldGetSet jfgs = new JavaFieldGetSet();
        String javaFieldType = convertor.datebaseType2JavaType(column.getDataType());
        jfgs.setFieldInfo("\tprivate " + javaFieldType + " " + column.getName() + ";\n");

        //public String getUsername(){return username;}
        //生成get方法的源代码
        StringBuilder getSec = new StringBuilder();
        getSec.append("\tpublic " + javaFieldType + " get" + StringUtils.firstChar2UpperCase(column.getName() + "(){\n"));
        getSec.append("\t\treturn " + column.getName() + ";\n");
        getSec.append("\t}\n");
        jfgs.setGetInfo(getSec.toString());

        //public void SetUsername(String username){this.username=username}
        //生成set方法源码

        StringBuilder setSrc = new StringBuilder();
        setSrc.append("\tpublic void set" + StringUtils.firstChar2UpperCase(column.getName()) + "(" + javaFieldType + " " + column.getName() + "){\n");
        setSrc.append("\t\tthis." + column.getName() + "=" + column.getName() + ";\n");
        setSrc.append("\t}\n");
        jfgs.setSetInfo(setSrc.toString());

        return jfgs;
    }

    public static void createJavaPOFile(TableInfo tableInfo, TypeConvertor typeConvertor) {
        //获取类源码;
        String javaSrc =StringUtils. createJavaSrc(tableInfo, typeConvertor);

        //通过IO生成Java文件
        File file = new File(DBManager.getConf().getSrcPath() + "/" + DBManager.getConf().getPoPackage().replace(".", "//"));
        if (file.exists()) {
            file.mkdirs();
        }

        BufferedWriter BufRe = null;
        FileWriter fileWriter =null;
        try {
            String javapath = file.getAbsoluteFile() + "/" + StringUtils.firstChar2UpperCase(tableInfo.getTname()) + ".java";
            fileWriter = new FileWriter(javapath);
            BufRe = new BufferedWriter(fileWriter);
            BufRe.write(javaSrc);
            System.out.println("创建表对应" + tableInfo.getTname() + "类、");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (BufRe != null) {
                try {
                    BufRe.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileWriter!=null)
            {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

    }
}
