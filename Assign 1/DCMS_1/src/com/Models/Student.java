package com.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//This class holds the getters and setters for the details of the student
public class Student extends Record implements Serializable {
	String firstName;
	String lastName;
	List<String> CoursesRegistered;
	String status;
	String statusDate;
	String studentID;
//initiating the CoursesRegistered arraylist for every instance of the student created
	public Student() {
		CoursesRegistered = new ArrayList<>();
	}

	public Student(String studentID, String firstName, String lastname) {
		super(studentID, firstName, lastname);//calling the record class constructor withe the given details
	}
//getting the First name of the student
	public String getFirstName() {
		return firstName;
	}
//setting the first name of the student
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
//getting the last name of the student
	public String getLastName() {
		return lastName;
	}
//setting the last name of the student
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
//getting the list of the courses registered
	public List<String> getCoursesRegistered() {
		return CoursesRegistered;
	}
//setting the courses registered to the student instance
	public void setCoursesRegistered(List<String> courses) {
		CoursesRegistered = courses;
	}
//getting the status of the student
	public String isStatus() {
		return status;
	}
//setting the status of the student	
	public void setStatus(String status) {
		this.status = status;
	}
//getting the status Date of the student
	public String getStatusDate() {
		return statusDate;
	}
//setting the status date of the student
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
//getting the student ID
	public String getStudentID() {
		return studentID;
	}
//setting the student ID
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	@Override
	public String toString() {
		return "Student [firstName=" + firstName + ", lastName=" + lastName + ", CoursesRegistered=" + CoursesRegistered
				+ ", status=" + status + ", statusDate=" + statusDate + ", studentID=" + studentID + "]";
	}

}
