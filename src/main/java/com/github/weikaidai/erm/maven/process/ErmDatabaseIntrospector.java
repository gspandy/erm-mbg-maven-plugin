package com.github.weikaidai.erm.maven.process;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringContainsSpace;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.types.JdbcTypeNameTranslator;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import com.github.weikaidai.erm.meta.Column;
import com.github.weikaidai.erm.meta.Database;
import com.github.weikaidai.erm.meta.Table;

/**
 * @author weikai.dai
 * @version 2018年4月3日 下午4:04:48
 *
 */
public class ErmDatabaseIntrospector{
	
	private FullyQualifiedJavaType fqjtInteger = new FullyQualifiedJavaType("java.lang.Integer");
	
	private FullyQualifiedJavaType fqjtBigDecimal = new FullyQualifiedJavaType("java.math.BigDecimal");
	
	private FullyQualifiedJavaType fqjtLong = new FullyQualifiedJavaType("java.lang.Long");
	
	private FullyQualifiedJavaType fqjtDate = new FullyQualifiedJavaType("java.util.Date");
	
	private Database database ;
	
	private Context context;
	
	public ErmDatabaseIntrospector(Context context,Database database) {
		this.context = context;
		this.database = database;
	}

	public IntrospectedColumn getColumn(Column column){
		IntrospectedColumn introspectedColumn = ObjectFactory.createIntrospectedColumn(context);
		
		introspectedColumn.setActualColumnName(column.getPhysicalName());
		
		String type = column.getType();
		
		if ("char".equals(type))
		{
			introspectedColumn.setFullyQualifiedJavaType(FullyQualifiedJavaType.getStringInstance());
			introspectedColumn.setJdbcTypeName("CHAR");
			introspectedColumn.setJdbcType(JdbcTypeNameTranslator.getJdbcType(introspectedColumn.getJdbcTypeName()));
		}
		else if ("character(n)".equals(type))
		{
			introspectedColumn.setFullyQualifiedJavaType(FullyQualifiedJavaType.getStringInstance());
			introspectedColumn.setJdbcTypeName("CHAR");
		}
		else if ("varchar(n)".equals(type))
		{
			introspectedColumn.setFullyQualifiedJavaType(FullyQualifiedJavaType.getStringInstance());
			introspectedColumn.setJdbcTypeName("VARCHAR");
		}
		else if ("decimal".equals(type))
		{
			introspectedColumn.setFullyQualifiedJavaType(fqjtInteger);
			introspectedColumn.setJdbcTypeName("INTEGER");
		}
		else if ("decimal(p)".equals(type)||"numeric(p)".equals(type))
		{
			int l = column.getLength();
			if (l < 8 ) {
				introspectedColumn.setFullyQualifiedJavaType(fqjtInteger);
				introspectedColumn.setJdbcTypeName("INTEGER");
			}
			else {
				introspectedColumn.setFullyQualifiedJavaType(fqjtBigDecimal);
				introspectedColumn.setJdbcTypeName("DECIMAL");
			}
		}
		else if ("decimal(p,s)".equals(type)||"numeric(p,s)".equals(type))
		{
			int l = column.getLength();
			int s = column.getDecimal();
			if (s == 0 && l < 8 ) {
				introspectedColumn.setFullyQualifiedJavaType(fqjtInteger);
				introspectedColumn.setJdbcTypeName("INTEGER");
			}
			else {
				introspectedColumn.setFullyQualifiedJavaType(fqjtBigDecimal);
				introspectedColumn.setJdbcTypeName("DECIMAL");
			}
		}
		else if ("integer".equals(type))
		{
			introspectedColumn.setFullyQualifiedJavaType(fqjtInteger);
			introspectedColumn.setJdbcTypeName("INTEGER");
		}
		else if ("bigint".equals(type))
		{

			introspectedColumn.setFullyQualifiedJavaType(fqjtLong);
			introspectedColumn.setJdbcTypeName("BIGINT");
		}
		else if ("date".equals(type))
		{
			introspectedColumn.setFullyQualifiedJavaType(fqjtDate);
			introspectedColumn.setJdbcTypeName("DATE");
		}
		else if ("time".equals(type))
		{
			introspectedColumn.setFullyQualifiedJavaType(fqjtDate);
			introspectedColumn.setJdbcTypeName("DATE");
		}
		else if ("timestamp".equals(type)||"datetime".equals(type))
		{
			introspectedColumn.setFullyQualifiedJavaType(fqjtDate);
			introspectedColumn.setJdbcTypeName("DATE");
		}
		else if ("clob".equals(type) || type.endsWith("text"))
		{
			introspectedColumn.setFullyQualifiedJavaType(FullyQualifiedJavaType.getStringInstance());
			introspectedColumn.setJdbcTypeName("CLOB");
		}
		else if (type.endsWith("blob"))
		{
			introspectedColumn.setFullyQualifiedJavaType(new FullyQualifiedJavaType("byte[]"));
			introspectedColumn.setJdbcTypeName("BLOB");
		}
		else
		{
			System.out.println(MessageFormat.format("无法识别的类型[{0}]，跳过, {1}, {2}", type, column.getPhysicalName()));// table.getDbName()));
		}
		
		introspectedColumn.setJdbcType(JdbcTypeNameTranslator.getJdbcType(introspectedColumn.getJdbcTypeName()));
		introspectedColumn.setJavaProperty(JavaBeansUtil.getCamelCaseString(introspectedColumn.getActualColumnName(), false));
		return introspectedColumn;
	}
	
	
	public IntrospectedTable introspectTable(TableConfiguration tc,Table table) {
		
		boolean delimitIdentifiers = tc.isDelimitIdentifiers()
                || stringContainsSpace(tc.getCatalog())
                || stringContainsSpace(tc.getSchema())
                || stringContainsSpace(tc.getTableName());
		
		FullyQualifiedTable fullyQualifiedTable = new FullyQualifiedTable(
                stringHasValue(tc.getCatalog()) ? table.getCatalog() : null,
                stringHasValue(tc.getSchema()) ? table.getSchema() : null,
                table.getPhysicalName(),
                tc.getDomainObjectName(),
                tc.getAlias(),
                isTrue(tc.getProperty(PropertyRegistry.TABLE_IGNORE_QUALIFIERS_AT_RUNTIME)),
                tc.getProperty(PropertyRegistry.TABLE_RUNTIME_CATALOG),
                tc.getProperty(PropertyRegistry.TABLE_RUNTIME_SCHEMA),
                tc.getProperty(PropertyRegistry.TABLE_RUNTIME_TABLE_NAME),
                delimitIdentifiers,
                tc.getDomainObjectRenamingRule(),
                context);
		
		IntrospectedTable introspectedTable = ObjectFactory.createIntrospectedTable(tc, fullyQualifiedTable, context);
		
		List<Column> columns = table.getColumns();
		for(Column column : columns){
			introspectedTable.addColumn(getColumn(column));
			if(column.isPrimaryKey()==true) {
				introspectedTable.addPrimaryKeyColumn(column.getPhysicalName());
			}
		}
		
		
		return introspectedTable;
	}
	
	public List<IntrospectedTable> introspectTables(TableConfiguration tc) {
		
		List<IntrospectedTable> introspectedTables = new ArrayList<IntrospectedTable>();
		
		List<Table> tables = database.getTables();
		for(Table table:tables) {
			if(tc.getTableName().equalsIgnoreCase(table.getPhysicalName())){
				introspectedTables.add(introspectTable(tc,table));
			}
		}
		return introspectedTables;
	}
	
	  
}
