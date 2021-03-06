package com.lsh.sorm.utils;

import java.lang.reflect.Method;

/**
 * 封装了反射常用的操作
 *
 * @author LSH
 */
@SuppressWarnings("all")
public class ReflectUtils {

    /**
     * 调用Object对象某给属性的get方法
     *
     * @param fieldName 属性名
     * @param obj       Object对象
     * @return 返回get方法的对象值
     */
    public static Object invokeGet(String fieldName, Object obj) {
        Class aClass = obj.getClass();
        try {
            Method method = aClass.getDeclaredMethod("get" + StringUtils.firstChar2UpperCase(fieldName), null);
            return method.invoke(obj);
        } catch (Exception e) {
            //  e.printStackTrace();
            return null;
        }
    }

    /**
     * 调用Object对象某给属性的set方法
     *
     * @param fieldName 属性名
     * @param setObj    需要设置的Object对象
     * @param obj       设置的原对象
     */
    public static void invokeSet(String fieldName, Object setObj, Object obj) {
        if (setObj != null) {   //保存值不为null
            Class aClass = obj.getClass();
            String s = "set" + StringUtils.firstChar2UpperCase(fieldName);
            try {
                Method method = aClass.getDeclaredMethod(s, setObj.getClass());
                method.invoke(obj, setObj);
            } catch (Exception e) {
                System.out.println(s);
                System.out.println(setObj);
                e.printStackTrace();
            }
        }


    }


}
