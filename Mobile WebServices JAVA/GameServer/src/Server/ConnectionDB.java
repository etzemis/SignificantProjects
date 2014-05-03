
package Server;
import java.sql.*;
import java.sql.ResultSet;




public class ConnectionDB{
    private static final String dbUrl = "jdbc:mysql://localhost:3306/GameServer";
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
        try{
//            int count = stat.executeUpdate (
//               "INSERT INTO statistics (id ,ip, wins , ties , loses , coins)"
//               + " VALUES"
//               + "('1','a', '5', '10' , '9' , '6'),"
//               + "('2', 'a','6','2','7','8')");
            stat.executeUpdate(
                    "UPDATE statistics"
                    +" SET wins=wins+1"
                    +" WHERE id='1'");

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

    public void insertIntoStatistics(Player p){
       try{
        synchronized(Server.dbLock){
           stat.executeUpdate (
                "INSERT INTO statistics (ip , wins , ties , loses , coins )"
                + " VALUES"
                + "('"+p.ip+"' , "+p.wins+" , "+p.ties+" , "+p.loses+" , "+p.coins+")");
           }
        }
       catch(Exception e){
           e.printStackTrace();
       }
    }


    public void updateStats(int id,boolean opposite,String result){         //update stats vector


        try{
            synchronized(Server.dbLock){
                if(opposite==false){
                    if(result.equalsIgnoreCase("win"))
                        stat.executeUpdate(
                            "UPDATE statistics"
                            +" SET wins=wins+1"
                            +" WHERE id="+id);
                    else if(result.equalsIgnoreCase("tie"))
                        stat.executeUpdate(
                            "UPDATE statistics"
                            +" SET ties=ties+1"
                            +" WHERE id="+id);
                    else
                        stat.executeUpdate(
                            "UPDATE statistics"
                            +" SET loses=loses+1"
                            +" WHERE id="+id);
                }
                else{
                    if(result.equalsIgnoreCase("win"))
                        stat.executeUpdate(
                            "UPDATE statistics"
                            +" SET loses=loses+1"
                            +" WHERE id="+id);
                    else if(result.equalsIgnoreCase("tie"))
                        stat.executeUpdate(
                            "UPDATE statistics"
                            +" SET ties=ties+1"
                            +" WHERE id="+id);
                    else
                        stat.executeUpdate(
                            "UPDATE statistics"
                            +" SET wins=wins+1"
                            +" WHERE id="+id);
                }
            }
        }
        catch(Exception e){
                e.printStackTrace();
        }
    }



    public void printStatistics(){
        try{
            synchronized(Server.dbLock){
                ResultSet result = stat.executeQuery (
                "SELECT * FROM statistics");
                System.out.println("\tID\t|\tIP\t|\tGames\t|\tWins\t|\tties\t|\tloses\t|\tper");
                while(result.next()){
                    int wins = result.getInt("wins");
                    int ties = result.getInt("ties");
                    int loses = result.getInt("loses");
                    int games=wins+ties+loses;
                    int percentage;
                    if(wins+loses == 0) percentage = 0;
                    else percentage = wins/(wins+loses);
                    System.out.println("\t"+result.getInt("id")+"\t|\t"+result.getString("ip")+"\t|\t"+games+"\t|\t"+wins+"\t|\t"+ties+"\t|\t"+loses+"\t|\t"+percentage);

                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }




}