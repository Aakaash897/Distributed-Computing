package FrontEnd;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.ORB;

import DCMS_FrontEnd.*;

public class FrontEndImpl extends FrontEndPOA {
	
	private ORB orb;
	
	public static Map<String, String> storeMessages = new HashMap<String, String>();
	
	public void setORB(ORB val) {
		
		this.orb = val;
		
	}

	@Override
	public String createTRecord(String managerID, String firstName, String lastName, String address, String phoneNumber,
			String specialization, String location) {
		// TODO Auto-generated method stub
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "001";
		String manager_id = managerID;
		
		String content = firstName+"\n"+lastName+"\n"+address+"\n"+phoneNumber+"\n"+specialization+"\n"+location;
		
		String message = request_id+"\n"+function_id+"\n"+manager_id+"\n"+content;
		String result = sendMessageToPrimaryServer(message);
		
		return result;
	}

	@Override
	public String createSRecord(String managerID, String firstName, String lastName, String courseRegistered,
			String status, String statusDate) {
		// TODO Auto-generated method stub
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "002";
		String manager_id = managerID;
		
		String content = firstName+"\n"+lastName+"\n"+courseRegistered+"\n"+status+"\n"+statusDate;
		
		String message = request_id+"\n"+function_id+"\n"+manager_id + "\n"+content;
		String result = sendMessageToPrimaryServer(message);
		
		return result;
	}

	@Override
	public String recordCounts(String managerID) {
		// TODO Auto-generated method stub
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "003";
		String manager_id = managerID;
	
		String message = request_id+"\n"+function_id+"\n"+manager_id ;
		
		String result = sendMessageToPrimaryServer(message);

		return result;
	}

	@Override
	public String editRecord(String managerID, String recordID, String fieldName, String newValue) {
		// TODO Auto-generated method stub
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "004";
		String manager_id = managerID;
		String content = recordID + "\n" + fieldName + "\n" + newValue;
		
		String message = request_id+"\n"+function_id+"\n"+manager_id+"\n"+content;
		String result = sendMessageToPrimaryServer(message);
		return result;
	}

	@Override
	public String transferRecord(String managerID, String recordID, String remoteCenterServerName) {
		// TODO Auto-generated method stub
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "005";
		String manager_id = managerID;
		
		String content = recordID + "\n" + remoteCenterServerName;
		
		String message = request_id+"\n"+function_id+"\n"+manager_id+"\n"+content;
		String result = sendMessageToPrimaryServer(message);
		return result;
	}
	
	public static String getNumberFromRequestIDGenerator(){
		DatagramSocket socket = null;
	    try {
	    	socket = new DatagramSocket();
	    	byte[] message = (new String("getRequestIdNumber")).getBytes();
	    	InetAddress host = InetAddress.getByName("localhost");
	    	DatagramPacket request = new DatagramPacket(message, message.length, host, 9998);
	    	socket.send(request);
	    	byte[] buffer = new byte[100];
	    	DatagramPacket reply = new DatagramPacket(buffer, buffer.length); 
	    	socket.receive(reply);
	    	String result = new String(reply.getData()).trim();
	    	return result;
	    }
	    catch(Exception e){
	    	System.out.println("Socket: " + e.getMessage()); 
	    	}
		finally{
			if(socket != null){
				socket.close();
				}
			}
		return null; 
	}
	
	public static String sendMessageToPrimaryServer(String n_message){
		
		/*
		 * new_message is like
		 * 0001 ---> requestID
		 * 001 ----> function_ID
		 * mtl10000 ---> managerID
		 * dong ----> first_name
		 * chen -----> last_name
		 * montreal,downtown ---> address
		 * 5145899900 ---> phone
		 * mtl ---> location
		 */
		
		// put the request in the hash table first
		// key = requestID, value = message itself
		System.out.println("Add request: "+ n_message.split("\n")[0] +" to hashtable");
		storeMessages.put(n_message.split("\n")[0], n_message);
		
		DatagramSocket socket = null;
	    try {
	    	socket = new DatagramSocket();
	    	byte[] message = (new String(n_message)).getBytes();
	    	InetAddress host = InetAddress.getByName("localhost");
	    	DatagramPacket request = new DatagramPacket(message, message.length, host, 9998);
	    	
	    	socket.send(request);
	    	socket.setSoTimeout(5000);
	    	byte[] buffer = new byte[1000];
	    	DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
	    	while(true){
	    		try {
	    			socket.receive(reply);
	    			String result = new String(reply.getData()).trim();
	    			if(result.equals("OK")){
	    	    		// if acknowledgement is OK then remove the request from hash map;
	    	    		System.out.println("delete request: "+ n_message.split("\n")[0] +" from hashtable");
	    	    		storeMessages.remove(n_message.split("\n")[0]);
	    	    	}
	    			else {
	    	    		return result;
	    	    	}
	    		} 
	    		catch (SocketTimeoutException e) {
	    			InetAddress host_resend = InetAddress.getByName("localhost");
	    			DatagramPacket request_resend = new DatagramPacket(message, message.length, host_resend, 9998);
	    			socket.send(request_resend);
	    		}
	    	}
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
		finally{
			if(socket != null){
				socket.close();
				}
		}
		return null;
	}
}

