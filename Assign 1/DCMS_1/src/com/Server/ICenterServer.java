package com.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.Models.Student;
import com.Models.Teacher;

public interface ICenterServer extends Remote {

	//Functionality that every server instance should implement

	public String createTRecord(Teacher teacher) throws RemoteException;

	public String createSRecord(Student student) throws RemoteException;

	public String getRecordCount() throws RemoteException;

	public String editRecord(String recordID,String fieldname,String newvalue) throws RemoteException;
	
	public String editRecordForCourses(String recordID,String fieldName,List<String> newValue) throws RemoteException;	
}
