package com.github.weikaidai.erm.maven.process;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.github.weikaidai.erm.meta.Database;
import com.github.weikaidai.erm.meta.Table;

/**
 * @author weikai.dai
 * @version 2018年4月3日 下午4:04:48
 *
 */
public class Generator {
	
	private Database database;
	private MavenConfig mavenConfig;
	
	public Generator(Database database, MavenConfig mavenConfig){
		this.database = database;
		this.mavenConfig = mavenConfig;
	}
	

	public void generatorJava(){
		
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		
		ErmContext myContext = new ErmContext(ModelType.FLAT);
		myContext.setDatabase(database);
		
		myContext.setId(database.getResourceName());
		myContext.setTargetRuntime("MyBatis3Simple");
		myContext.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
		myContext.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");
		
		JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
		javaModelGeneratorConfiguration.setTargetPackage(mavenConfig.getTargetPackage()+".entity");
		javaModelGeneratorConfiguration.setTargetProject("src/main/java");
		
		SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
		sqlMapGeneratorConfiguration.setTargetPackage("mapper");
		sqlMapGeneratorConfiguration.setTargetProject("src/main/resources");
		
		JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
		javaClientGeneratorConfiguration.setTargetPackage(mavenConfig.getTargetPackage()+".entitymapper");
		javaClientGeneratorConfiguration.setTargetProject("src/main/java");
		javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
		
		myContext.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
		myContext.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
		myContext.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
		
		
		PluginConfiguration pluginConfiguration = new PluginConfiguration();
		pluginConfiguration.setConfigurationType("com.github.weikaidai.erm.maven.process.ErmMapperPlugin");//tk.mybatis.mapper.generator.MapperPlugin
		pluginConfiguration.addProperty("mappers", mavenConfig.getSuperMapper());
		myContext.addPluginConfiguration(pluginConfiguration);
		
		for(Table table:database.getTables()) {
			TableConfiguration tc = new TableConfiguration(myContext);
			tc.setTableName(table.getPhysicalName());
			myContext.addTableConfiguration(tc);
		}
		
		Configuration config = new Configuration();
		config.addContext(myContext);
		
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = null;
		try {
			myBatisGenerator = new MyBatisGenerator(config,callback, warnings);
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("InvalidConfigurationException");
		}
		
		try {
			myBatisGenerator.generate(null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} 
		
	}
}
