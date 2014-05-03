
package bankshell;
import java.sql.*;




public class ConnectionDB{
    private static final String dbUrl = "jdbc:mysql://localhost:3306/BankServer";
    private static final String dbDriver = "com.mysql.jdbc.Driver";
    private static final String userId = "root";
    private static final String password = "pasxa1989";
    private static int AccountID = 1;

    private Connection conn;
    private Statement stat;

     public void setConn(Connection conn)
    {
        this.conn = conn;
    }

     public void setStat(Statement stat)
    {
        this.stat = stat;
    }

    // getters
    public Connection getConn()
    {
        return conn;
    }

     public Statement getStat()
    {
        return stat;
    }



    public void init() throws SQLException
    {
        try
        {
            Class.forName(dbDriver);
        }
        catch(Exception e)
        {
            System.err.println("Cannot load db driver");
            System.exit(-1);
        }
        try
        {
            setConn(DriverManager.getConnection(dbUrl, userId, password));
            setStat(getConn().createStatement());
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

    }

    public void close()
    {
        try
        {
            if(getStat()!=null)
                getStat().close();
            if(getConn()!=null)
                getConn().close();
        }
        catch(Exception e)
        {}
    }



    public void InsertRec(String name , String surname , String username , String password , String money){
        System.out.println(name+surname+username+password+money);
        int UserId;
        try{    
            stat.executeUpdate (
                "INSERT INTO clientProfile (name , surname , username , password)"
                + " VALUES"
                + "('"+name+"' , '"+surname+"' , '"+username+"' , '"+password+"')");


            ResultSet result = stat.executeQuery (
                "SELECT userid FROM clientProfile WHERE username = '"+username+"'");

            result.next();
            UserId= result.getInt("userid");
            int m  = Integer.parseInt(money);
            String account = "insert into userAccount values("+AccountID+","+UserId+","+m+" , 0)";
            AccountID++;
            System.out.println(account);
            stat.executeUpdate(account);
        }
        catch(Exception e ){
            e.printStackTrace();
        }
    }




}
