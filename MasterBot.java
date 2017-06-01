

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;

public class MasterBot {
    ServerListener listener;
    Thread serthread;
    ServerSocket serverSocket;
    ArrayList<ClientSocketInfo> Clisockinfo;
    int port;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    public MasterBot(int port) {
        try {
            this.Clisockinfo = new ArrayList<ClientSocketInfo>();
            this.port = port;
            
            this.serverSocket = new ServerSocket(port);
           
            this.listener = new ServerListener();
            
            this.serthread  = new Thread(this.listener);
           
            this.serthread.start();
        } catch (IOException e) {
           // e.printStackTrace();
            System.exit(-1);
        }
    }

    public void executeCommand(String command) throws IOException {
       
        String[] command_parts = command.split(" "); 
        if(command_parts.length <= 0)
            return;
        switch(command_parts[0]) {
            case "list":
               
                System.out.println("SlaveHostName IPAddress SourcePortNumber RegistrationDate");
                for(ClientSocketInfo clientSocketInfo: Clisockinfo) {
                    System.out.print(clientSocketInfo.getHostname() + " ");
                    System.out.print(clientSocketInfo.getIpaddress() + " ");
                    System.out.print(clientSocketInfo.getPort() + " ");
                    System.out.print(clientSocketInfo.getRegisteredDate() + " ");
                    System.out.println();
                }
                break;
            case "connect":
                for(ClientSocketInfo clientSocketInfoObject : Clisockinfo) {
                    printWriter = new PrintWriter(clientSocketInfoObject.getSocket().getOutputStream(), true);
                    printWriter.println(command);
                }
                break;
            case "disconnect":
                for(ClientSocketInfo clientSocketInfoObject : Clisockinfo) {
                    
                    printWriter = new PrintWriter(clientSocketInfoObject.getSocket().getOutputStream(), true);
                    printWriter.println(command);
                }
                break;
            default:
   
                break;
        }
    }
    public static void main(String[] args) {
  
        int port=0;
	int length=args.length;
	if((length==2)&&args[0].equals("p"))
	{
		
		 port=Integer.parseInt(args[1]);
		
		
	}else
	{
	   		System.exit(-1);
	}
        
	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        MasterBot masterBot = new MasterBot(port);
       
        while(true) {

        

         String rdline;

         try {

         rdline = null;

         System.out.print(">");

         rdline = bufferedReader.readLine();




         if(rdline.equals("exit"))

         break;



         if(rdline != null) {

        

         masterBot.executeCommand(rdline);

         }

         } catch (IOException e) {

       
System.exit(-1);
         }
         }

         }
    private class ServerListener extends Thread{
        public ServerListener() {
            
        }
        @Override
        public void run() {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    InetAddress inetAddress = clientSocket.getInetAddress();
                    String hostname = inetAddress.getHostName();
                    String ipaddress = inetAddress.getHostAddress();
                    int portNumber = port;
                    Date registeredDate = new Date(System.currentTimeMillis());

                   
                    ClientSocketInfo newClientSocketInfo = new ClientSocketInfo(hostname, ipaddress, portNumber, clientSocket, registeredDate);
                                      Clisockinfo.add(newClientSocketInfo);

                                } 
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unused")
    private class ClientSocketInfo {
        private String hname;
        private String ipadr;
        private int pt;
        private Socket socket;
        private Date registeredDate;
        public ClientSocketInfo(String hostname, String ipaddress, int port, Socket socket, Date registeredDate) {
           
            this.hname = hostname;
            this.ipadr = ipaddress;
            this.pt = port;
            this.socket = socket;
            this.registeredDate = registeredDate;
        }
        public void setPort(int port) {
            this.pt = port;
        }
        public void setHostname(String hostname) {
            this.hname = hostname;
        }
        public String getHostname() {
            return hname;
        }
       
        public void setIpaddress(String ipaddress) {
            this.ipadr = ipaddress;
        }
        public String getIpaddress() {
            return ipadr;
        }
        public int getPort() {
            return port;
        }
       
        public Socket getSocket() {
            return socket;
        }
        public void setSocket(Socket socket) {
            this.socket = socket;
        }
        public void setRegisteredDate(Date registeredDate) {
            this.registeredDate = registeredDate;
        }
        public Date getRegisteredDate() {
            return registeredDate;
        }
      
    }
}          