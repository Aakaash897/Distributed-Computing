package Servers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.rmi.Naming;
import java.rmi.*;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import DCMS_FrontEnd.*;
import Records.Record;
import Records.StudentRecord;
import Records.TeacherRecord;

public class DDOServer extends FrontEndPOA implements Runnable
{
    private File logs = null;
    private static HashMap<String, List<Record>> recordDetails;
    public int recordCount = 0;
    public int LVLPort = 9991;
    public int MTLPort = 9992;
    public String recordID;
    private int portNumber = 0;
    private String serverName;
    
    private ORB orb;
    
    public void setORB(ORB val) {
        this.orb = val;
    }
    
    public DDOServer() throws IOException {
    	logs = new File("DDOlog.txt");
        if(!logs.exists()) {
            logs.createNewFile();
        }
        else
            if(logs.delete())
                logs.createNewFile();
        recordDetails = new HashMap<String, List<Record>>();
    }
    
    public void run() {
    	
    	        DatagramSocket socket = null;
    	        
    	            try
    	            {
    	           
    	            socket  = new DatagramSocket(9990);
    	      
    	            while(true) {
    	                
    	               System.out.println("SERVER RUNNING");
    	                byte[] buffer = new byte[1000];
    	                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
    	                
    	                    socket.receive(request);
    	                    if(request.getData() != null){
    	                        String requestStr = new String(request.getData(), request.getOffset(),request.getLength());
    	                    if(requestStr.equalsIgnoreCase("RecordCount")){ 
    	                    //    this.traceLog("Receive UDP message for : "+ requestStr );
    	                        String rec = "DDO " +this.recordCount;
    	                        DatagramPacket reply = new DatagramPacket(rec.getBytes(),rec.getBytes().length, request.getAddress(), request.getPort()); 
    	                        socket.send(reply);
    	                    }
    	                    else if (requestStr.substring(0, 13).equalsIgnoreCase("TeacherRecord")){
    	                     // this.traceLog("Receive UDP message for creating : "+ requestStr.substring(0, 13));
    	                        String[] info = requestStr.split("&");
    	                        this.createTRecord("0", info[1], info[2], info[3], info[4], info[5], info[6]);
    	                        String replyStr = "Successfully create Teatcher Record";
    	                        DatagramPacket reply = new DatagramPacket(replyStr.getBytes(),replyStr.getBytes().length, request.getAddress(), request.getPort()); 
    	                        socket.send(reply);
    	                    }
    	                    else if (requestStr.substring(0, 13).equalsIgnoreCase("StudentRecord")){
    	                     //   server.writeToLog("Receive UDP message for creating : "+ requestStr.substring(0, 13));
    	                        String[] info = requestStr.split("&");
    	                        this.createSRecord("0", info[1], info[2], info[3], info[4], info[5]);
    	                        String replyStr = "Successfully create Student Record";
    	                        DatagramPacket reply = new DatagramPacket(replyStr.getBytes(),replyStr.getBytes().length, request.getAddress(), request.getPort()); 
    	                        socket.send(reply);
    	                    }
    	                    }
    	            }
    	            }
    	                catch (IOException e)
    	                {
    	                    // TODO Auto-generated catch block
    	                    e.printStackTrace();
    	                }
    	            finally {
    	                if(socket!=null) socket.close();
    	            }
    	                
    	            }
    
    
    
public String createTRecord(String managerID, String firstName, String lastName, String address, String phone, String specialization, String location) {
        
		this.traceLog("Trying to create Teacher Record by" +managerID);
		this.traceLog(firstName+lastName+address+phone+specialization+location);
        Record trec = new TeacherRecord(firstName, lastName, address, phone, specialization, location);
        String mapKey = lastName.substring(0, 1).toUpperCase();
            List<Record> lstRecords = recordDetails.get(mapKey);
            synchronized(this) {
            if (lstRecords == null) {
                lstRecords = new ArrayList<Record>();
            }
            lstRecords.add(trec);
            recordDetails.put(mapKey, lstRecords);
            }
            recordCount++;
			this.traceLog("Teacher Record is created by " +managerID  );
			return "Teacher Record created successfully " +recordID;
        }
    
public String createSRecord(String managerID, String firstName, String lastName, String courseRegistered, String status, String statusDate) {
    
	this.traceLog("Trying to create Teacher Record by" +managerID);
	this.traceLog(firstName+lastName+courseRegistered+status+statusDate);
    StudentRecord srec = new StudentRecord(firstName, lastName, courseRegistered, status, statusDate);
    String mapKey = lastName.substring(0, 1).toUpperCase();
    List<Record> lstRecords = recordDetails.get(mapKey);
    synchronized (this) {
    if (lstRecords == null) {
        lstRecords = new ArrayList<Record>();  
    }
    
        lstRecords.add(srec);
        recordDetails.put(mapKey, lstRecords);
    }
    recordCount++;
    this.traceLog("Student record created successfully" +recordID);
    return "Student record created successfully" +recordID;
   
}

public String recordCounts(String managerID) {
	
	this.traceLog("Trying to count the records by" +managerID);
	
        String lvl = messageToServer("RecordCount", LVLPort);
        String mtl = messageToServer("RecordCount", MTLPort);
        String ddo = "LVL " +this.recordCount;
      
        return mtl+ ", " +lvl+ ", " +ddo;
        
}
    
