package FrontEnd;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import DCMS_FrontEnd.*;
import Records.*;
import Servers.*;


public class FrontEndServer {
	public static void main(String[] args) {
		System.out.println("open a listener for update the config file of leader info.");
		open_UDP_Listener_For_Update_Leader_info();
		System.out.println("Initial the front end corba part");
		init_Front_End_CORBA(args);
	}
	

	public static void init_Front_End_CORBA(String[] args){
		try {
			//initial the port number of 1050;
			Properties props = new Properties();
	        props.put("org.omg.CORBA.ORBInitialPort", 900);
	        
			// create and initialize the ORB
			ORB orb = ORB.init(args, props);

			// get reference to rootpoa & activate the POAManager
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			FrontEndImpl Fimpl = new FrontEndImpl();
			Fimpl.setORB(orb); 

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(Fimpl);
			FrontEnd href = FrontEndHelper.narrow(ref);
			    
			// get the root naming context
			// NameService invokes the name service
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			// Use NamingContextExt which is part of the Interoperable
			// Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// bind the Object Reference in Naming
			String name = "FrontEnd";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, href);

			System.out.println("Front End Server Ready And Waiting ...");

			// wait for invocations from clients
			orb.run();
		} catch (Exception e) {
			System.err.println("ERROR: " + e);
	        e.printStackTrace(System.out);
		}
		System.out.println("Front End Server Exiting ...");
	}
	
	public static void open_UDP_Listener_For_Update_Leader_info(){
		Thread update = new Thread(new Runnable() {
			@Override
			public void run() {
				DatagramSocket socket = null;
				try{
					socket = new DatagramSocket(1111); 
					while(true){
						System.out.println("start updating primary leader listener");
						byte[] buffer = new byte[1000]; 
						int primaryPort = 9999;
						DatagramPacket request = new DatagramPacket(buffer, buffer.length);
						socket.receive(request);
						String content = new String(request.getData()).trim();
						if(content.equals("who is leader?")){
							String resend_leader_port = Integer.toString(primaryPort);
							DatagramPacket reply_leader = new DatagramPacket(resend_leader_port.getBytes(),resend_leader_port.getBytes().length, request.getAddress(), request.getPort());
							socket.send(reply_leader);
						}else{
							String receivedContent = new String(request.getData()).trim();
							int leader_port = Integer.parseInt(receivedContent.substring(9));
							primaryPort = leader_port;
							String acknowledgement = "OK";
							DatagramPacket update_leader = new DatagramPacket(acknowledgement.getBytes(),acknowledgement.getBytes().length, request.getAddress(), request.getPort());
							System.out.println("new leader is " + leader_port);
							socket.send(update_leader);
						}
					}	
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					if(socket != null) socket.close();
				}
			}
		});
		System.out.println("starting listener to update leader");
		update.start();
	}
}