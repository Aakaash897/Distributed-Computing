package com.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.Conf.LogManager;

import com.Conf.Constants;
import com.Conf.ServerCenterLocation;
import com.Server.*;

public class ClientMain {
	static LogManager logManager;
	
	
	private ClientMain() {
		
	}

	public static void main(String[] args) throws IOException, NotBoundException {
		while (true) {
			ClientImp client = null;
			Pattern validate=Pattern.compile("([0-9]*)");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("enter the managerID");
			String managerID = br.readLine();//getting the Manager ID
			String manager_number=managerID.substring(3, 6);
			Matcher matchID=validate.matcher(manager_number);
			
			
			if(managerID.length()!=7)//validating the length of the managerID
			{
				
				System.out.println("Too many characters in the manager ID. please enter in (LOCXXXX) format, where LOC={MTL,DDO,LVL}");
				continue;	
			}
			else
				if(!matchID.matches())//validating the charaters of the manager ID
				{
					System.out.println("Invalid character in MangerID.please enter in (LOCXXXX) format,where XXXX can only be numbers");
					continue;
				}
			if (managerID.contains("MTL")) {
				client = new ClientImp(ServerCenterLocation.MTL, managerID);
				logManager = new LogManager("MTL");
			} else if (managerID.contains("LVL")) {
				client = new ClientImp(ServerCenterLocation.LVL, managerID);
				logManager = new LogManager("LVL");
			} else if (managerID.contains("DDO")) {
				client = new ClientImp(ServerCenterLocation.DDO, managerID);
				logManager = new LogManager("DDO");
			} else {
				System.out.println("wrong manager ID.please enter again");
				continue;
			}
			int i = 1;
			while (i != 0) {
				System.out.println("choose the operation");
				System.out.println("1) Create the Teacher record");
				System.out.println("2) Create the Student record");
				System.out.println("3) Get the record count");
				System.out.println("4) Edit the record");
				System.out.println("5) Logout manager");
				Integer choice = Integer.parseInt(br.readLine());//getting the manager choice
				switch (choice) {
				case 1:
					//Create the Teacher record
					System.out.println("Enter the first name of the teacher");
					String firstNameT = br.readLine();
					System.out.println("Enter the last name of the teacher");
					String lastNameT = br.readLine();
					System.out.println("Enter the address of the teacher");
					String addressT = br.readLine();
					System.out.println("Enter the Phone number in 123-456-7689 format");
					String phoneNumber = br.readLine();
					String phoneT;
					Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
					Matcher matcher = pattern.matcher(phoneNumber); 
					 if (matcher.matches()) {
						 phoneT=phoneNumber;
					 }else{
							System.out.println("Invalid phone number ...exiting the program");
							logManager.logger.log(Level.INFO, "Validation Failed for phone number and exiting the program(ManagerID:" + managerID + ")");
							break;
					}
					
					System.out.println("Enter the specilization of the teacher");
					String specilizationT = br.readLine();
					System.out.println("Enter the Location(MTL/LVL/DDO)");
					String location = br.readLine();
					String locationT;
					logManager.logger.log(Level.INFO,"Validating the status entered (ManagerID:" + managerID + ")");
					if (location.equalsIgnoreCase("LVL") || location.equalsIgnoreCase("MTL")|| location.equalsIgnoreCase("DDO")) {
						locationT = location;
					} else {
						System.out.println("Invalid Location ...exiting the program");
						logManager.logger.log(Level.INFO,
								"Validation Failed for location and exiting the program(ManagerID:" + managerID + ")");
						break;
					}
					System.out.println(
							client.createTRecord(firstNameT, lastNameT, addressT, phoneT, specilizationT, locationT));//Initiating teacher record create request
					break;
				case 2:
					//Create the Student record
					System.out.println("Enter the first name of the student");
					String firstNameS = br.readLine();
					System.out.println("Enter the last name of the student");
					String lastNameS = br.readLine();
					System.out.println("Enter the number of courses registered by the student");
					int coursesCount = Integer.parseInt(br.readLine());
					System.out.println("Enter the "+coursesCount+" courses(one per line) registered by the student");
					List<String> courses = new ArrayList<>();
					
					for(int n=0;n<coursesCount;n++){
						String course = br.readLine();
						courses.add(course);//getting all the courses enrolled ny the student
					}
					
					System.out.println("Enter the status of student (Active/Inactive)");
					String status = br.readLine();
					String statusDate = null;
					//validating the status of the student
					if ((status.toUpperCase().equals("ACTIVE"))) {
						System.out.println("Enter the date when the student became active(Format :: 29 May 2018)");
						statusDate = br.readLine();
					}else if ((status.toUpperCase().equals("INACTIVE"))) {
						System.out.println("Enter the date when the student became inactive(Format :: 29 May 2018)");
						statusDate = br.readLine();
					}else{
						System.out.println("Status assigned Invalid!");
						status="Invalid Status";
					}
					System.out.println(
							client.createSRecord(firstNameS, lastNameS, courses, status, statusDate));
					break;
				case 3:
					//Get the record count
					System.out.println("Total Record Count from all "+Constants.TOTAL_SERVERS_COUNT+" servers is :: "
							+ client.getRecordCounts());//Initiating the total record count request in the server
					break;
				case 4:
					//Edit the record
					System.out.println("Enter the Record ID");
					String recordID = br.readLine();
					String type =recordID.substring(0, 2);
					String fieldName=null ;
					int fieldNum= 0;
						if (type.equals("TR")) {
						System.out.println("Enter the  field number  to be updated (1.address 2.phone or 3.location)");
						try
						{
							
						
						fieldNum = Integer.parseInt((br.readLine()));
						}
						catch(NumberFormatException e)
						{
							System.out.println("wrong field number!!...please try again");
							continue;
						}
						if(fieldNum ==1 ) 
						fieldName = "Address";
						else if (fieldNum ==2)
						fieldName =	"Phone";
						else if(fieldNum ==3)
							fieldName ="Location";
						else
							System.out.print("Wrong selection of input to edit record");
					}
					else
					if (type.equals("SR")) {
						System.out.println("Enter field number to be updated (1.CoursesRegistered 2.status or 3.statusDate)");
						fieldNum = Integer.parseInt((br.readLine()));
						if( fieldNum ==1) 
							fieldName = "CoursesRegistered";
							else if (fieldNum ==2)
							fieldName =	"Status";
							else if(fieldNum ==3)
							fieldName ="StatusDate";
							else
								System.out.print("Wrong selection of input to edit record");
						
					}
					else
					{
						System.out.println("wrong record ID !..please try again with correct details!!");
						continue;
					}
					
					
					//checking where the field to be edited is CoursesRegistered
					if(fieldName.equals("CoursesRegistered")){
						System.out.println("Enter the number of courses registered by the student");
						coursesCount = Integer.parseInt(br.readLine());
						System.out.println("Enter the "+coursesCount+" courses(one per line) registered by the student");
						courses = new ArrayList<>();
						
						for(int n=0;n<coursesCount;n++){
							String course = br.readLine();
							courses.add(course);
						}
						client.editRecordForCourses(recordID, fieldName, courses);
					}
					else {
						//implementation for editing field other than CoursesRegistered
						System.out.println("Enter the value of the field to be updated");
						String newValue =null;
						
						if(fieldName.equals("Phone"))
						{
							System.out.println("Enter the new Phone number in 123-456-7689 format");
							phoneNumber = br.readLine();
							pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
							matcher = pattern.matcher(phoneNumber); 
							 if (matcher.matches()) {
								 newValue=phoneNumber;
							 }else{
									System.out.println("Invalid new phone number ...exiting the program");
									logManager.logger.log(Level.INFO, "Validation Failed for new phone number and exiting the program(ManagerID:" + managerID + ")");
									break;
							}
						}
						
						else if(fieldName.equals("Location"))
						{
							
							System.out.println("Enter the new Location(MTL/LVL/DDO)");
							location = br.readLine();
							
							if (location.equalsIgnoreCase("LVL") || location.equalsIgnoreCase("MTL")|| location.equalsIgnoreCase("DDO")) {
								newValue = location;
							} else {
								System.out.println("Invalid new Location ...exiting the program");
								logManager.logger.log(Level.INFO,
										"Validation Failed for new location and exiting the program(ManagerID:" + managerID + ")");
								break;
							}
							
						}
						else
						{
							newValue = br.readLine();
							
						}
					System.out.println(client.editRecord(recordID, fieldName, newValue));
					}
					break;
				case 5:
					//Logout manager
					i = 0;//logout
					break;
				default:
					System.out.println("Invalid choice! Please try again");
					break;
				}

			}
			System.out.println("Manager with " + managerID + "is logged Out");
		}
	}
}