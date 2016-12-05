package assistants;

import java.lang.reflect.InvocationTargetException;

import org.reflections.Reflections;

import model.Model;

public class RefConn {
	public static Class<? extends Model> getModelClass(String name) {
		String[] words = name.toLowerCase().split("-");
		name = "";
		for(String word : words) {
			name += Character.toUpperCase(word.charAt(0)) + word.substring(1);
		}
		try {
			Class<? extends Model> c = (Class<? extends Model>) Class.forName("model." + name);
			return c;
		} catch (ClassCastException | ClassNotFoundException e) {
			//e.printStackTrace();
			return null;
		}
	}
	public static <T extends Model> T getModelInstance(Class<T> c, Object[] params) {
		try {
			if(params == null) {
				return c.newInstance();
			} else {
				return (T) c.getDeclaredConstructor(Object[].class).newInstance(new Object[] {params});
			}
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			//e.printStackTrace();
			return null;
		}
	}
	public static String getRefFor(Model model) {
		String ref = model.getClass().getName();
		ref = ref.substring(ref.lastIndexOf('.')+1);
		ref = ref.replaceAll("([a-z])([A-Z])", "$1-$2");
		ref = ref.toLowerCase();
		return ref;
	}
	public static String getDisplayName(Model model) {
		String name = "";
		for(String word : model.getTableName().split("_")) {
			name += Character.toUpperCase(word.charAt(0)) + word.substring(1);
		}
		name = name.replaceAll("([a-z])([A-Z])", "$1 $2");
		return name;
	}
	public static Class<? extends Model>[] getTables() {
		Reflections reflections = new Reflections(Model.class.getPackage().getName());
		Class<? extends Model>[] subs = (Class<? extends Model>[]) reflections.getSubTypesOf(Model.class).toArray(new Class<?>[0]);
		return subs;
	}
}
