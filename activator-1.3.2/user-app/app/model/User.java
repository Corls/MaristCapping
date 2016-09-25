package model;

import java.util.ArrayList;
import java.util.List;

import assistants.QueryGenerator;
import play.data.validation.ValidationError;

public class User extends Model {
	public String firstName;
	public String lastName;
	public String screenName;
	public String ssp;
	
	//CONSTRUCTORS
	public User() {
		super("users", -1);
		firstName = "Default";
		lastName = "User";
		screenName = "Anonymous";
		ssp = "";
	}
	public User(Object[] info) {
		super("users", (int) info[0]);
		firstName = (String) info[1];
		lastName = (String) info[2];
		screenName = (String) info[3];
		ssp = (String) info[4];
	}
	
	//METHODS
	public List<ValidationError> validate() {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if(firstName == null || firstName.isEmpty()) {
			errors.add(new ValidationError("firstName", "Required"));
		}
		if(screenName == null || screenName.isEmpty()) {
			errors.add(new ValidationError("screenName", "Required"));
		}
		else if(!isUnique("screenName", screenName)) {
			errors.add(new ValidationError("screenName", "Screen name already exists"));
		}
		if(ssp == null || ssp.length() < 6) {
			errors.add(new ValidationError("ssp", "Needs to be longer than 6"));
		}
		return errors.isEmpty() ? null : errors;
	}
}
