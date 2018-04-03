package com.github.weikaidai.erm.maven.process;

import java.lang.reflect.Field;

import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Context;

import tk.mybatis.mapper.generator.MapperCommentGenerator;
import tk.mybatis.mapper.generator.MapperPlugin;

/**
 * @author weikai.dai
 * @version 2018年4月3日 下午4:04:48
 *
 */
public class ErmMapperPlugin extends MapperPlugin {

	@Override
	public void setContext(Context context) {
		// super.setContext(context);

		boolean useMapperCommentGenerator = (Boolean) getSuperPrivateField("useMapperCommentGenerator");
		CommentGeneratorConfiguration commentCfg = (CommentGeneratorConfiguration) getSuperPrivateField("commentCfg");
		
		useMapperCommentGenerator = !"FALSE".equalsIgnoreCase(context.getProperty("useMapperCommentGenerator"));
		if (useMapperCommentGenerator) {
			commentCfg = new CommentGeneratorConfiguration();
			commentCfg.setConfigurationType(MapperCommentGenerator.class.getCanonicalName());
			setSuperPrivateField("commentCfg",commentCfg);
			context.setCommentGeneratorConfiguration(commentCfg);
		}

		// context.getJdbcConnectionConfiguration().addProperty("remarksReporting","true");
	}

	private Field superPrivateField(String fieldName) {
		Field field = null;
		try {
			field = MapperPlugin.class.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		field.setAccessible(true);
		return field;
	}
	
	private Object getSuperPrivateField(String fieldName) {
		Field field = superPrivateField(fieldName);
		Object obj = null;
		try {
			obj = field.get(this);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	private void setSuperPrivateField(String fieldName,Object value) {
		Field field = superPrivateField(fieldName);
		try {
			field.set(this, value);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
