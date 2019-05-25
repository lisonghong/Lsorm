package com.lsh.sorm.bean;

/**
 * 管理配置信息
 *
 * @author LSH
 */
public class Configuration {
    /**
     * 驱动类 默认com.mysql.cj.jdbc.Driver
     */
    private String driver="com.mysql.cj.jdbc.Driver";
    /**
     * jdbc的url
     */
    private String url;
    /**
     * 数据库的用户名
     */
    private String user;
    /**
     * 数据库的密码
     */
    private String pwd;
    /**
     * 正在使用哪个数据库
     */
    private String usingDB;
    /**
     * 项目的源码路径  默认当前程序的src目录
     */
    private String srcPath=System.getProperty("user.dir") + "\\src";
    /**
     * 扫描生成Java类的包(po的意思是：Persistence object持久化对象)  默认包 DBClass
     */
    private String poPackage="DBClass";

    /**
     * 使用哪个query实现类   默认使用com.lsh.sorm.core.MySqlQuery
     */
    private String queryClass="com.lsh.sorm.core.MySqlQuery";

    /**
     * 连接池最小连接数  默认初始最小连接2
     */
    private int poolMinSize = 2;

    /**
     * 连接池最大连接数 默认空闲最大连接数10
     */
    private int poolMaxSize = 10;


    public Configuration() {
    }

    public Configuration(String driver, String url, String user, String pwd, String usingDB, String srcPath, String poPackage, String queryClass, int poolMinSize, int poolMaxSize) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.pwd = pwd;
        this.usingDB = usingDB;
        this.srcPath = srcPath;
        this.poPackage = poPackage;
        this.queryClass = queryClass;
        this.poolMinSize = poolMinSize;
        this.poolMaxSize = poolMaxSize;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUsingDB() {
        return usingDB;
    }

    public void setUsingDB(String usingDB) {
        this.usingDB = usingDB;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getPoPackage() {
        return poPackage;
    }

    public void setPoPackage(String poPackage) {
        this.poPackage = poPackage;

    }

    public String getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(String queryClass) {
        this.queryClass = queryClass;
    }


    public int getPoolMinSize() {
        return poolMinSize;
    }

    public void setPoolMinSize(int poolMinSize) {
        this.poolMinSize = poolMinSize;
    }

    public int getPoolMaxSize() {
        return poolMaxSize;
    }

    public void setPoolMaxSize(int poolMaxSize) {
        this.poolMaxSize = poolMaxSize;
    }


}
