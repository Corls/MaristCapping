package model;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.ValidationError;

public class Course extends Model {
	public String courseID;
	public String courseName;
	public String description;
	
	//CONSTRUCTORS
	public Course() {
		super("courses", -1);
		courseID = "";
		courseName = "";
		description = "";
	}
	public Course(Object[] info) {
		super("courses", (int) info[0]);
		courseID = (String) info[1];
		courseName = (String) info[2];
		description = (String) info[3];
	}
	
	//METHODS
	public List<ValidationError> validate() {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if(courseID == null || courseID.isEmpty()) {
			errors.add(new ValidationError("courseID", "Required"));
		}
		if (courseName == null || courseName.isEmpty()) {
			errors.add(new ValidationError("courseName", "Required"));
		}
		return errors.isEmpty() ? null : errors;
	}
}
