# Lsorm
模仿sorm框架，实现连接池功能，实现对象到表，表到对象
db.properties为配置文件  




driver=com.mysql.cj.jdbc.Driver  
url=jdbc:mysql://121.42.168.192:3306/JB?characterEncoding=UTF-8  
user=root  
pwd=123456  
usingDB=JB  
srcPath=D:\\JavaSe\\SORM\\src
poPackage=DBClass
queryClass=com.lsh.sorm.core.MySqlQuery
poolMinSize=2  
poolMaxSize=10  












driver               数据库加载驱动,默认com.mysql.cj.jdbc.Driver  
url                  数据库连接url  
user                 数据库用户名  
pwd                  数据库密码  
usingDB              使用哪个数据库  
srcPath              生成表对应的类文件位置，默认为当前项目的src目录,建议默认，注释该行  
poPackage            生成表对应的类的包文件，默认DBClass     建议默认，注释该行  
queryClass           使用哪个Query对象的子类进行查询，如果是mysql 使用默认，也可以自行继承Query重写queryPagenate  
poolMinSize          初始化连接池最小值，默认为2,建议默认，注释该行  
poolMaxSize          连接池最大空闲连接，该配置决定你最后连接池能保留的数据，根据程序同时并发量设置  
  
  
  
  
欢迎各位共同修改bug  
有任何使用问题和意见欢迎邮箱骚扰1154653270@qq.com  
