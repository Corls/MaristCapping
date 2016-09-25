package model;

import java.lang.reflect.Field;

import org.springframework.util.NumberUtils;

import assistants.QueryGenerator;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers.NumberSerializer;

public class Model {
	private int modelId;
	private String tableName;
	
	public Model(String name, int id) {
		tableName = name;
		modelId = id;
	}
	
	public String getTableName() {
		return tableName;
	}
	public int getModelId() {
		return modelId;
	}
	public int getFieldCount() {
		return this.getClass().getFields().length + 1;
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
					values[i] = "" + check;
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
		if(QueryGenerator.getModel(this.getClass(), where) == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
