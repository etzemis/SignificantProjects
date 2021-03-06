

package graphicInterface;

import java.lang.Object.*;
import java.io.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import org.netbeans.microedition.lcdui.SimpleTableModel;
import org.netbeans.microedition.lcdui.TableItem;



public class TrilizaProcess implements Runnable{

    Thread thread;
    private User player;
    char[] metablhtes;
    private char symbol;
    
    TextField choise;
    TableItem tableItem;
    StringItem wait;
    StringItem YourSymbol;


    /* Communication with the server*/
    SocketConnection serverSocket;
    OutputStream serverOut;
    InputStream serverIn;
    int turn;
    String serverIp;
    String port;
    String myIp;
    /* Communication between players*/
    private static ServerSocketConnection serverPlayerSocket;                   //paikths pou kanei ton server
    private static SocketConnection rivalSocket;//otan turn == 1
    StreamConnection opponentStream = null;     //otan turn == 0;
    OutputStream out;
    InputStream in;

    
    private boolean readyToSend=false;
    private String result=null;

    Start midlet;
    Object lock;


    public TrilizaProcess(User player , Start midlet, Object lock){
        this.midlet=midlet;
        this.lock = lock;
        metablhtes = new char[]{'1','2','3','4','5','6','7','8','9'};
        midlet.trilizaForm = new Form("tic-tac-toe");// You can use display to present a new form etc....
        choise = new TextField("Type your choise:", "", 15, TextField.DECIMAL);
        wait = new StringItem("...","wait for rival to play");
        SimpleTableModel tableModel1 = new SimpleTableModel(new java.lang.String[][] {
        new java.lang.String[] { "1", "2", "3" },
        new java.lang.String[] { "4", "5", "6" },
        new java.lang.String[] { "7", "8", "9" }}, null);
        tableItem = new TableItem(midlet.display, "");
        tableItem.setLayout(ImageItem.LAYOUT_CENTER | Item.LAYOUT_TOP | Item.LAYOUT_BOTTOM | Item.LAYOUT_VCENTER | Item.LAYOUT_EXPAND | Item.LAYOUT_VEXPAND);
        tableItem.setTitle("triliza");
        tableItem.setModel(tableModel1);
        midlet.trilizaForm.append(choise);
        midlet.trilizaForm.append(tableItem);
        midlet.trilizaForm.addCommand(midlet.goToChatCommand);
        midlet.trilizaForm.addCommand(midlet.sendChoiseCommand);
        midlet.trilizaForm.setCommandListener(midlet);
        this.player = player;
        System.out.println("1. TrilizaThread Process...");
    }

    public void execute(){
        thread = new Thread(this);
        try{
            System.out.println("2. TrilizaThread Start...");
            thread.start();
        }
        catch(Exception e){
            System.err.println("TrilizaProcess"+e.toString());
        }
    }