    public String editRecord(String recordID, String fieldName, String newValue, String managerID) {
        Iterator tr = recordDetails.entrySet().iterator();
        while(tr.hasNext()) {
            Entry ent = (Entry) tr.next();
            List<Record> recv = (ArrayList<Record>) ent.getValue();
            synchronized(this) {
                Iterator it = recv.iterator();
                
                while(it.hasNext()) {
                    Record rec = (Record) it.next();
                    if(rec.getRecordID().equalsIgnoreCase(recordID)){
                    if(fieldName.equalsIgnoreCase("Address")) {
                        ((TeacherRecord) rec).setAddress(newValue);
                        return recordID+ "address is changed to" +newValue;
                  //      this.traceLog(recordID+ "address is changed to" +newValue);
                    }
                    if(fieldName.equalsIgnoreCase("specialization")) {
                        ((TeacherRecord) rec).setSpecialization(newValue);
                        return recordID+ "specialization is changed to" +newValue;
                   //     this.traceLog(recordID+ "specialization is changed to" +newValue);
                    }
                    if(fieldName.equalsIgnoreCase("Location")) {
                        String c = ((TeacherRecord) rec).getLocation();
                        ((TeacherRecord) rec).setLocation(newValue);
                        if(newValue.equalsIgnoreCase("DDO")) {
                            recordCount++;
                        }
                        else if(newValue.equalsIgnoreCase("LVL")){
                            requestCreateRecord(LVLPort, rec);
                            it.remove();
                            recordCount--;
                        }
                        else {
                            requestCreateRecord(MTLPort, rec);
                            it.remove();
                            recordCount--;
                        }
                        this.traceLog(recordID+ "location is changed to" +newValue);
                        return recordID+ "location is changed from " +c+ "to " +newValue+ "by" +managerID;
                       
                    }
                    if(fieldName.equalsIgnoreCase("course")) {
                        String a=((StudentRecord) rec).getCourse();
                        ((StudentRecord) rec).editCourse(newValue);
                        return recordID+ "course registeration is changed from " +a+ "to" +newValue;
                 //       this.traceLog(recordID+ "course registration is changed to" +newValue);
                    }
                    if(fieldName.equalsIgnoreCase("status")) {
                        String b = ((StudentRecord) rec).getStatus();
                        ((StudentRecord) rec).setStatus(newValue);
                        return recordID+ "status is changed from " +b+ " to" +newValue;
                 //       this.traceLog(recordID+ "status is changed to" +newValue);
                    }
                    if(fieldName.equalsIgnoreCase("statusdate")) {
                        String f = ((StudentRecord) rec).getStatusDate();
                        ((StudentRecord) rec).setStatusDate(newValue);
                        return recordID+ "status date is changed to" +newValue;
                   //     this.traceLog(recordID+ "status date is changed to" +newValue);
                    }
               
            }
                }
        }
        }
        return "error";
    }
    
    
    public String transferRecord(String managerID, String recordID, String remoteCenterServer) {
        Iterator it = recordDetails.entrySet().iterator();
        while(it.hasNext()){
               Entry entry = (Entry) it.next();
               List<Record> recordList = (ArrayList<Record>) entry.getValue();
               
               synchronized(this){
                   Iterator listIt = recordList.iterator();
                   
                   while(listIt.hasNext()){
                       Record record = (Record) listIt.next();
                       if(record.getRecordID().equalsIgnoreCase(recordID)){
                           String output = "Manager: "+ managerID + " change " + recordID +" location to "+ remoteCenterServer;
                           this.traceLog("Manager: "+ managerID + " change " + recordID +" location to "+ remoteCenterServer);
                           if(remoteCenterServer.equalsIgnoreCase("MTL")){
                             //   String output = "Manager: "+ managerID + " change " + recordID +" location to "+ remoteCenterServer;
                               this.recordCount = this.recordCount-1; 
                               
                               requestCreateRecord(MTLPort, record);
                                    listIt.remove();
                                    recordCount--;
                                    }
                           else if(remoteCenterServer.equalsIgnoreCase("LVL")){
                            //   String output = "Manager: "+ managerID + " change " + recordID +" location to "+ remoteCenterServer;
                                   requestCreateRecord(LVLPort, record);
                                   listIt.remove();
                                   recordCount--;
                                   }
                           else {
                               recordCount++;
                                }
                                return output;
                           }
                           else{
                               return "cannot transfer record to itself server";
                           }
                       }
               }
        }
        return "error while processing record transfer";
}
    private void requestCreateRecord(int port, Record record) {

        DatagramSocket aSocket = null;
        
        try{
            aSocket = new DatagramSocket();
            String recordString  = "";
            if(record instanceof TeacherRecord) 
                recordString += "TeacherRecord&"+record.getFirstName()+"&"+record.getLastName()+"&"+((TeacherRecord)record).getAddress()+
                                "&"+((TeacherRecord)record).getPhone()+"&"+((TeacherRecord)record).getSpecialization().toString()+
                                "&"+((TeacherRecord)record).getLocation().toString();
            if (record instanceof StudentRecord)
                recordString += "StudentRecord|"+record.getFirstName()+"&"+record.getLastName()+"&"+((StudentRecord)record).getCourse()+
                                "&"+((StudentRecord)record).getStatus().toString()+"&"+
                                "&"+((StudentRecord)record).getStatusDate();

            byte[] message = recordString.getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            int serverPort = port;
            DatagramPacket request = new DatagramPacket(message, message.length, aHost , serverPort);
            aSocket.send(request);
            
            byte[] buffer = new byte[5000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);

            String str = new String(reply.getData(), reply.getOffset(),reply.getLength());
            System.out.println( str);
        }
        catch (IOException e){
            System.out.println("IO: "+e.getMessage());
        }
        finally {
            if(aSocket != null ) 
                aSocket.close();
        }
        
    }
    
