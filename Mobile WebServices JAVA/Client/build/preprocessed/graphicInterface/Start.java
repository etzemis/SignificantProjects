package graphicInterface;



import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;
import javax.microedition.io.*;
import java.io.*;
import clientserviceservice.*;




public class Start extends MIDlet implements CommandListener {

    Display display;     // The display for this MIDlet

    Command exitCommand; // The exit command
    private Command playCommand;
    private Command loginCommand;
    private Command changeSettingsCommand;  //change username ip port
    Command sendMessageCommand;
    Command goToTrilizaCommand;
    Command goToChatCommand;
    Command sendChoiseCommand;
    Command buyFromBankCommand;
    Command buyThroughGameServerCommand;
    Command continueGameCommand;

    private Form signIn;
    private Form playForm;
    private Form waitForConnectionForm;
    Form trilizaForm;
    Form chatForm;
    Form resultForm;
    Form haltForm;
    Form buyForm;
    Form continueGameForm;
    Form timeExpiredForm;

    StringItem resultOfGame;
    StringItem waitOpponentString;
    StringItem haltMessage;
    StringItem buyMessage;
    StringItem continueMessage;


    Image playImage;
    Image waitImage;

    private TextField userName,ip ,port;    
    private Records rs;
    User player;

    private TextField bankUsername,bankPassword,money;

    TrilizaProcess trilizaGame;
    ChatProcess chat;
    BuyFromBank buyFromBank;

    SocketConnection serverSocket=null;
    OutputStream serverOut;
    InputStream serverIn;

    boolean connectionWithServerIsOk = false;
    String resultOfBuyFromTheBank = null;
    boolean notifyChat = false;

    String myIp;



    public Start() {
        display = Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.EXIT, 0);                     //initialize commands
        playCommand = new Command("Play", Command.ITEM, 1);
        changeSettingsCommand = new Command("Change Settings", Command.ITEM, 1);
        goToTrilizaCommand = new Command("ToTriliza",Command.BACK,3);
        goToChatCommand = new Command("ToChat",Command.BACK,3);
        sendMessageCommand = new Command("SendMessage",Command.OK,3);
        loginCommand = new Command("Login", Command.OK, 2);
        sendChoiseCommand = new Command("SendChoise", Command.OK, 2);
        buyFromBankCommand = new Command("Direct Buy",Command.OK,2);
        buyThroughGameServerCommand = new Command("Buy from Game Server",Command.OK,2);
        continueGameCommand = new Command("Continue",Command.OK,2);

