package assistants;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Model;

public class QueryGenerator {
	public static <T extends Model> T getModelById(Class<T> c, String id) {
		T t = null;
		try {
			Model model = c.newInstance();
			String selectQuery = "SELECT * FROM " + model.getTableName() + " WHERE " + model.getModelKey() + " = " + id;
			List<Object[]> results = Database.getData(selectQuery, model.getFieldCount());
			if(!results.isEmpty()) {
				t = c.getDeclaredConstructor(Object[].class).newInstance(new Object[] {results.get(0)});
			}
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
	public static Model getModel(Class<? extends Model> c, String where) {
		List<Model> mList = getAll(c, where);
		if(mList.isEmpty()) {
			return null;
		}
		return mList.get(0);
	}
	
	public static List<Model> getModels(Class<? extends Model> c, String where, Integer show, Integer page) {
		List<Model> mList = new ArrayList<>();
		Integer start = show * (page-1);
		Integer end = start + show;
		try {
			Model model = c.newInstance();
			String selectQuery = "SELECT * FROM " + model.getTableName() + (where.isEmpty() ? "" : " WHERE " + where);
			List<Object[]> results = Database.getData(selectQuery, model.getFieldCount());
			if(results.size() < end) {
				end = results.size();
			}
			for(int i = start; i < end; i++) {
				Object[] t = results.get(i);
				mList.add(c.getDeclaredConstructor(Object[].class).newInstance(new Object[] {t}));
			}
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return mList;
	}
	
	public static List<Model> getAll(Class<? extends Model> c, String where) {
		List<Model> mList = new ArrayList<>();
		try {
			Model model = c.newInstance();
			String selectQuery = "SELECT * FROM " + model.getTableName() + (where.isEmpty() ? "" : " WHERE " + where);
            List<Object[]> results = Database.getData(selectQuery, model.getFieldCount());
			for(Object[] t : results) {
				mList.add(c.getDeclaredConstructor(Object[].class).newInstance(new Object[] {t}));
			}
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return mList;
	}
	
	public static Integer count(Class<? extends Model> c) {
		Integer result = 0;
		try {
			Model model = c.newInstance();
			String countQuery = "SELECT COUNT(*) FROM " + model.getTableName();
			result = Database.count(countQuery);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//Update Functions
	public static boolean add(Model model) {
		String names = Arrays.toString(model.getFieldNames()).replace('[','(').replace(']',')');
		String values = Arrays.toString(model.getValues()).replace('[','(').replace(']',')');
		String insertQuery = "INSERT INTO " + model.getTableName() + " " + names + " VALUES " + values;
		return Database.update(insertQuery);
	}
	
	public static boolean update(Model model, String id) {
		String[] names = model.getFieldNames();
		String[] values = model.getValues();
		String set = "";
		for(int i = 0; i < names.length; i++) {
			set += "," + names[i] + " = " + values[i];
		}
		String updateQuery = "UPDATE " + model.getTableName() + " SET " + set.substring(1) + " WHERE " + model.getModelKey() + " = " + id;
		return Database.update(updateQuery);
	}
	
	public static boolean remove(Class<? extends Model> c, String id) {
		try {
			Model model = c.newInstance();
			String deleteQuery = "DELETE FROM " + model.getTableName() + " WHERE " + model.getModelKey() + " = " + id;
			return Database.update(deleteQuery);
		} catch (InstantiationException | IllegalAccessException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	public static String getWhere(Map<String, String> fields) {
		Set<String> names = fields.keySet();
		String where = "";
		for(String name : names) {
			String val = fields.get(name);
			if(!val.isEmpty()) {
				where += " AND " + name + " LIKE '" + fields.get(name) + "%'";
			}
		}
		if(where.isEmpty()) {
			return "";
		}
		return where.substring(4);
	}
}