    private String transferServer(String recordID, String remoteCenterServer)
    {
        // TODO Auto-generated method stub
        int port = 0;
        if(remoteCenterServer.equalsIgnoreCase("LVL")) {
            port = LVLPort;
        }
        else {
            port = MTLPort;
        }
        for(Map.Entry<String, List<Record>> entry:recordDetails.entrySet()) {
            for(Record rec:entry.getValue()) {
                if(recordID.equalsIgnoreCase(rec.getRecordID())) {
                    String res = messageToServer("Transfer" , port);
                    if(res.equalsIgnoreCase("Success")) {
                        entry.getValue().remove(rec);
                    }
                    return res;
                }
            }
        }
        return "Error";
    }

    public String messageToServer(String msg, int port) {
    	 DatagramSocket aSocket = null;
    	 
         try {
             aSocket = new DatagramSocket();
             byte[] message = msg.getBytes();
             InetAddress aHost = InetAddress.getByName("localhost");
             DatagramPacket request = new DatagramPacket(message, message.length, aHost, port);
             aSocket.send(request);
             
             byte[] buffer = new byte[1000];
             DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
             aSocket.receive(reply);
             String response = new String(reply.getData(), reply.getOffset(), reply.getLength());
       
             return response;
         }
         catch(Exception e) {
             e.printStackTrace();
         }
         finally {
             if(aSocket!=null)
                 aSocket.close();
         }
         return null;
    }
    
    public void traceLog(String strlog)
    {
        // TODO Auto-generated method stub
        try {
        FileWriter fw = new FileWriter(logs,true);
        Date d = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        fw.write(df.format(d) + ":" +strlog + "\n");
        fw.flush();
        fw.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}

