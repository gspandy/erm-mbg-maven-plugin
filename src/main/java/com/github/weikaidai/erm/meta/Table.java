package com.github.weikaidai.erm.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weikai.dai
 * @version 2018年4月3日 下午4:04:48
 *
 */
public class Table {

	/**
	 * 物理名称，code
	 */
	private String physicalName;
	
	/**
	 * 中文名
	 */
	private String logicalName;
	
	/**
	 * 主键列表
	 */
	private List<Column> primaryKeyColumns = new ArrayList<Column>();
	/**
	 * 字段列表
	 */
	private List<Column> columns = new ArrayList<Column>();

	
	private String catalog;

    private String schema;
    
	
	public String getPhysicalName() {
		return physicalName;
	}

	public void setPhysicalName(String physicalName) {
		this.physicalName = physicalName;
	}

	public String getLogicalName() {
		return logicalName;
	}

	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Column> getPrimaryKeyColumns() {
		return primaryKeyColumns;
	}

	public void setPrimaryKeyColumns(List<Column> primaryKeyColumns) {
		this.primaryKeyColumns = primaryKeyColumns;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
}
