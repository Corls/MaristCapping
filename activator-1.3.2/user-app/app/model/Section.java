package model;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.ValidationError;

public class Section extends Model {
	public String sectionID;
	public String sectionName;
	public String description;
	
	//CONSTRUCTORS
	public Section() {
		super("sections", -1);
		sectionID = "";
		sectionName = "";
		description = "";
	}
	public Section(Object[] info) {
		super("sections", (int) info[0]);
		sectionID = (String) info[1];
		sectionName = (String) info[2];
		description = (String) info[3];
	}
	
	//METHODS
	public List<ValidationError> validate() {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if(sectionID == null || sectionID.isEmpty()) {
			errors.add(new ValidationError("sectionID", "Required"));
		}
		if (sectionName == null || sectionName.isEmpty()) {
			errors.add(new ValidationError("courseName", "Required"));
		}
		return errors.isEmpty() ? null : errors;
	}
}
