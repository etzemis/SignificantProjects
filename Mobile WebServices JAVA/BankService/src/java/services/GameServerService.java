/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.ResultSet;

/**
 *
 * @author fokos
 */
@WebService()
public class GameServerService {
    ConnectionDB db;
    int coins = 0;

    /**
     * Web service operation
     */
    @WebMethod(operationName = "GetCredits")
    public String GetCredits(@WebParam(name = "username")
    String username, @WebParam(name = "password")
    String password, @WebParam(name = "money")
    int money) {
        int moneyLeft=0;
        int UserId=0;
        boolean flag=false;

        try{

            db = new ConnectionDB();
            db.init();
            ResultSet result = db.getStat().executeQuery (
            "SELECT userid FROM clientProfile WHERE username = '"+username+"' AND password = '"+password+"'");
                result.next();
            if(result!=null){
                UserId= result.getInt("userid");
                ResultSet moneyResult = db.getStat().executeQuery (
                "SELECT * FROM userAccount WHERE userid = "+UserId);
                while(moneyResult.next()){
                    moneyLeft = moneyResult.getInt("money");
                }
                flag=true;
            }
            if(flag==true){
                if(moneyLeft >= money){
                    moneyLeft = moneyLeft-money;
                    db.getStat().executeUpdate(
                    "UPDATE userAccount"
                    +" SET money = "+moneyLeft
                    +" WHERE userid = "+UserId);

                    coins = 10*money;
                    db.close();
                    return "Success";
                }
                else{
                    db.close();
                    return "Not enough money";
                }
            }
            else{
                db.close();
                return "User does not exist";
            }
        }
        catch(Exception e ){
            e.printStackTrace();
            return "Problem occured";
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "init")
    public String init() {
        db = new ConnectionDB();
        try{

            //db.init();
            System.out.println("init invoked");

            return "success";
        }
        catch(Exception e){
            e.printStackTrace();
            return "Problem occured";
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "closeDB")
    public String closeDB() {
        try{
            //db.close();
            return "success";
        }
        catch(Exception e){
            e.printStackTrace();
            return "Problem occured";
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateCoins")
    public String updateCoins(@WebParam(name = "username")
    String username) {
        int money=0;
        int UserId=0;
        boolean flag=false;
        String returnValue=null;
        try{
            db = new ConnectionDB();
            db.init();
System.out.println("1");
            ResultSet result = db.getStat().executeQuery (
            "SELECT * FROM clientProfile WHERE username = '"+username+"'");
System.out.println("2");
            result.next();
System.out.println("3");
            UserId= result.getInt("userid");
System.out.println("4");
            ResultSet moneyResult = db.getStat().executeQuery (
            "SELECT * FROM userAccount WHERE userid = "+UserId);
System.out.println("5");
            moneyResult.next();
            money = moneyResult.getInt("lastcharge");
            db.getStat().executeUpdate(
                    "UPDATE userAccount"
                    +" SET lastcharge = 0"
                    +" WHERE userid = "+UserId);
            System.out.println("Bank: money= "+money);
            returnValue = Integer.toString(money);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        db.close();
        return returnValue;
    }

}