    public void run(){
            try{
//                synchronized(lock){
//                    lock.wait();
//                }
            while(midlet.connectionWithServerIsOk == false){
                Thread.yield();
            }
                    System.out.println("triliza After Wait");
                    serverSocket=midlet.serverSocket;
                    startCommunicationWithServer();
                    if(symbol == 'O')
                        YourSymbol = new StringItem("Your symbol = ","O");
                    else if(symbol == 'X')
                        YourSymbol = new StringItem("Your symbol = ","X");
                    midlet.trilizaForm.insert(midlet.trilizaForm.size() , YourSymbol);
//                synchronized(lock){
//                    lock.notify();//norify chat thread
//                }
                  midlet.notifyChat = true;
                startRivalConnection();
                if(turn == 0){
                    midlet.trilizaForm.delete(0);
                    midlet.trilizaForm.insert(0,wait);
                    midlet.trilizaForm.removeCommand(midlet.sendChoiseCommand);
                    readRivalChoise();
                    midlet.trilizaForm.delete(0);
                    midlet.trilizaForm.insert(0,choise);
                    midlet.trilizaForm.addCommand(midlet.sendChoiseCommand);
                }
                while(gameStillOn()){
                    sendChoise();
                    midlet.trilizaForm.delete(0);
                    midlet.trilizaForm.insert(0,wait);
                    midlet.trilizaForm.removeCommand(midlet.sendChoiseCommand);
                    if(!gameStillOn()) break;
                    readRivalChoise();
                    midlet.trilizaForm.delete(0);
                    midlet.trilizaForm.insert(0,choise);
                    midlet.trilizaForm.addCommand(midlet.sendChoiseCommand);
                }
                closeConnections();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        
    }

    private void startCommunicationWithServer(){
        try {
            serverIn = midlet.serverIn;//serverSocket.openInputStream();
            serverOut = midlet.serverOut;//serverSocket.openOutputStream();
            byte[] data = new byte[200];
            String responseLine;

            data = "Play\n".getBytes();
            serverOut.write(data);
            byte[] data2 =new byte[500];

            serverIn.read(data2);
            responseLine = new String(data2);


            int position=responseLine.indexOf(" ");
            serverIp = responseLine.substring(0, position);                        //we have the opoonent's ip adress
            String s = responseLine.substring(position+1 , position+2);
            symbol=s.charAt(0);
            String t= responseLine.substring(position+3 , position+4);
            turn = Integer.parseInt(t);
            port = responseLine.substring(position+5 , position+9);

            System.out.println("IP "+serverIp+" symbol " +symbol+ " turn " +turn+ " port "+port);

        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void startRivalConnection(){
        if(turn == 0){                                                          //emeis kanoyme ton server
            byte[] b = new byte[128];
            try{
                serverPlayerSocket = (ServerSocketConnection) Connector.open("socket://:"+port);


                opponentStream = serverPlayerSocket.acceptAndOpen();
                in = opponentStream.openInputStream();
                out = opponentStream.openOutputStream();
           //check for the right opoonent
                byte[] data = new byte[100];
                in.read(data);
                String temp= new String(data);  //mas stelnei o antipalos thn ip toy
                temp=temp.substring(0,serverIp.length());
                System.out.println("opponents ip = "+temp+"serverip = "+serverIp);
                System.out.println(temp.compareTo(serverIp));
                while(temp.compareTo(serverIp)!= 0){
                    in.close();
                    out.close();
                    opponentStream = serverPlayerSocket.acceptAndOpen();
                    in = opponentStream.openInputStream();
                    out = opponentStream.openOutputStream();
                    in.read(data);
                    temp= new String(data);  //mas stelnei o antipalos thn ip toy
                }

                //midlet.display.setCurrent(midlet.trilizaForm);

                System.out.println("server client ok");
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else{                                                                   //syndeomaste ston allon antipalo
            try {
                Thread.sleep(1000);
                rivalSocket = (SocketConnection) Connector.open("socket://"+serverIp+":"+port);
                in = rivalSocket.openInputStream();
                out = rivalSocket.openOutputStream();

                //send validation message
                myIp = rivalSocket.getLocalAddress();
                System.out.println("myIp = "+myIp );
                byte[] s = myIp.getBytes();                              /////sos alla3e to
                out.write(s);


                //midlet.display.setCurrent(midlet.trilizaForm);

            }
            catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
    }


    private void closeConnections(){
        try{
            in.close();
            out.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }




    public void sendChoise(){
        int position;
        while(readyToSend!=true) Thread.yield();
        try{
            position = Integer.valueOf(choise.getString()).intValue();
            if(wrongChoise(position)){
                midlet.wrongInput();
                readyToSend=false;
                sendChoise();
            }
            if(position==0){                                                        //an kaneis resign
                result="lose";
                out.write((new Integer(position)).byteValue());
            }
            else if(metablhtes[position-1]=='X' || metablhtes[position-1]=='O'){    //if you choose an already occupied box
                midlet.FullBox();
                readyToSend=false;
                sendChoise();
            }
            else{
                updateTable(position, symbol);
                out.write((new Integer(position)).byteValue());
            }
            choise.setString(null);
            readyToSend=false;
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setToSend(){
        readyToSend = true;
    }

    public void readRivalChoise(){
        try{
            byte[] data = new byte[512];
            in.read(data);
            int rivalChoise = (int) data[0];

            if(rivalChoise==0){
                result="win";
            }
            else if(symbol == 'X')
            {
                updateTable(rivalChoise,'O');
            }
            else{
                updateTable(rivalChoise,'X');
            }
            //myTurn = true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void updateTable(int ch,char s){
            //if(!myTurn) return;
            metablhtes[ch-1]=s;          //allagh table
            tableItem.setModel(metablhtesToTableModel());
            //choise.setString(null);
    }


    private SimpleTableModel metablhtesToTableModel(){
            SimpleTableModel tableModel = new SimpleTableModel(new java.lang.String[][] {
            new java.lang.String[] { toString(metablhtes[0]) , toString(metablhtes[1]), toString(metablhtes[2]) },
            new java.lang.String[] { toString(metablhtes[3]), toString(metablhtes[4]), toString(metablhtes[5]) },
            new java.lang.String[] { toString(metablhtes[6]), toString(metablhtes[7]), toString(metablhtes[8]) }}, null);

            return tableModel;
    }

    private String toString(char c){
        char[] ch={c};
        String s  = new String(ch);
        return s;
    }

    private boolean gameStillOn(){
        System.out.println("result = "+result);
        if(result == null)
            checkTable();
        if(result != null){
            sendResultToServer();
            midlet.ShowResult(result);
            return false;
        }
        return true;
    }

    public void setResult(String r){
        result=r;
    }

    public void sendResultToServer(){
        try{
            if(turn==1){
                result=result+"\n";
                serverOut.write("result\n".getBytes());
                serverOut.write(result.getBytes());
            }
            if(turn==0){
                serverOut.write("gameOver\n".getBytes());
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private boolean wrongChoise(int position){
        if(position!=0 && position!=1
        && position!=2 && position!=3
        && position!=4 && position!=5
        && position!=6 && position!=7
        && position!=8 && position!=9)  return true;
        return false;
    }

    private boolean checkIfFull(){
        int i;
        for(i = 0 ; i<9 ; i++){
            if(metablhtes[i]!='X' && metablhtes[i]!='O')
                return false;
        }
        return true;
    }

    private void checkTable(){
        //check lines
        if(checkIfFull()) result= "tie";
        if(metablhtes[0] == symbol && metablhtes[1] == symbol && metablhtes[2] == symbol){
            result= "win";
        }
        if(metablhtes[3] == symbol && metablhtes[4] == symbol && metablhtes[5] == symbol){
            result= "win";
        }
        if(metablhtes[6] == symbol && metablhtes[7] == symbol && metablhtes[8] == symbol){
            result= "win";
        }
        //check columns
        if(metablhtes[0] == symbol && metablhtes[3] == symbol && metablhtes[6] == symbol){
            result= "win";
        }
        if(metablhtes[1] == symbol && metablhtes[4] == symbol && metablhtes[7] == symbol){
            result= "win";
        }
        if(metablhtes[2] == symbol && metablhtes[5] == symbol && metablhtes[8] == symbol){
            result= "win";
        }
        //check prwth diagwnio
        if(metablhtes[0] == symbol && metablhtes[4] == symbol && metablhtes[8] == symbol){
            result= "win";
        }
        //check deyterh diagwnio
        if(metablhtes[2] == symbol && metablhtes[4] == symbol && metablhtes[6] == symbol){
            result= "win";
        }
/*******************************************************************************/
        char s;
        if(symbol == 'X') s='O';
        else s='X';

        if(metablhtes[0] == s && metablhtes[1] == s && metablhtes[2] == s){
            result= "lose";
        }
        if(metablhtes[3] == s && metablhtes[4] == s && metablhtes[5] == s){
            result= "lose";
        }
        if(metablhtes[6] == s && metablhtes[7] == s && metablhtes[8] == s){
            result= "lose";
        }
        //check columns
        if(metablhtes[0] == s && metablhtes[3] == s && metablhtes[6] == s){
            result= "lose";
        }
        if(metablhtes[1] == s && metablhtes[4] == s && metablhtes[7] == s){
            result= "lose";
        }
        if(metablhtes[2] == s && metablhtes[5] == s && metablhtes[8] == s){
            result= "lose";
        }
        //check prwth diagwnio
        if(metablhtes[0] == s && metablhtes[4] == s && metablhtes[8] == s){
            result= "lose";
        }
        //check deyterh diagwnio
        if(metablhtes[2] == s && metablhtes[4] == s && metablhtes[6] == s){
            result= "lose";
        }
    }
}






