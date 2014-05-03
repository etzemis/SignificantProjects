
package Server;

public class Pairs {
    int player1,player2,id;
    //String result;
    Thread threadServing;
    
    public Pairs(int i,int j,int ID,Thread thread){
        player1=i;
        player2=j;
        id=ID;
        //result=null;
        threadServing=thread;
    }

    public void print(){
        System.out.println("Game"+id+" peer"+player1+" VS "+"peer"+player2);
    }

    public boolean hasID(int id){
        if(this.id == id) return true;
        return false;
    }

//    public String getResult(){
//        return result;
//    }

//    public void setResult(String r){
//        result=r;
//    }
}

