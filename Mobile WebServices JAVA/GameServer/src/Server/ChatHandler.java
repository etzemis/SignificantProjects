
package Server;
import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.TimeZone;



public class ChatHandler implements Runnable{
    
     private BufferedReader in;
     private PrintWriter rivalOut;
     ClientHandler clientHandler;
     CoinHandler coinH;

    public ChatHandler(BufferedReader in,PrintWriter out,ClientHandler ch,CoinHandler coinH){
        this.in=in;
        rivalOut=out;
        clientHandler=ch;
        this.coinH=coinH;
    }

    public void run(){
        try{
            String msg;
            System.out.println("ChatHandler thread started");
            System.out.flush();
            while(true){
                msg = in.readLine();
                //System.out.println("msg : "+msg);
                if(msg.equalsIgnoreCase("Send")){
                    msg=in.readLine();
                    rivalOut.println("("+now()+") "+msg);
                    System.out.println(msg);
                }
                else if(msg.equalsIgnoreCase("Result")){                        //to result to stelnei otan exei turn=1
                    msg=in.readLine();
                    clientHandler.setGameResult(msg);
                    rivalOut.println("gameOver");
                    break;
                }
                else if(msg.equalsIgnoreCase("gameOver")){                      //gameover stelnei otan exei turn=0
                    rivalOut.println("gameOver");
                    break;
                }
                else if(msg.equalsIgnoreCase("buyCoins")){
                    coinH.updateCoinsFromBank(in.readLine(),in.readLine(),in.readLine());//username password money
                }
                else if(msg.equalsIgnoreCase("updateCreditsFromBank")){
                    System.out.println("GAmeServer: update credits directly");
                    String username = in.readLine();
                    String m = updateCoins(username);
                    //System.out.println(m+"  -----");
                    int money = Integer.parseInt(m);
                    //System.out.println("money From update =  "+money);
                    coinH.addCoins(10*money);
                }
            }
        }
        catch(IOException e){
            System.err.println("ChatHandler error "+ e.toString());
        }
    }

    String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    private static String updateCoins(java.lang.String username) {
        services.GameServerServiceService service = new services.GameServerServiceService();
        services.GameServerService port = service.getGameServerServicePort();
        return port.updateCoins(username);
    }



}

