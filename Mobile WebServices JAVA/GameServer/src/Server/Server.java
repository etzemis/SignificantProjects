
package Server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;



public class Server
{
 protected static Socket clientSocket1;
 protected static Socket clientSocket2;
 static Vector<Pairs> OnGoing = new Vector<Pairs>();
 static volatile Vector<Player> Stats = new Vector<Player>();
 static Vector<Profile> Waiting = new Vector<Profile>();
 static ConnectionDB db;
 static Object dbLock;


 public static void main(String[] args) throws IOException
 {
    int ID=1;
    int gameID=1;
    int id1=0,id2=0,i=0;
    boolean flag1=false,flag2=false;
    ServerSocket serverSocket = null;
    ResultSet result;


    String model="p";
    InputStream is;
    InputStreamReader isr;
    BufferedReader in1;

    dbLock = new Object();


    try{
       
        serverSocket = new ServerSocket(Integer.parseInt(args[1]),1,InetAddress.getByName(args[0]));
    }
    catch(IOException e){
        System.err.println("Could not listen on port "+args[1]);
        System.exit(1);
    }

    db = new ConnectionDB();
    try{
        db.init();    //connect with database
        String s = init();       //starts db of bank
        System.out.println(s);
    }
    catch(Exception e){
        e.printStackTrace();
    }

    CmdThread ct = new CmdThread();
    Thread myThread1 = new Thread(ct);
    myThread1.start();
    System.out.println("Server running\n");
    System.out.flush();
    while(true){
        try{
            while(true){
                clientSocket1 = serverSocket.accept();
                is = clientSocket1.getInputStream();
                isr = new InputStreamReader(is);
                in1 = new BufferedReader(isr);
                model=in1.readLine();
                System.out.println(model);
                if(findMatch(model)){
                    break;
                }
                Profile profile=new Profile(model,clientSocket1);
                Waiting.add(profile);
            }
                
            try{
                synchronized(Server.dbLock){
    //                result = db.getStat().executeQuery (
    //                "SELECT * FROM statistics WHERE ip='"+clientSocket1.getInetAddress().getHostAddress()+"'");
    //                if(result.next()){
    //                    id1= result.getInt("ID");
    //                }
    //                else{
                        id1=ID++;
                        Player p1 = new Player(id1,clientSocket1.getInetAddress().getHostAddress());
                        db.insertIntoStatistics(p1);
    //                }
    //                result = db.getStat().executeQuery (
    //                "SELECT * FROM statistics WHERE ip='"+clientSocket2.getInetAddress().getHostAddress()+"'");
    //                if(result.next()){
    //                    id2= result.getInt("ID");
    //                }
    //                else{
                        id2=ID++;
                        Player p2 = new Player(id2,clientSocket2.getInetAddress().getHostAddress());
                        db.insertIntoStatistics(p2);
    //                }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }                                                                          //------

            ClientHandler ch = new ClientHandler(clientSocket1 , clientSocket2,id1,id2,gameID); //start ClientHandler thread
            Thread myThread2 = new Thread(ch);
            myThread2.start();

            Pairs pair = new Pairs(id1,id2,gameID,myThread2);                             //update OnGoing matches
            Server.OnGoing.add(pair);

            gameID++;
            flag1=false;
            flag2=false;

        }
        catch(IOException e){
            System.err.println("Accept failed.");
            serverSocket.close();
        }
    }
 }

 private static boolean findMatch(String model){
    int i;
    for(i=0;i<Waiting.size();i++){
        if(Waiting.get(i).model.equalsIgnoreCase(model)){
            Server.clientSocket2=Waiting.get(i).socket;
            Waiting.remove(i);
            return true;
        }
    }
    return false;
 }

public static void removeFromOngoing(int ID){
    int i=0;
    for(i=0;i< OnGoing.size();i++){
        if(OnGoing.get(i).hasID(ID)){
            OnGoing.remove(i);
            break;
        }
    }
}

    private static String init() {
        services.GameServerServiceService service = new services.GameServerServiceService();
        services.GameServerService port = service.getGameServerServicePort();
        return port.init();
    }



}

