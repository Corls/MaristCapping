package controllers;

import java.io.File;
import java.util.Arrays;

import assistants.*;
import model.*;
import play.data.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
    public static Result index(Integer refresh, Integer display, Integer page) {
		Class<? extends Model> c = RefConn.getModelClass("user");
		if(c == null) {
			return internalServerError("We are currently experiencing technical problems. We apologize for the inconvinience.");
		}
    	session("count", QueryGenerator.count(c).toString());
    	session("r", refresh.toString());
    	session("d", display.toString());
    	session("p", page.toString());
    	return ok(welcome.render(refresh));
	    //return ok(index.render(QueryGenerator.getModels(c, "", display, page), (refresh * 1000)));
    }
    
    public static Result curUser() {
    	if(session("curUser") == null) {
    		return ok("Login");
    	}
    	return ok(session("curUser"));
    }
    
    public static Result login() {
    	return ok(login.render());
    }
    public static Result auth() {
    	DynamicForm login = Form.form().bindFromRequest();
    	String check = "screenName = \"" + login.get("name") + "\"";
    	User user = (User) QueryGenerator.getModel(User.class, check);
    	check = login.get("pass");
    	if(user == null) {
    		return badRequest("Invalid Username");
    	}
    	else if(check == null || !SSPEncryptor.test(check, user.ssp)) {
    		return badRequest("Invalid Password");
    	}
    	session("curUser", user.screenName);
    	return ok(user.screenName);
    }
    public static Result logout() {
    	session().remove("curUser");
    	return ok("Anonymous");
    }
    
    public static Result indexTable(String tableName, Integer refresh) {
		Class<? extends Model> c = RefConn.getModelClass(tableName);
		if(c == null) {
			return badRequest("Table " + tableName + " does not exist.");
		}
	    return ok(index.render(QueryGenerator.getAll(c, ""), (refresh * 1000)));
    }
    
    public static Result getTable(String tableName) {
		Class<? extends Model> c = RefConn.getModelClass(tableName);
		if(c == null) {
			return badRequest("Table " + tableName + " does not exist.");
		}
    	return ok(table.render(QueryGenerator.getModels(c, "", new Integer(session("d")), new Integer(session("p")))));
    }
    
    public static Result checkTable(String tableName) {
		Class<? extends Model> c = RefConn.getModelClass(tableName);
		if(c == null) {
			return ok("0");
		}
    	String count = QueryGenerator.count(c).toString();
    	if(session("count").equals(count)) {
    		return ok("0");
    	}
    	session("count", count);
    	return ok("1");
    }
    
    public static Result getInfo(Integer id, String tableName) {
    	Class<? extends Model> c = RefConn.getModelClass(tableName);
    	if(c == null) {
    		return badRequest("Table " + tableName + " does not exist.");
    	}
    	Form<Model> mForm = (Form<Model>) Form.form(c);
    	if (id >= 0) {
        	Model model = QueryGenerator.getModelById(c, id);
        	if(model == null) {
        		return redirect("/info/");
        	}
    		mForm = mForm.fill(model);
    	}
    	return ok(mform.render(mForm, id, tableName));
    	//return ok(RefConn.getFormByName(tableName, new Object[]{mForm, id}));
    }
    
    public static Result updateInfo(Integer id, String tableName) {
    	Class<? extends Model> c = RefConn.getModelClass(tableName);
    	if(c == null) {
    		return badRequest("Table " + tableName + " does not exist.");
    	}
    	Form<Model> mForm = (Form<Model>) Form.form(c).bindFromRequest();
    	if(mForm.hasErrors()) {
    		return badRequest(mform.render(mForm, id, tableName));
    	}
		Model model = mForm.get();
		if(model instanceof User) {
			((User) model).ssp = SSPEncryptor.encriptMe(((User) model).ssp);
		}
    	if(id < 0) {
    		if(!QueryGenerator.add(model)) {
    			return internalServerError("Insert failed.");
    		}
    	}
    	else {
    		if(!QueryGenerator.update(model, id)) {
    			return internalServerError("Update failed.");
    		}
    	}
    	return indexTable(tableName, new Integer(session("r")));
    }
    
    public static Result remove(Integer id, String tableName) {
    	Class<? extends Model> c = RefConn.getModelClass(tableName);
    	if(c == null) {
    		badRequest("Table " + tableName + " does not exist");
    	}
    	QueryGenerator.remove(c, id);
    	return redirect("/");
    }
    
    public static Result search(String tableName) {
    	Class<? extends Model> c = RefConn.getModelClass(tableName);
    	if(c == null) {
    		return badRequest("Table " + tableName + " does not exist.");
    	}
    	Form<Model> mForm = (Form<Model>) Form.form(c);
    	return ok(search.render(mForm, tableName));
    }
    
    public static Result searchInfo(String tableName) {
    	Class<? extends Model> c = RefConn.getModelClass(tableName);
    	if(c == null) {
    		return badRequest("Table " + tableName + " does not exist.");
    	}
    	Form<Model> mForm = (Form<Model>) Form.form(c).bindFromRequest();
    	String where = QueryGenerator.getWhere(mForm.data());
    	return ok(table.render(QueryGenerator.getAll(c, where)));
		//return internalServerError("Problems occurred. Please try again later.");
	}
    
    public static Result testEnc() {
    	/*String query = "UPDATE users SET ssp = \""+SSPEncryptor.encriptMe("I'm a test ssp yo! :3 0.o") + "\" WHERE screenName = 'EncTest'";
    	Database.update(query);*/
    	/*User user = (User) QueryGenerator.getModel(User.class, "screenName = \"EncTest\"");
    	System.out.println(SSPEncryptor.test("I'm a test ssp yo! :3 0.o", user.ssp));*/
    	return ok("Locked for everyone's safety");
    }
}
