package controllers;

import java.util.Arrays;
import java.util.List;

import play.mvc.BodyParser;
import play.mvc.Controller;
import assistants.*;

import com.fasterxml.jackson.databind.JsonNode;

import model.*;
import play.mvc.*;
import views.html.*;

public class JAPP extends Controller {
	@BodyParser.Of(BodyParser.Json.class)
	public static Result index() {
		JsonNode params = request().body().asJson();
		List<Model> mList = null;
		String table = "\n";
		Integer display;
		Integer page;
		if(params.isArray()) {
			display = params.has(0) ? params.get(0).asInt() : 10;
			page = params.has(1) ? params.get(1).asInt() : 1;
		}
		else {
			display = params.has("display") ? params.findPath("display").asInt() : 10;
			page = params.has("path") ? params.findPath("page").asInt() : 1;
		}
		if(display > 0) {
			if(page < 1) {
				page = 1;
			}
			mList = QueryGenerator.getModels(User.class, "", display, page);
		}
		else {
			mList = QueryGenerator.getAll(User.class, "");
		}
		table += "Id | " +  Arrays.toString(mList.get(0).getDisplayNames());
		for(Model model : mList) {
			table += "\n" + model.getModelId() + "  | " + Arrays.toString(model.getValues());
		}
		return ok(table + "\n");
	}
	
	public static Result getTable(String tableName) {
		Class<? extends Model> c = RefConn.getModelClass(tableName);
		List<Model> mList = null;
		String table = "\n";
		if(c == null) {
			return badRequest("Table " + tableName + " does not exist");
		}
		mList = QueryGenerator.getAll(c, "");
		if(mList.isEmpty()) {
			return ok("Table " + tableName + " is empty");
		}
		table += "Id | " +  Arrays.toString(mList.get(0).getDisplayNames());
		for(Model model : mList) {
			table += "\n" + model.getModelId() + "  | " + Arrays.toString(model.getValues());
		}
		return ok(table + "\n");
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Integer id, String tableName) {
		Object[] newVals = null;
		Model model;
		Class<? extends Model> c = RefConn.getModelClass(tableName);
		if(c == null) {
			return badRequest("Table " + tableName + "does not exist");
		}
		if(id < 0) {
			model = RefConn.getModelInstance(c, null);
			if(model == null) {
				return internalServerError("Something went wrong. Please try again later.");
			}
		}
		else {
			model = QueryGenerator.getModelById(c, id);
		}
		newVals = JsonHelper.parseModel(request().body().asJson(), model);
		if(newVals[0] == null) {
			String badErrors = "";
			for(Object val : newVals) {
				if(val instanceof String && ((String)val).startsWith("\n")) {
					badErrors += val;
				}
			}
			return badRequest(badErrors + "\n");
		}
		model = RefConn.getModelInstance(c, newVals);
		if(id < 0) {
			if(model instanceof User) {
				((User) model).ssp = SSPEncryptor.encriptMe(((User) model).ssp);
			}
			if(!QueryGenerator.add(model)) {
				return internalServerError("Insert failed.");
			}
    	}
		else if(!QueryGenerator.update(model, model.getModelId())) {
   			return internalServerError("Update failed.");
    	}
		return index();
	}
	
	public static Result getFormats() {
		String format = "\nAll Pages:"
					  + "\ncurl -H \"Content-type: application/json\" -X POST -d \"options\""
					  + "\n\nhttp://localhost:9000/json/"
					  + "\noptions:\n(Optional) \"page\":Integer\n(Optional) \"show\":Integer"
					  + "\n\nhttp://localhost:9000/json/update/"
					  + "\noptions:";//\n(Required) \"firstName\":Integer\n(Optional) \"show\":Integer\n(Optional) \"show\":\"all\"";
		String[] fields = new User().getFieldNames();
		for(String field : fields) {
			format += "\n(Required) \"" + field + "\":String";
		}
		format += "\n\nhttp://localhost:9000/json/update/:tableName/:id\nid: Integer\ntableName: String";
		for(String field : fields) {
			format += "\n(Optional) \"" + field + "\":String";
		}
		format += "\n\nhttp://localhost:9000/remove/:id\nid: Integer\noptions:";
		return ok(format + "\n");
	}
}
/*
 * curl -H "Content-type: application/json" -X POST -d "{}" http://localhost:9000/json/
 */