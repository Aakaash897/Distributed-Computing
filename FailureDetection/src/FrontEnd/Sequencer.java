package FrontEnd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Sequencer {
	private static int startNumber = 1;
	
	/**
	 * This method is get the number of the ID
	 * @return int
	 */
	
	public static synchronized int getRequestID(){
		return startNumber++;
	}
	
	public static void main(String[] args) {
		System.out.println("Sequence Generator Server Ready And Waiting ...");
		openUDPListener();
	}
	
	/**
	 * Open UDP listening port to assign unique recordID number.
	 * 
	 */
	public static void openUDPListener(){
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket(1112);
			while(true){
				byte[] buffer = new byte[100]; 
				DatagramPacket request = new DatagramPacket(buffer, buffer.length); 
				socket.receive(request);
				new Connection(socket, request);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(socket != null) socket.close();
		}
	}
	
	/**
	 * New thread to handle the newly request
	 * @author AlexChen
	 *
	 */
	static class Connection extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		String result = null;
		public Connection(DatagramSocket n_socket, DatagramPacket n_request) {
			this.socket = n_socket;
			this.request = n_request;
			String content = new String(request.getData()).trim();
			System.out.println(content);
			switch (content) {
			case "getRequestIdNumber":
				result = String.format("%04d", getRequestID());
				break;
			}
			this.start();
		}
		
		@Override
		public void run() {
			try {
				DatagramPacket reply = new DatagramPacket(result.getBytes(),result.getBytes().length, request.getAddress(), request.getPort()); 
				socket.send(reply);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
