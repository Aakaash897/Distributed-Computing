package Servers;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import DCMS_FrontEnd.*;

public class ServerRunner
{

    public static void main(String[] args) {
        try {
            
            MTLServer mtl = new MTLServer();
            LVLServer lvl = new LVLServer();
            DDOServer ddo = new DDOServer();
            
            Thread t1 = new Thread(mtl);
            Thread t2 = new Thread(lvl);
            Thread t3 = new Thread(ddo);
            
            t1.start();
            t2.start();
            t3.start();
            
            initCORBA(args);
            
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void initCORBA(String[] args) throws Exception {
        try {
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", 900);
            
            String[] a = null;
            ORB orb = ORB.init(a,props);
           
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
   
            rootpoa.the_POAManager().activate();
            
            MTLServer mtlserver = new MTLServer();
            LVLServer lvlserver = new LVLServer();
            DDOServer ddoserver = new DDOServer();
            
            mtlserver.setORB(orb);
            lvlserver.setORB(orb);
            ddoserver.setORB(orb);

            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(mtlserver);
            org.omg.CORBA.Object lref = rootpoa.servant_to_reference(lvlserver);
            org.omg.CORBA.Object dref = rootpoa.servant_to_reference(ddoserver);
   
            FrontEnd href = FrontEndHelper.narrow(ref);
            FrontEnd href1 = FrontEndHelper.narrow(lref);
            FrontEnd href2 = FrontEndHelper.narrow(dref);
      
            org.omg.CORBA.Object objref = orb.resolve_initial_references("NameService");
          
            NamingContextExt nref = NamingContextExtHelper.narrow(objref);
            
            String name = "MTL";
            String name1 = "LVL";
            String name2 = "DDO";
            
            NameComponent path[] = nref.to_name(name);
            nref.rebind(path, href);
            
            NameComponent path1[] = nref.to_name(name1);
            nref.rebind(path1, href1);
            
            NameComponent path2[] = nref.to_name(name2);
            nref.rebind(path2, href2);
            
            System.out.println(" Servers are running. . . .");
            
            orb.run();
        }
        catch(Exception e) {
            e.printStackTrace();
            
        } System.out.println("Center Server Montreal Exiting ...");
    } 
}
