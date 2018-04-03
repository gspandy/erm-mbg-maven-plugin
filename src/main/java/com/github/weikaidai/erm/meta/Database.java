package com.github.weikaidai.erm.meta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * @author weikai.dai
 * @version 2018年4月3日 下午4:04:48
 *
 */
public class Database {

	/**
	 * 资源名称
	 */
	private String resourceName;

	/**
	 * 所有表
	 */
	private List<Table> tables = new ArrayList<Table>();

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
	
	public static Database genDatabase(File file) throws RuntimeException {
		Database database = new Database();
		database.setResourceName(file.getName());
		
		SAXReader sar = new SAXReader();
		
		Document docSource = null;
		try {
			docSource = sar.read(file);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("parse file error:"+file.getName());
		}

		Map<String, Element> tables = new HashMap<String, Element>();
		for (Node node : docSource.selectNodes("/diagram/contents/table")) {
			Element element = (Element) node;
			tables.put(element.elementText("id"), element);
		}

		// 先取�?有的word，组建Column
		Map<String, Element> words = new HashMap<String, Element>();
		// Map<String, Domain> domains = new HashMap<String, Domain>();
		for (Node nodeWord : docSource.selectNodes("/diagram/dictionary/word")) {
			Element element = (Element) nodeWord;
			String id = element.elementText("id");
			words.put(id, element);

			// //Domain
			// Domain domain = parseDomain(nodeWord.elementText("physical_name"),
			// nodeWord.elementText("description"));
			// if (domain != null)
			// {
			// domains.put(id, domain);
			// result.getDomains().add(domain);
			// }
		}

		for (Element elementTable : tables.values()) {
			Table table = new Table();

			table.setPhysicalName(elementTable.elementText("physical_name"));
			table.setLogicalName(elementTable.elementText("logical_name"));

			for (Node nodeColumn : elementTable.selectNodes("columns/*")) {
				Element elementColmn = (Element) nodeColumn;

				Column column = new Column();
				String word_id = elementColmn.elementText("word_id");
				// if (word_id == null)
				// {
				// //没找到的话就找referenced_column
				// Element node = nodeColumn;
				//
				// do
				// {
				// String refId = node.elementText("referenced_column");
				// if (refId == null)
				// throw new IllegalArgumentException();
				// node = (Element)docSource.selectSingleNode("//table/columns/*[id='" + refId +
				// "']");
				// word_id = node.elementText("word_id");
				// }while (StringUtils.isEmpty(word_id));
				// }

				Element nodeWord = words.get(word_id);

				if (Boolean.parseBoolean(elementColmn.elementText("primary_key"))) {
					table.getPrimaryKeyColumns().add(column);
					column.setPrimaryKey(true);
				}

				column.setAutoIncrement(Boolean.parseBoolean(elementColmn.elementText("auto_increment")));
				column.setNotNull(Boolean.parseBoolean(elementColmn.elementText("not_null")));
				column.setUnique(Boolean.parseBoolean(elementColmn.elementText("unique_key")));

				// 以本node的物理名优先
				String physicalName = elementColmn.elementText("physical_name");
				if (StringUtils.isBlank(physicalName)) {
					physicalName = nodeWord.elementText("physical_name");
				}
				column.setPhysicalName(physicalName);

				// 逻辑名也�?样处�?
				String logicalName = elementColmn.elementText("logical_name");
				if (StringUtils.isBlank(logicalName)) {
					logicalName = nodeWord.elementText("logical_name");
				}
				column.setLogicalName(logicalName);

				column.setDescription(nodeWord.elementText("description"));

				String type = nodeWord.elementText("type");
				column.setType(type);
				
				String lengthStr = nodeWord.elementText("length");
				String decimalStr = nodeWord.elementText("decimal");
				if (!"null".equals(lengthStr)&&StringUtils.isNotBlank(lengthStr)) {
					column.setLength(Integer.parseInt(lengthStr));
				}
				if (!"null".equals(decimalStr)&&StringUtils.isNotBlank(decimalStr)) {
					column.setDecimal(Integer.parseInt(decimalStr));
				}

				table.getColumns().add(column);
			}
			database.getTables().add(table);
		}
		
		return database;
	}
}
