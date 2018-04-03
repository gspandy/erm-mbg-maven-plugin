package com.github.weikaidai.erm.maven.process;

import static org.mybatis.generator.internal.util.StringUtility.composeFullyQualifiedTableName;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.ObjectFactory;

import com.github.weikaidai.erm.meta.Database;

/**
 * @author weikai.dai
 * @version 2018年4月3日 下午4:04:48
 *
 */
public class ErmContext extends Context{
	
	private Database database;
	
	private List<IntrospectedTable> introspectedTables;
	private ArrayList<TableConfiguration> tableConfigurations;
	private List<PluginConfiguration> pluginConfigurations;

	public ErmContext(ModelType defaultModelType) {
		super(defaultModelType);

		introspectedTables = new ArrayList<IntrospectedTable>();
		
		setSuperPrivateField("introspectedTables",introspectedTables);
		
        tableConfigurations = (ArrayList<TableConfiguration>) getSuperPrivateField("tableConfigurations");
        pluginConfigurations = (List<PluginConfiguration>) getSuperPrivateField("pluginConfigurations");
	}

	private Field superPrivateField(String fieldName) {
		Field field = null;
		try {
			field = Context.class.getDeclaredField(fieldName);
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
	

    /**
     * This method does a simple validate, it makes sure that all required fields have been filled in. It does not do
     * any more complex operations such as validating that database tables exist or validating that named columns exist
     *
     * @param errors
     *            the errors
     */
    public void validate(List<String> errors) {
        if (!stringHasValue(getId())) {
            errors.add(getString("ValidationError.16")); //$NON-NLS-1$
        }

        if (getJavaModelGeneratorConfiguration() == null) {
            errors.add(getString("ValidationError.8", getId())); //$NON-NLS-1$
        } else {
        	getJavaModelGeneratorConfiguration().validate(errors, getId());
        }

        if (getJavaClientGeneratorConfiguration() != null) {
        	getJavaClientGeneratorConfiguration().validate(errors, getId());
        }

        IntrospectedTable it = null;
        try {
            it = ObjectFactory.createIntrospectedTableForValidation(this);
        } catch (Exception e) {
            errors.add(getString("ValidationError.25", getId())); //$NON-NLS-1$
        }

//        if (it != null && it.requiresXMLGenerator()) {
            if (getSqlMapGeneratorConfiguration() == null) {
                errors.add(getString("ValidationError.9", getId())); //$NON-NLS-1$
            } else {
                getSqlMapGeneratorConfiguration().validate(errors, getId());
            }
//        }

        if (tableConfigurations.size() == 0) {
            errors.add(getString("ValidationError.3", getId())); //$NON-NLS-1$
        } else {
            for (int i = 0; i < tableConfigurations.size(); i++) {
                TableConfiguration tc = tableConfigurations.get(i);

                tc.validate(errors, i);
            }
        }
            
        for (PluginConfiguration pluginConfiguration : pluginConfigurations) {
            pluginConfiguration.validate(errors, getId());
        }
    }
    
    public void introspectTables(ProgressCallback callback,
            List<String> warnings, Set<String> fullyQualifiedTableNames)
            throws SQLException, InterruptedException {

        callback.startTask(getString("Progress.0")); //$NON-NLS-1$

        ErmDatabaseIntrospector databaseIntrospector = null;
		databaseIntrospector = new ErmDatabaseIntrospector(this,this.database);
        
        for (TableConfiguration tc : tableConfigurations) {
            String tableName = composeFullyQualifiedTableName(tc.getCatalog(), tc
                            .getSchema(), tc.getTableName(), '.');

            if (fullyQualifiedTableNames != null
                    && fullyQualifiedTableNames.size() > 0
                    && !fullyQualifiedTableNames.contains(tableName)) {
                continue;
            }

            if (!tc.areAnyStatementsEnabled()) {
                warnings.add(getString("Warning.0", tableName)); //$NON-NLS-1$
                continue;
            }

            callback.startTask(getString("Progress.1", tableName)); //$NON-NLS-1$
            List<IntrospectedTable> tables = databaseIntrospector.introspectTables(tc);

            if (tables != null) {
                introspectedTables.addAll(tables);
            }

            callback.checkCancel();
        }
    }

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}
