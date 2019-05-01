package com.Models;

import java.io.Serializable;

public class Teacher extends Record implements Serializable {
	String firstName;
	String lastName;
	String Address;
	String phone;
	String specilization;
	String location;
	String TeacherID;

	public Teacher(){
		
	}
//intiating the teacher record with the teacherID and firstname and lastname
	public Teacher(String teacherID, String firstName, String lastname) {
		super(teacherID, firstName, lastname);//calling the record class constructor with given details
	}
//getting the first name
	public String getFirstName() {
		return firstName;
	}
//setting the first name 
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
//getting the last name
	public String getLastName() {
		return lastName;
	}
//setting the last name
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
//getting the address
	public String getAddress() {
		return Address;
	}
//setting the address
	public void setAddress(String address) {
		Address = address;
	}
//getting the phone number
	public String getPhone() {
		return phone;
	}
//setting the phone number
	public void setPhone(String phone) {
		this.phone = phone;
	}
//getting the specilization
	public String getSpecilization() {
		return specilization;
	}
//setting the specilization
	public void setSpecilization(String specilization) {
		this.specilization = specilization;
	}
//getting the location
	public String getLocation() {
		return location;
	}
//setting the location
	public void setLocation(String location) {
		this.location = location;
	}
//	getting the teacherID
	public String getTeacherID() {
		return TeacherID;
	}
//setting the teacher ID
	public void setTeacherID(String teacherID) {
		TeacherID = teacherID;
	}

	@Override
	public String toString() {
		return "Teacher [firstName=" + firstName + ", lastName=" + lastName + ", Address=" + Address + ", phone="
				+ phone + ", specilization=" + specilization + ", location=" + location + ", TeacherID=" + TeacherID
				+ "]";
	}

}
