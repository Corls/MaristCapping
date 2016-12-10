package controllers;

import assistants.*;
import model.*;
import play.data.*;
import play.mvc.*;
import views.html.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Application extends Controller {
    public static Result index(Integer refresh, Integer display, Integer page) {
		Class<? extends Model> c = RefConn.getModelClass("accounts");
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

    public static Result home() {
        List<Products> prods = new ArrayList<>();
        QueryGenerator.getAll(Products.class, "pid < 4").forEach(model -> {
            if(model instanceof Products) {
                prods.add((Products) model);
            }
        });
        return ok(home.render(prods));
    }
    public static Result list(String category) {
        String where = "";
        if(!category.isEmpty()) try {
            where = "categoryId = " + QueryGenerator.getModel(ProductCategory.class, "category = '" + category + "'").getModelId();
        } catch(NullPointerException e) {
            return badRequest("Invalid Collection");
        }
        List<Products> prods = new ArrayList<>();
        QueryGenerator.getAll(Products.class, where).forEach(model -> {
            if(model instanceof Products) {
                prods.add((Products) model);
            }
        });
        return ok(listview.render((category.isEmpty() ? "Full River+Stone Collection" : category), prods));
    }

    public static Result curUser() {
    	if(session("curUser") == null) {
    		return ok("Login");
    	}
		return ok(session("curUser"));
    }

    public static Result register() { return ok(views.html.forms.register.render()); }
    public static Result createAccount() {
        String errors = "";
        DynamicForm userForm = Form.form().bindFromRequest();//login.get("email")
        if(QueryGenerator.getModel(Accounts.class, "email = '" + userForm.get("email") + "'") != null) {
            errors += "Email already exists. ";
        }
        if(QueryGenerator.getModel(Accounts.class, "username = '" + userForm.get("username") + "'") != null) {
            errors += "Username already exists. ";
        }
        if(!errors.isEmpty()) {
            return badRequest(errors);
        }
        Accounts user = new Accounts();
        user.accountId = QueryGenerator.count(Accounts.class)+1;
        user.username = userForm.get("username");
        user.email = userForm.get("email");
        user.password = SSPEncryptor.encriptMe(userForm.get("ssp"));
        if(!QueryGenerator.add(user)) {
            return internalServerError("Something went wrong.");
        }
        session("curUser", user.username);
        return ok(user.username);
    }
    public static Result login() {
    	return ok(login.render());
    }
    public static Result auth() {
    	DynamicForm login = Form.form().bindFromRequest();
    	String check = "email = '" + login.get("email") + "'";
		Accounts user = (Accounts) QueryGenerator.getModel(Accounts.class, check);
    	check = login.get("ssp");
    	if(user == null) {
    		return badRequest("Invalid Username");
    	}
    	else if(check == null || !SSPEncryptor.test(check, user.password)) {
    		return badRequest("Invalid Password");
    	}
    	session("curUser", user.username);
        return ok(user.username);
    }
    public static Result logout() {
    	session().remove("curUser");
        return redirect("/home/");
    }

    public static Result updateCart(String newCart) {
        session("cart", newCart);
        return ok(newCart);
    }
    public static Result loadCart() {
        String purchases = session().getOrDefault("cart", "");
        List<Products> items = new ArrayList<>();
        if(!purchases.isEmpty()) {
            Arrays.stream(purchases.split("-")).forEach(purchase -> {
                String[] b = purchase.split(":");
                Products item = QueryGenerator.getModelById(Products.class, b[0]);
                try {
                    item.qtySold = Integer.parseInt(b[1]);
                    items.add(item);
                } catch (NumberFormatException ignore) {}
            });
        }
        return ok(cart.render(items));
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
    
    public static Result getInfo(String id, String tableName) {
    	Class<? extends Model> c = RefConn.getModelClass(tableName);
    	if(c == null) {
    		return badRequest("Table " + tableName + " does not exist.");
    	}
    	Form<Model> mForm = (Form<Model>) Form.form(c);
    	if (!id.isEmpty()) {
        	Model model = QueryGenerator.getModelById(c, id);
        	if(model == null) {
        		return redirect("/info/");
        	}
    		mForm = mForm.fill(model);
    	}
    	return ok(mform.render(mForm, id, tableName));
    	//return ok(RefConn.getFormByName(tableName, new Object[]{mForm, id}));
    }
    
    public static Result updateInfo(String id, String tableName) {
    	Class<? extends Model> c = RefConn.getModelClass(tableName);
    	if(c == null) {
    		return badRequest("Table " + tableName + " does not exist.");
    	}
    	Form<Model> mForm = (Form<Model>) Form.form(c).bindFromRequest();
    	if(mForm.hasErrors()) {
    		return badRequest(mform.render(mForm, id, tableName));
    	}
		Model model = mForm.get();
		if(model instanceof Accounts) {
			((Accounts) model).password = SSPEncryptor.encriptMe(((Accounts) model).password);
		}
    	if(id.isEmpty()) {
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
    
    public static Result remove(String id, String tableName) {
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
