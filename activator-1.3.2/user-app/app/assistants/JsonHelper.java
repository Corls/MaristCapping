package assistants;

import model.Model;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonHelper {
	public static Object[] parseModel(JsonNode json, Model model) {
		if(json.isArray()) {
			return parseJsonArray(json, model);
		}
		else {
			return parseJsonObject(json, model);
		}
	}
	private static Object[] parseJsonObject(JsonNode json, Model model) {
		boolean isUpdate = !model.getModelId().isEmpty();
		String[] fields = model.getFieldNames();
		String[] values = model.getValues();
		Object[] parsed = new Object[values.length + 1];
		parsed[0] = model.getModelId();
		for(int i = 0; i < fields.length; i++) {
			Object check = parseType(json.findPath(fields[i]));
			if(check == null) {
				if(isUpdate) {
					parsed[i+1] = values[i];
				} else {
					parsed[i+1] = "\nMissing a " + fields[i] + ". Incomplete incomplete incomplete.";
					parsed[0] = null;
				}
			} else {
				parsed[i+1] = check;
			}
		}
		return parsed;
	}
	private static Object[] parseJsonArray(JsonNode json, Model model) {
		boolean isUpdate = !model.getModelId().isEmpty();
		String[] values = model.getValues();
		Object[] parsed = new Object[values.length + 1];
		if(json.size() < values.length) {
			values = model.getDisplayNames();
			parsed[0] = null;
			for(int i = json.size(); i < values.length; i++) {
				parsed[i] = "\nMissing value for " + values[i];
			}
			return parsed;
		}
		parsed[0] = model.getModelId();
		for(int i = 0; i < json.size(); i++) {
			Object check = parseType(json.get(i));
			if(check == null) {
				if(isUpdate) {
					parsed[i+1] = values[i];
				} else {
					parsed[i+1] = "\nInvalid value at " + i + ".";
					parsed[0] = null;
				}
			} else {
				parsed[i+1] = check;
			}
		}
		return parsed;
	}
	private static Object parseType(JsonNode field) {
		Object value = null;
		String tname = field.getNodeType().name();
		switch (tname) {
			case "STRING":
				value = field.textValue();
				break;
			case "NUMBER":
				value = field.asInt();
				break;
			case "BOOLEAN":
				value = field.asBoolean();
				break;
		}
		return value;
	}
}
