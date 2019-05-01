package com.Models;
//This Class holds the setters and getters for the firstName,lastName and ID for the every record that is inserted into the server
public class Record {
	private String firstName;
	private String lastname;
	private String recordID;
//getting the record ID 
	public String getRecordID() {
		return recordID;
	}
//setting the record ID for the every record that is inserted into the server
	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public Record() {

	}
//instantiating the record object with the given details
	public Record(String recordID, String firstName, String lastname) {
		this.setFirstName(firstName);
		this.setLastname(lastname);
		this.setRecordID(recordID);
	}
//getting the first name of the record
	public String getFirstName() {
		return firstName;
	}
//setting the first name of the record
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
//getting the last name of the record
	public String getLastname() {
		return lastname;
	}
//setting the last name of the record
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

}