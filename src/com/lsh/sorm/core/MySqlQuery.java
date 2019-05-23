package com.lsh.sorm.core;

import com.lsh.po.ABC;
import com.lsh.po.NewS;
import com.lsh.sorm.bean.TableInfo;

import java.util.List;
import java.util.Map;

public class MySqlQuery extends Query {

    public static void A() {
        //        Map<String, TableInfo> tables = TableContext.tables;
//        ABC newS = new ABC();
//        newS.setId(2);
//        new MySqlQuery().delete(newS);

        ABC abc = new ABC();
//        abc.setId(5325);
        abc.setName("sdfdddddddddddddddddd");

        //   new MySqlQuery().insert(abc);
        int update = new MySqlQuery().update(abc, new String[]{"name", "idrg"});
        System.out.println(update);
    }

    public static void main(String[] args) {
        Map<String, TableInfo> tables = TableContext.tables;

        List<ABC> list = new MySqlQuery().queryRows("SELECT * FROM ABC", ABC.class, null);
        for (ABC a : list) {
            System.out.println(a.getId() + "    " + a.getName());
        }

        Object o = new MySqlQuery().queryValue("select count(*) from ABC", null);
        System.out.println(o);

        Number number = new MySqlQuery().queryNumber("select numa from ABC where id=1", null);
        System.out.println(number);


        List<NewS> list1 = new MySqlQuery().queryRows("select * from newS", NewS.class, null);

        for (NewS s : list1) {
            System.out.println("文章ID" + s.getNews_ID() + "     文章标题" + s.getTitle() + "     文章描述" + s.getDescribes() + "     所属公司" + s.getCorporate_name() + "     所属用户名" + s.getUserName() + "     所属时间" + s.getTime() + "     文章内容" + s.getText());
        }



/*
        ABC abc = new ABC();
        abc.setId(1);
        abc.setName("李松鸿哦哦哦哦哦哦");
        new MySqlQuery().update(abc);*/


    }


    @Override
    public Object queryPagenate(int pageNum, int siez) {
        return null;
    }
}
