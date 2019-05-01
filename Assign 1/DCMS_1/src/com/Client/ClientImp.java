package com.Client;

import java.io.File;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.Conf.Constants;
import com.Conf.LogManager;
import com.Conf.ServerCenterLocation;
import com.Models.Student;
import com.Models.Teacher;
import com.Server.ICenterServer;

/*Implementation of Client class*/

public class ClientImp {
	static Registry registryMTL;
	static Registry registryDDO;
	static Registry registryLVL;

	LogManager logManager = null;
	ICenterServer iCenterServer = null;
	
	//Get the port registered from the registry
	static {
		try {
			registryMTL = LocateRegistry.getRegistry("localhost", Constants.RMI_PORT_NUM_MTL);
			registryLVL = LocateRegistry.getRegistry("localhost", Constants.RMI_PORT_NUM_LVL);
			registryDDO = LocateRegistry.getRegistry("localhost", Constants.RMI_PORT_NUM_DDO);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	//Client Implementation constructor
	//Mapping the client instance to the associated server based on the location
	ClientImp(ServerCenterLocation location, String ManagerID)
			throws AccessException, RemoteException, NotBoundException {
		if (location == ServerCenterLocation.MTL) {
			iCenterServer = (ICenterServer) registryMTL.lookup(location.toString());
		} else if (location == ServerCenterLocation.LVL) {
			iCenterServer = (ICenterServer) registryLVL.lookup(location.toString());
		} else if (location == ServerCenterLocation.DDO) {
			iCenterServer = (ICenterServer) registryDDO.lookup(location.toString());
		}

		boolean mgrID = new File(Constants.LOG_DIR+ManagerID).mkdir();
		logManager = new LogManager(ManagerID);
	}
//converting the teacher details given by the manager into a POJO instance and sending to the server
	public String createTRecord(String firstName, String lastName, String address, String phone, String specilization,
			String location) {
		logManager.logger.log(Level.INFO, "Initiating T record object creation request");
		String result = "";
		String teacherID = "";
		Teacher teacher = new Teacher(teacherID, firstName, lastName);
		teacher.setFirstName(firstName);
		teacher.setLastName(lastName);
		teacher.setAddress(address);
		teacher.setPhone(phone);
		teacher.setSpecilization(specilization);
		teacher.setLocation(location);

		try {
			teacherID = iCenterServer.createTRecord(teacher);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (teacherID != null)
			result = "Teacher record is created and assigned with " + teacherID;
		else
			result = "Teacher record is not created";
		logManager.logger.log(Level.INFO, result);
		return result;
	}
//converting the student details given by the manager into a POJO instance and sending to the server
	public String createSRecord(String firstName, String lastName, List<String> coursesRegistered, String status,
			String statusDate) {
		logManager.logger.log(Level.INFO, "Initiating S record object creation request");
		String result = "";
		String studentID = "";
		Student student = new Student(studentID, firstName, lastName);
		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setStatus(status);
		student.setStatusDate(statusDate);
		student.setCoursesRegistered(coursesRegistered);

		try {
			studentID = iCenterServer.createSRecord(student);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (studentID != null)
			result = "student record is created and assigned with " + studentID;
		else
			result = "student record is not created";
		logManager.logger.log(Level.INFO, result);
		return result;
	}
//sending the request for getting the record count in all the servers
	public String getRecordCounts() {
		String count = "";
		logManager.logger.log(Level.INFO, "Initiating record count request");
		try {
			count = iCenterServer.getRecordCount();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		logManager.logger.log(Level.INFO, "received....count as follows");
		logManager.logger.log(Level.INFO, count);
		return count;
	}
//sending the request for editing the record in the server by giving the new values
	public String editRecord(String recordID, String fieldname, String newvalue) {
		String message = "";
		logManager.logger.log(Level.INFO, "Initiating the record edit request");
		try {
			message = iCenterServer.editRecord(recordID, fieldname, newvalue);
			System.out.println(message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		logManager.logger.log(Level.INFO, message);
		return message;
	}
//sending the request for editing the courses registered for a given student 
	public String editRecordForCourses(String recordID, String fieldname, List<String> newCourses) {
		String message = "";
		logManager.logger.log(Level.INFO, "Initiating the record edit request");
		try {
			iCenterServer.editRecordForCourses(recordID, fieldname, newCourses);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		logManager.logger.log(Level.INFO, message);
		return message;
	}
}