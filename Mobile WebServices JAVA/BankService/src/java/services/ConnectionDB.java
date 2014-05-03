
package services;
import java.sql.*;




public class ConnectionDB{
    private static final String dbUrl = "jdbc:mysql://localhost:3306/BankServer";
    private static final String dbDriver = "com.mysql.jdbc.Driver";
    private static final String userId = "root";
    private static final String password = "pasxa1989";


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






}
