package model;

import java.lang.reflect.Field;

import org.springframework.util.NumberUtils;

import assistants.QueryGenerator;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers.NumberSerializer;

public abstract class Model {
	private String tableName;
	private String modelKey;

	public Model(String name, String pkFieldName) {
		tableName = name;
        modelKey = pkFieldName;
	}

	public String getTableName() {
		return tableName;
	}
	public String getModelKey() { return modelKey; }
    public abstract String getModelId();
	public int getFieldCount() {
		return this.getClass().getFields().length;
	}

	public String[] getFieldNames() {
		Field[] fields = this.getClass().getFields();
		String[] names = new String[fields.length];
		for(int i = 0; i < fields.length; i++) {
			names[i] = fields[i].getName();
		}
		return names;
	}
	public String[] getDisplayNames() {
		String[] names = this.getFieldNames();
		for(int i = 0; i < names.length; i++) {
			names[i] = names[i].replaceAll("([a-z])([A-Z])", "$1 $2");
			names[i] = Character.toUpperCase(names[i].charAt(0)) + names[i].substring(1);
		}
		return names;
	}
	public String[] getValues() {
		Field[] fields = this.getClass().getFields();
		String[] values = new String[fields.length];
		for(int i = 0; i < fields.length; i++) {
			try {
				Object check = fields[i].get(this);
				if(check instanceof String) {
					values[i] = "'" + check + "'";
				}
				else {
					values[i] = check.toString();
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				values[i] = "";
			}
		}
		return values;
	}
	
	protected boolean isUnique(String fieldName, Object fieldValue) {
		String where = fieldName + " = ";
		if(fieldValue instanceof String) {
			where += "'" + fieldValue + "'";
		}
		else {
			where += fieldValue;
		}
		return QueryGenerator.getModel(this.getClass(), where) == null;
	}
}
