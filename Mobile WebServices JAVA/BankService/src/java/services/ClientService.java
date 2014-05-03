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
public class ClientService {

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
        ConnectionDB db = new ConnectionDB();
        try{
            System.out.println("mesa sthn get gredits");
            db.init();
            ResultSet result = db.getStat().executeQuery (
            "SELECT * FROM clientProfile WHERE username = '"+username+"' AND password = '"+password+"'");
            if(result!=null){
                result.next();
            //if(result!=null){
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
                    db.getStat().executeUpdate(
                    "UPDATE userAccount"
                    +" SET lastcharge = "+money
                    +" WHERE userid = "+UserId);
                    return "Success";
                }
                else return "Not enough money";
            }
            else return "User does not exist";
        }
        catch(Exception e ){
            e.printStackTrace();
            return "Problem occured";
        }
        finally{
            db.close();            //return "Problem occured";
        }

    }

}
