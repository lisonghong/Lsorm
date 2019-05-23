package com.lsh.sorm.core;
/**
 * 负责Java数据类型和数据数据类型的互相转换
 * @author LSH
 *
 */
public interface TypeConvertor {


	/**
	 * 将数据库数据类型转化成Java的数据类型
	 * @param columnType 数据库字段的数据类型
	 * @return Java的数据类型
	 */
	public String datebaseType2JavaType(String columnType);


	/**
	 * 负责将Java数据类型转化成数据库数据类型
	 * @param javaDatatype	Java数据类型
	 * @return 数据库类型
	 */
	public String JavaType2DatabaseType(String javaDatatype);

}
