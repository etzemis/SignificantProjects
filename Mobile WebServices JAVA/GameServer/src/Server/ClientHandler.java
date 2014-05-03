
package Server;
import java.io.*;
import java.net.*;



 class ClientHandler implements Runnable{
     private BufferedReader in1 , in2;
     private PrintWriter out1 ,out2;
     private Socket clientSocket1 = null;
     private Socket clientSocket2 = null;
     int id1,id2,gameID;
     static int port=6000;
     String result=null;


     public ClientHandler(Socket clientSocket1 , Socket clientSocket2,int id1,int id2,int gameID){
         this.clientSocket1 = clientSocket1;
         this.clientSocket2 = clientSocket2;
         this.id1=id1;
         this.id2=id2;
         this.gameID=gameID;
     }

     public void run(){
        System.out.println ("New Communication Thread Started");
        int i=0;
        try {
            //player1 input/output streams
            InputStream is = clientSocket1.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            in1 = new BufferedReader(isr);
            OutputStream os = clientSocket1.getOutputStream();
            out1 = new PrintWriter(os,true);

            //player2 input/output streams
            InputStream is2 = clientSocket2.getInputStream();
            InputStreamReader isr2 = new InputStreamReader(is2);
            in2 = new BufferedReader(isr2);
            OutputStream os2 = clientSocket2.getOutputStream();
            out2 = new PrintWriter(os2,true);


            Player2();
            Player1();

            CoinHandler coin = new CoinHandler(out1,out2,id1,id2);
            Thread coinThread = new Thread(coin);
            coinThread.start();
            ChatHandler chat1 = new ChatHandler(in1,out2,this,coin);//prosoxh  in toy protoy me out toy deyteroy
            ChatHandler chat2 = new ChatHandler(in2,out1,this,coin);
            Thread myThread = new Thread(chat1);
            myThread.start();
            Thread myThread2 = new Thread(chat2);
            myThread2.start();


            
            try{
                myThread.join();
                myThread2.join();
                coin.stop();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }

            Server.removeFromOngoing(gameID);                                        //update OnGoing matches

            Server.db.updateStats(id1,false,result);
            Server.db.updateStats(id2,true,result);

            out1.close();                                                       //close connection
            in1.close();
            clientSocket1.close();
            out2.close();
            in2.close();
            clientSocket2.close();
        }
        catch (IOException e){                                                  
         System.err.println("Problem with Communication Server");
        }
    }

     private void Player1(){
         try{
            System.out.println("sending to player1");
             if((in1.readLine()).equalsIgnoreCase("play")){
                out1.println(clientSocket2.getInetAddress().getHostAddress() + " X 1 "+port);
                port = port+1;
             }
         }
         catch(IOException e){
             e.printStackTrace();
         }
     }

     private void Player2(){
         try{
            System.out.println("sending to player2");
            if(port>8000) port=6000;
            String test;
            test=in2.readLine();
            if(test.equalsIgnoreCase("play")){
                out2.println(clientSocket1.getInetAddress().getHostAddress() + " O 0 "+port);
                out2.flush();
            }
         }
         catch(IOException e){
             e.printStackTrace();
         }
     }


    void setGameResult(String result){
        this.result=result;
    }


 }