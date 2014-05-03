
package graphicInterface;


import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.util.*;





class ChatProcess implements Runnable {
    private Start midlet;
    Object lock;
    private boolean gameOver;
    TextField send;
    Thread thread;

    SocketConnection serverSocket;
    OutputStream serverOut;
    InputStream serverIn;

    final int howManyMsgs=5;

    public ChatProcess(Start midlet,Object lock){

        try{
            int i;
            gameOver=false;                                               //initialize sockets
            this.lock=lock;
            this.midlet = midlet;                                               //initialize chatForm
            midlet.chatForm = new Form("Instant Messaging");
            for(i=0;i<howManyMsgs;i++){
                StringItem temp = new StringItem("","");
                midlet.chatForm.append(temp);
            }
            send = new TextField ("Send:", "", 150, TextField.ANY);
            midlet.chatForm.append(send);
            midlet.chatForm.addCommand(midlet.sendMessageCommand);
            midlet.chatForm.addCommand(midlet.goToTrilizaCommand);
            midlet.chatForm.setCommandListener(midlet);
        }
        catch(Exception e){
            System.err.println("ChatProcess::run: "+e.toString());
        }
        System.out.println("1. ChatThread Process...");
    }

    public void execute(){
        thread = new Thread(this);
        try{
            System.out.println("2. ChatThread Start...");
            thread.start();
        }
        catch(Exception e){
            System.err.println("ChatProcess"+e.toString());
        }
    }

    public void stop(){
        gameOver=true;
    }


    public void run(){
        try{
//            synchronized(lock){
//                lock.wait();
//                lock.wait();
//            }
            while(midlet.connectionWithServerIsOk == false){
                Thread.yield();
            }
            while(midlet.notifyChat == false){
                Thread.yield();
            }
            System.out.println("chat After Wait");
            serverSocket=midlet.serverSocket;
            serverIn = midlet.serverIn;
            serverOut = midlet.serverOut;
            while(!gameOver){
                receiveMSG();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void sendMSG(){                                                      //stelnei prvta send kai meta to msg
        try{
            byte[] s = "send\n".getBytes();
            serverOut.write(s);
            String displayMessage = send.getString()+"\n";
            String msg = midlet.player.username+" says: "+send.getString()+" \n";
            send.setString(null);
            byte[] data = msg.getBytes();
            serverOut.write(data);
            Calendar.getInstance();

            displayMessage="("+now()+") "+midlet.player.username+": "+displayMessage;
            displayMsg(displayMessage);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void receiveMSG(){
        try{
            byte[] data = new byte[100];
            serverIn.read(data);
            //Form currentForm=null;
            String temp= new String(data);
            String t=temp.substring(0,temp.indexOf(' '));
            System.out.println("msg from server: "+t+"---");
            if(t.equalsIgnoreCase("doCommand")){                                //commands apo ton coinHandler toy server
                t = temp.substring(temp.indexOf(" ")+1 , temp.lastIndexOf(' '));
                System.out.println("Second msg from server: "+t+"---");
                if(t.equalsIgnoreCase("halt")){
                    midlet.Halt();
                    //currentForm=midlet.haltForm;
                }
                else if(t.equalsIgnoreCase("buyMore")){
                    midlet.initializeBuyCoinsForm();
                    //currentForm=midlet.buyForm;
                }
                else if(t.equalsIgnoreCase("continue")){
                   midlet.display.setCurrent(midlet.trilizaForm);
                }
                else if(t.equalsIgnoreCase("NotEnoughMoney")){
                    Alert error = new Alert("error");
                    error.setType(AlertType.INFO);
                    error.setString("You do not have enough money");
                    //midlet.display.setCurrent(error , currentForm);
                    midlet.display.setCurrent(error , midlet.buyForm);
                }
                else if(t.equalsIgnoreCase("UserDoesNotExist")){
                    Alert error = new Alert("error");
                    error.setType(AlertType.INFO);
                    error.setString("Wrong username or password");
                    //midlet.display.setCurrent(error , currentForm);
                    midlet.display.setCurrent(error , midlet.buyForm);
                }
                 else if(t.equalsIgnoreCase("timePassedwin")){
                    midlet.timeLimitExpired("win");
                    stop();
//                    serverIn.close();
//                    serverOut.close();
//                    Thread.sleep(100000);
                 }
                 else if(t.equalsIgnoreCase("timePassedlose")){
                    midlet.timeLimitExpired("lose");
                    stop();
//                    serverIn.close();
//                    serverOut.close();
//                    Thread.sleep(100000);
                 }
            }
            else{
                displayMsg(temp+'\n');
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
//        catch(InterruptedException e){
//            e.printStackTrace();
//        }
        catch(StringIndexOutOfBoundsException ex){
            //do nothing
        }
    }

    private void displayMsg(String msg){
        int i;
        midlet.chatForm.delete(0);
        midlet.chatForm.insert(howManyMsgs-1,new StringItem("",msg));
    }

    String now() {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        String time = c.get(Calendar.HOUR_OF_DAY) + ":" +
        c.get(Calendar.MINUTE) + ":" +
        c.get(Calendar.SECOND);
        return time;
    }



}