        userName = new TextField("LoginID:", "", 30, TextField.ANY);
        ip = new TextField("Server ip", "", 30, TextField.ANY);
        port = new TextField("Server port", "", 30, TextField.ANY);
    }

    public void startApp() {
                                                                                //initialize buyForm

        rs = new Records();
        if(rs.checkIfExists("MyRs") == false){
            rs.openRecStore("MyRs");
            SignIn();
        }
        else{
            rs.openRecStore("MyRs");//an yparxei hdh i bash mas me ta stoixeia mas
            player = new User();
            player.username = rs.readUsername();
            player.ip = rs.readIp();
            player.port = rs.readPort();
            rs.closeRecStore();
            System.out.println("database exists!!");
            playScreen();
            display.setCurrent(playForm);
        }
        //initializeBuyCoinsForm();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void SignIn(){
        signIn = new Form("Sign in");
        signIn.append(userName);
        signIn.append(ip);
        signIn.append(port);
        signIn.addCommand(loginCommand);
        signIn.addCommand(exitCommand);
        signIn.setCommandListener(this);
        display.setCurrent(signIn);
    }

    public void InsertUser(String username, String ip , String port) {
        if(username.equals("") || ip.equals("") || port.equals(""))
        {
            tryAgain();
        }
        else{
            rs.insertRecord(username, ip, port);
            
            player = new User();
            player.username = rs.readUsername();
            player.ip = rs.readIp();
            player.port = rs.readPort();
            rs.closeRecStore();
            showMsg();
        }
    }

    public void commandAction(Command c, Displayable s) {
        if (c == exitCommand) {
            closeServerConnection();
            destroyApp(true);
            notifyDestroyed();
        }
        else if(c== loginCommand)
        {
            InsertUser(userName.getString() , ip.getString() , port.getString());
        }
        else if(c==playCommand){
            WaitForConnectionToBeEstablished();
            display.setCurrent(waitForConnectionForm);
            Object lock = new Object();
            trilizaGame = new TrilizaProcess(player,this,lock);
            trilizaGame.execute();
            chat = new ChatProcess(this,lock);
            chat.execute();
            Thread conThread = new Thread(new startConnectionWithServer(this,lock));
            conThread.start();
        }
        else if(c==changeSettingsCommand){
            try{
                rs.closeRecStore();                                             /*Prosoxhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh!!!!!!!!!!1*/
                RecordStore.deleteRecordStore("MyRs");
            }
            catch(Exception e){
                System.err.println(e.toString()+"  deleting the whole recordstore");
            }
            userName = new TextField("LoginID:", "", 30, TextField.ANY);
            ip = new TextField("Server ip", "", 30, TextField.ANY);
            port = new TextField("Server port", "", 30, TextField.ANY);
            rs.openRecStore("MyRs");
            SignIn();
        }
        else if(c == goToChatCommand){
            display.setCurrent(chatForm);
        }
        else if(c== goToTrilizaCommand){
            display.setCurrent(trilizaForm);
        }
        else if(c == sendChoiseCommand){
            trilizaGame.setToSend();
        }
        else if(c == sendMessageCommand){
            chat.sendMSG();
        }
        else if(c==buyFromBankCommand){
            try{
                buyFromBank = new BuyFromBank(bankUsername.getString(), bankPassword.getString(), Integer.parseInt(money.getString()) , this);
                buyFromBank.execute();
          
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(c==continueGameCommand){
            try{
                String result = resultOfBuyFromTheBank;
                System.out.println("result of buy Start == "+result);

                //Displayable currentForm = display.getCurrent();
                if(result.equalsIgnoreCase("success")){
                    serverOut.write("updateCreditsFromBank".getBytes());
                    System.out.println("updateCreditsFromBank");
                    serverOut.write(player.username.getBytes());
                    display.setCurrent(trilizaForm);
                }
                else if(result.equalsIgnoreCase("Not enough money")){
                    Alert error = new Alert("error");
                    error.setType(AlertType.INFO);
                    error.setString("You do not have enough money");
                    //display.setCurrent(error , currentForm);
                    display.setCurrent(error , buyForm);
                }
                else if(result.equalsIgnoreCase("User does not exist")){
                    Alert error = new Alert("error");
                    error.setType(AlertType.INFO);
                    error.setString("Wrong username or password");
                    //display.setCurrent(error , currentForm);
                    display.setCurrent(error , buyForm);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
        else if(c==buyThroughGameServerCommand){
            try{
                serverOut.write("buyCoins\n".getBytes());
                serverOut.write((bankUsername.getString()+"\n").getBytes());
                serverOut.write((bankPassword.getString()+"\n").getBytes());
                serverOut.write((money.getString()+"\n").getBytes());
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void showMsg() {
        Alert success = new Alert("Login Successfully");
        success.setType(AlertType.INFO);
        success.setString("Your Login Process is completed!");
        success.setTimeout(2500);
        userName.setString("");
        ip.setString("");
        port.setString("");
        playScreen();
        display.setCurrent(success , playForm);


    }
    public void wrongInput() {
        Alert success = new Alert("Wrong Input");
        success.setType(AlertType.INFO);
        success.setString("Avalaible inputs 0-9");
        success.setTimeout(40000);
        display.setCurrent(success , trilizaForm);


    }
    
    public void FullBox() {
        Alert success = new Alert("Wrong Input");
        success.setType(AlertType.INFO);
        success.setString("The box you chose is already full\n Please give another input");
        success.setTimeout(4000);
        display.setCurrent(success , trilizaForm);
    }



    public void ShowResult(String r) {
        try{
            resultForm = new Form("The game is over.");
            resultOfGame = new StringItem("Result = ",r);
            resultForm.insert(resultForm.size() , resultOfGame);
            resultForm.addCommand(exitCommand);
            resultForm.setCommandListener(this);
            display.setCurrent(resultForm);
            chat.stop();
        }
        catch(Exception e){
            System.err.println(e.toString()+"ShowResult Screen error");
        }
    }

    public void tryAgain() {
        Alert error = new Alert("Insert Error");
        error.setType(AlertType.INFO);
        error.setString("You have to fill all the spaces");
        userName.setString("");
        ip.setString("");
        port.setString("");
        display.setCurrent(error , signIn);
    }

    public void playScreen(){
        try{
            playForm = new Form("Select Play to start Game");
            playImage = Image.createImage("/Play-icon2.png");
            playForm.append(playImage);
            playForm.addCommand(playCommand);
            playForm.addCommand(changeSettingsCommand);
            playForm.addCommand(exitCommand);

        }
        catch(Exception e){
            System.err.println(e.toString()+"PlaySreen error");
        }
        System.out.println("username:"+player.username+"\nport:"+player.port+"\nip:"+player.ip);
        playForm.setCommandListener(this);
    }

    public void WaitForConnectionToBeEstablished(){
        try{
            waitForConnectionForm = new Form("Please wait...");
            waitOpponentString = new StringItem("\n\n\nPlease Wait until server find a opoonent for you,so you can play with him...","");
            waitForConnectionForm.insert(waitForConnectionForm.size() , waitOpponentString);
            
            waitForConnectionForm.addCommand(exitCommand);
        }
        catch(Exception e){
            System.err.println(e.toString()+"PlaySreen error");
        }
        waitForConnectionForm.setCommandListener(this);
    }

    void startConnectionWithServer(){
        try {
            serverSocket = (SocketConnection) Connector.open("socket://"+player.ip+":"+player.port);
            myIp = serverSocket.getLocalAddress();
            System.out.println("Localip = "+myIp);
            serverIn = serverSocket.openInputStream();
            serverOut = serverSocket.openOutputStream();
            connectionWithServerIsOk = true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    void sendModel(){
        try{
            String model="";
            if(display.isColor()){
                model = "withColor ";
            }
            model+=Integer.toString(display.numColors());
            model+=" ";
            model+=Integer.toString(playForm.getHeight());
            model+=" ";
            model+=Integer.toString(playForm.getWidth());
            model+="\n";
            System.out.println(model);
            serverOut.write(model.getBytes());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


     public void Halt() {
        try{
            haltForm = new Form("Game Halted");
            haltMessage = new StringItem("You must wait for at most" , " 2 minutes");
            haltForm.insert(haltForm.size() , haltMessage);
            haltForm.addCommand(exitCommand);
            haltForm.setCommandListener(this);
            display.setCurrent(haltForm);
        }
        catch(Exception e){
            System.err.println(e.toString()+"Halt screen error");
        }
    }

     public void initializeBuyCoinsForm(){
        buyForm = new Form("Buy Coins");
        buyMessage = new StringItem("Please give your account info for the bank" , ":");
        bankUsername= new TextField("Username:", "", 50, TextField.ANY);
        bankPassword = new TextField("Password:", "", 50, TextField.ANY);
        money = new TextField("Money:", "", 10, TextField.DECIMAL);
        buyForm.append(bankUsername);
        buyForm.append(bankPassword);
        buyForm.append(money);
        buyForm.addCommand(buyFromBankCommand);
        buyForm.addCommand(buyThroughGameServerCommand);
        buyForm.addCommand(exitCommand);
        buyForm.setCommandListener(this);
        display.setCurrent(buyForm);
     }


     public void timeLimitExpired(String result){
         timeExpiredForm = new Form("Time limit expired");
         StringItem msg = new StringItem("Result = ",result);
         timeExpiredForm.append(msg);
         timeExpiredForm.addCommand(exitCommand);
         timeExpiredForm.setCommandListener(this);
         trilizaGame.setResult(result);
         trilizaGame.sendResultToServer();
         display.setCurrent(timeExpiredForm);
     }
//     void BuyDirectlyFromBank(ClientServiceService_Stub stub , String username , String password , int money){
//         try{
//            System.out.println("prin thn agora");
//            resultOfBuyFromTheBank=stub.getCredits(username , password , money);
//            System.out.println("Egine i agora apeyueias    buy deirctly from bank");
//         }
//         catch(Exception e){
//             e.printStackTrace();
//         }
  //   }


    void closeServerConnection(){
        try{
            serverSocket.close();
            serverIn.close();
            serverOut.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }




}
