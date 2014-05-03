

package Server;
import java.io.*;


public class CmdThread  implements Runnable{
        String cmd;
        int i;

        public void run(){
            try{
               while(true){
                   System.out.println("Available commands: statistics, exit");
                   BufferedReader standardIn = new BufferedReader(new InputStreamReader(System.in));
                   cmd = standardIn.readLine();
                   if(cmd.equalsIgnoreCase("statistics")){
                       Server.db.printStatistics();
                       System.out.println("\n Ongoing Matches:"+Server.OnGoing.size());
                       for(i=0;i<Server.OnGoing.size();i++){
                           Server.OnGoing.get(i).print();
                       }
                   }
                   if(cmd.equalsIgnoreCase("exit")){
                       Server.db.close();
                       System.exit(0);
                   }
               }
           }
           catch(IOException e){
                System.err.println("Could not read from standar input");
                System.exit(1);
           }
        }


        public CmdThread(){
            i=0;
            cmd=null;
        }

    private static String closeDB() {
        services.GameServerServiceService service = new services.GameServerServiceService();
        services.GameServerService port = service.getGameServerServicePort();
        return port.closeDB();
    }
}
