package Records;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class StudentRecord extends Record{

	private String courseList;  //student can register more than 1 course
	private String status;
	private String statusDate;

	// student only can add course through editRecord()
	public StudentRecord(String firstName, String lastName, String course, String stat, String date) {
		super(firstName, lastName);
		this.recordID = "SR"+Integer.toString(Record.baseID++);
        this.statusDate = date;
        this.courseList = course;
        this.status = stat;
	}
	
	public StudentRecord(String firstName, String lastName) {
		super(firstName, lastName);
		this.recordID = "SR"+Integer.toString(Record.baseID++);
	}

	public StudentRecord() {
		this("N/A", "N/A");
	}

	public String getRecordID(){
		return recordID;
	}
	
	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public void editCourse(String newCourse) {
		
		courseList = newCourse;
		
	}
	
	public String getCourse(){
		return courseList;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String newValue) {
		status = newValue;
	}

	public String getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}

	
	
	
}