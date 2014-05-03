
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import proxyimpl.AccountManagerProxyImpl;
import proxyimpl.AccountManagerProxyImplService;
import proxyimpl.InexistentAccountException_Exception;
import proxyimpl.InexistentBranchException_Exception;
import proxyimpl.NegativeAmountException_Exception;
import proxyimpl.PartitionIsDownException_Exception;



public class Main {
    
        private static AccountManagerProxyImpl port;
        private static int NoBranches = 12;
        
        
	public static void main(String args []){
                InputStreamReader istream = new InputStreamReader(System.in) ;
                BufferedReader bufRead = new BufferedReader(istream) ;
                Random generator = new Random();
		
                AccountManagerProxyImplService serv = new AccountManagerProxyImplService();
                port = serv.getAccountManagerProxyImplPort();
                
                String i;
                      
                System.out.println("This Client runs until to be interupted credit requests to the Bank System");
	

                while(true){
            		int Bid,Aid;
            		double Am;
                    
                    Bid = generator.nextInt(NoBranches) +1;                    
                    Aid = generator.nextInt(25)+1;    
//                    System.out.printf("%d %d \n",Bid,Aid);
                    Am = 0.5;
                    credit(Bid, Aid, Am);
                    try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}                    
                }

                        

			
     }
      
     public static void credit(int b, int a, double am){
    	 try {
			port.credit(b, a, am);
		} catch (InexistentAccountException_Exception e) {
			System.out.println("The account does not exist");
		} catch (InexistentBranchException_Exception e) {
			System.out.println("The branch does not exist");
		} catch (NegativeAmountException_Exception e) {
			System.out.println("The amount has to be positive");
		} catch (PartitionIsDownException_Exception e) {
			System.out.println("UnAvailable System for this operation");
		}
     }
        
     
            
    public static void print(){
    	port.printBranchAndAccounts();
    }
       
     

}
                

               
