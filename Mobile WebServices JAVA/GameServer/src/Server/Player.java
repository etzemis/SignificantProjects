
package Server;

public class Player {
    int id;
    String ip;
    int wins;
    int ties;
    int loses;
    int coins;

    public Player(int ID,String IP){
        id=ID;
        ip=IP;
        this.coins=0;                 //παντα αρχικοποιείται με μηδεν και μετα αν θελει βαζει μοναδες(coins)
        wins=0;ties=0;loses=0;
    }

    public boolean hasID(int id){
        if(this.id==id) return true;
        else return false;
    }

    public boolean hasIP(String IP){
        if(ip.equals(IP)) return true;
        else return false;
    }



}

