/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package graphicInterface;
import javax.microedition.lcdui.*;
import clientserviceservice.*;
/**
 *
 * @author vaggelis
 */
public class BuyFromBank implements Runnable{
    boolean flag = false;
    Thread thread;
    String username,password;
    int money;
    Start midlet;
    String result;
    ClientServiceService_Stub stub;

    public BuyFromBank(String username , String password , int money , Start midlet){
        this.username = username;
        this.password = password;
        this.money = money;
        this.midlet = midlet;
    }

    public void execute(){
        thread = new Thread(this);
        try{
            System.out.println("2. BankThread Start...");
            thread.start();
        }
        catch(Exception e){
            System.err.println("bankProcess"+e.toString());
        }
    }

    public void run(){
        try{
            stub = new ClientServiceService_Stub();
            System.out.println("prin thn agora");
            midlet.resultOfBuyFromTheBank=stub.getCredits(username , password , money);
//            midlet.InitializeContinueForm();
//            midlet.display.setCurrent(midlet.continueGameForm);
            System.out.println("Egine i agora apeyueias    buy deirctly from bank");

                String result = midlet.resultOfBuyFromTheBank;
                System.out.println("result of buy Start == "+result);

                //Displayable currentForm = display.getCurrent();
                if(result.equalsIgnoreCase("success")){
                    midlet.serverOut.write("updateCreditsFromBank\n".getBytes());
                    System.out.println("updateCreditsFromBank");
                    midlet.serverOut.write((username+"\n").getBytes());
                }
                else if(result.equalsIgnoreCase("Not enough money")){
                    Alert error = new Alert("error");
                    error.setType(AlertType.INFO);
                    error.setString("You do not have enough money");
                    //display.setCurrent(error , currentForm);
                    midlet.display.setCurrent(error , midlet.buyForm);
                }
                else if(result.equalsIgnoreCase("User does not exist")){
                    Alert error = new Alert("error");
                    error.setType(AlertType.INFO);
                    error.setString("Wrong username or password");
                    //display.setCurrent(error , currentForm);
                    midlet.display.setCurrent(error , midlet.buyForm);
                }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
