

package Server;

import java.io.*;
import java.net.*;
import java.sql.*;


public class CoinHandler implements Runnable {

    private int id1,id2;
    private PrintWriter out1,out2;
    boolean stop=false;
    int player;
    int coinsOne,coinsTwo;


    public CoinHandler(PrintWriter out1,PrintWriter out2,int id1,int id2){
        this.id1=id1;
        this.id2=id2;
        this.out2=out2;
        this.out1=out1;
    }

    public void run(){
        try{
            System.out.println("coinHandler thread started");
            updateCoins("both");
            player=0;
            double time;
            deleteCoins();
            while(stop==false){
                if(coinsOne<=0){
                    //System.out.println("inside if(coinsOne==0)");
                    player=1;
                    out1.println("doCommand buyMore ");
                    out2.println("doCommand halt ");
                    time=0.0;
                    while(coinsOne<=0){
                        if(stop==true) return;
                        Thread.sleep(500);
                        time+=0.5;
                        if(time==120){
                            out1.println("doCommand timePassedlose ");
                            out2.println("doCommand timePassedwin ");
                            addSpareCoins();
                            return;
                        }
                        updateCoins("player1");
                    }
                    deleteCoins();
                    out1.println("doCommand continue \n");                      //epidh kolage xvris \n
                    out2.println("doCommand continue \n");//out2.println("continue ");
                    Thread.yield();
                    Thread.sleep(1000);
                    //System.out.println("esteila doCommand continue ston player1");
                }
                if(coinsTwo<=0){
                    //System.out.println("inside if(coinsTwo==0)");
                    player=2;
                    out1.println("doCommand halt ");//out1.println("halt ");
                    out2.println("doCommand buyMore ");//out2.println("buyMore ");
                    time=0.0;
                    while(coinsTwo<=0){
                        if(stop==true) return;
                        Thread.sleep(500);
                        time+=0.5;
                        if(time==120){
                            out1.println("doCommand timePassedwin ");
                            out2.println("doCommand timePassedlose ");
                            addSpareCoins();
                            return;
                        }
                        updateCoins("player2");
                        Thread.yield();
                    }
                    deleteCoins();
                    out1.println("doCommand continue ");//out1.println("continue ");
                    out2.println("doCommand continue ");
                    //System.out.println("esteila doCommand continue ston player2");
                }
                Thread.sleep(900);
                coinsOne--;
                coinsTwo--;
            }
            addSpareCoins();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    void updateCoinsFromBank(String username, String password, String money){
        int m = Integer.parseInt(money);
        String result=getCredits(username,password,m);
        //System.out.println("result apo buy coins == "+result);
        if(result.equalsIgnoreCase("success")){
            addCoins(Integer.parseInt(money)*10);
        }
        else if(result.equalsIgnoreCase("Not enough money")){
            if(player==1){
                out1.println("doCommand  NotEnoughMoney ");
            }
            else{
                out2.println("doCommand  NotEnoughMoney ");
            }
        }
        else if(result.equalsIgnoreCase("User does not exist")){
            if(player==1){
                out1.println("doCommand  UserDoesNotExist ");
            }
            else{
                out2.println("doCommand  UserDoesNotExist ");
            }
        }
    }

    void stop(){
        stop=true;
    }


    void updateCoins(String p){
        try{
            synchronized(Server.dbLock){
                ResultSet result;
                //System.out.println("id1 , id2 = "+id1+" , "+id2);
                if(p.equalsIgnoreCase("player1") || p.equalsIgnoreCase("both")){
                    result = Server.db.getStat().executeQuery (
                    "SELECT coins FROM statistics WHERE id = "+id1);
                    if(result.next()){
                        if(result==null) return;
                        coinsOne= result.getInt("coins");
                    }
                }
                if(p.equalsIgnoreCase("player2") || p.equalsIgnoreCase("both")){
                    result = Server.db.getStat().executeQuery (
                    "SELECT coins FROM statistics WHERE id = "+id2);
                    if(result.next()){
                        if(result==null) return;
                        coinsTwo= result.getInt("coins");
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    void deleteCoins(){
        try{
            synchronized(Server.dbLock){
                if(player==1){
                    Server.db.getStat().executeUpdate (
                            "UPDATE statistics SET coins = 0 WHERE id = "+id1);
                }
                if(player==2){
                    Server.db.getStat().executeUpdate (
                            "UPDATE statistics SET coins = 0 WHERE id = "+id2);
                }
                if(player==0){
                    Server.db.getStat().executeUpdate (
                            "UPDATE statistics SET coins = 0 WHERE id = "+id2+" OR id = "+id1);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    void addSpareCoins(){
        try{
            synchronized(Server.dbLock){
                Server.db.getStat().executeUpdate (
                        "UPDATE statistics SET coins = "+coinsOne+" WHERE id = "+id1);
                Server.db.getStat().executeUpdate (
                        "UPDATE statistics SET coins = "+coinsTwo+" WHERE id = "+id2);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    void addCoins(int coins){
        try{
            synchronized(Server.dbLock){
                int id=0;
                if(player==1) id=id1;
                if(player==2) id=id2;
                Server.db.getStat().executeUpdate (
                        "UPDATE statistics SET coins = coins+"+coins+" WHERE id = "+id);
                System.out.println("add Coins done");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static String getCredits(java.lang.String username, java.lang.String password, int money) {
        services.GameServerServiceService service = new services.GameServerServiceService();
        services.GameServerService port = service.getGameServerServicePort();
        return port.getCredits(username, password, money);
    }


}