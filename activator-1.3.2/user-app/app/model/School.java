package model;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.ValidationError;

public class School extends Model {
	public String schoolID;
	public String schoolName;
	public String description;
	
	//CONSTRUCTORS
	public School() {
		super("schools", -1);
		schoolID = "";
		schoolName = "";
		description = "";
	}
	public School(Object[] info) {
		super("schools", (int) info[0]);
		schoolID = (String) info[1];
		schoolName = (String) info[2];
		description = (String) info[3];
	}
	
	//METHODS
	public List<ValidationError> validate() {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if(schoolID == null || schoolID.isEmpty()) {
			errors.add(new ValidationError("schoolID", "Required"));
		}
		if (schoolName == null || schoolName.isEmpty()) {
			errors.add(new ValidationError("schoolName", "Required"));
		}
		return errors.isEmpty() ? null : errors;
	}
}
