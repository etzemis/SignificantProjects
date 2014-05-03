package bankshell;
import java.io.*;
import java.sql.*;

public class Main{
static ConnectionDB db;
static BufferedReader standardIn;
    public static void main(String[] args)throws IOException{
        String cmd = null;
        int i;

        db = new ConnectionDB();
        try{
            db.init();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        standardIn = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            System.out.println("Available commands: insert (if you want to insert a new user to the database), get-balance 'username' , exit");
            try{
                cmd = standardIn.readLine();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            if(cmd.indexOf(' ')!= -1){
                String t=cmd.substring(0,cmd.indexOf(' '));
                if(t.equalsIgnoreCase("get-balance")){
                    t = cmd.substring(cmd.indexOf(" ")+1);
                    getBalance(t);
                }
            }
            if(cmd.equalsIgnoreCase("insert")){
                insertUser();
            }
            if(cmd.equalsIgnoreCase("get-balance")){
                insertUser();
            }
            if(cmd.equalsIgnoreCase("exit")){
                db.close();
                break;
            }
        }
    }

    public static void insertUser(){
        String name , surname , username , password , money;
        try{
            System.out.println("Name:");
            name = standardIn.readLine();
            System.out.println("SurName:");
            surname = standardIn.readLine();
            System.out.println("Username:");
            username = standardIn.readLine();
            System.out.println("Password:");
            password = standardIn.readLine();
            System.out.println("Xrhmata:");
            money = standardIn.readLine();

            db.InsertRec(name, surname, username, password, money);
//            db.InsertRec("a", "a", "a", "a", "1");
        }
        catch(IOException e){
            e.printStackTrace();
        }


    }

    public static void update(){

        try{

            System.out.println("id:");
            String UserId = standardIn.readLine();
            System.out.println("money:");
            String moneyLeft = standardIn.readLine();

//            String sql = "UPDATE userAccount SET left = ? WHERE userid = ?";
//
//            PreparedStatement prest = db.getConn().prepareStatement(sql);
//            prest.setInt(1,10);
//            prest.setInt(2,1);
//            prest.executeUpdate();

            //int rows = db.getStat().executeUpdate( "UPDATE userAccount SET left = 9842 WHERE left = 1000" ) ;

            // Print how many rows were modified
            //System.out.println( rows + " Rows modified" ) ;

             db.getStat().executeUpdate(
                    "update userAccount "
                    +" set money = 1000 where money = 10");
                    //+" where userid = "+UserId);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

static void getBalance(String s){
    try{
        int UserId,money=0;
        ResultSet result = db.getStat().executeQuery (
        "SELECT * FROM clientProfile WHERE username = '"+s+"'");
        if(result.next()){
            UserId= result.getInt("userid");
            ResultSet moneyResult = db.getStat().executeQuery (
            "SELECT * FROM userAccount WHERE userid = "+UserId);
            while(moneyResult.next()){
                money = moneyResult.getInt("money");
            }
        System.out.println(s+" has "+money+" $");
        }
        else System.out.println("User doesn't exist");
    }
    catch(Exception e){
        e.printStackTrace();
    }

}


 

}
