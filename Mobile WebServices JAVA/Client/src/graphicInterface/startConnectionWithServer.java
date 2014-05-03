
package graphicInterface;

public class startConnectionWithServer implements Runnable{

    private Start midlet;
    Object lock;

    public startConnectionWithServer(Start midlet,Object lock){
        this.midlet=midlet;
        this.lock = lock;
    }


    public void run(){
        try{
            System.out.println("startConnection thread started");
            midlet.startConnectionWithServer();
            midlet.sendModel();
//
//            synchronized(lock){
//                lock.notifyAll();
//            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
