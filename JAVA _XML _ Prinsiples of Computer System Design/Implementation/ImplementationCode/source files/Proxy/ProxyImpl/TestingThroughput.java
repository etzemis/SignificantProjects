package ProxyImpl;



public class TestingThroughput implements Runnable{
    private AccountManagerProxyImpl P;
    
    public TestingThroughput(AccountManagerProxyImpl P){
        this.P = P;
    }
    
    public void run(){
        long previousNoLogs = 0;;
        long CurrentNoLogs = 0;
        int totalSeconds = 0;
        while(true){
            previousNoLogs = P.getNoLogs();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            CurrentNoLogs = P.getNoLogs();
            totalSeconds++;
            if(totalSeconds%10 == 0){
                //System.out.printf("For the last %d seconds there have been %d logs completed\n", totalSeconds, CurrentNoLogs);
            }
        }
        
    }
    
}
