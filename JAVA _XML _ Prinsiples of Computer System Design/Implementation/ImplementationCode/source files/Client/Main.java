
import java.io.BufferedReader;
import java.io.InputStreamReader;

import proxyimpl.AccountManagerProxyImpl;
import proxyimpl.AccountManagerProxyImplService;
import proxyimpl.InexistentAccountException_Exception;
import proxyimpl.InexistentBranchException_Exception;
import proxyimpl.NegativeAmountException_Exception;
import proxyimpl.PartitionIsDownException_Exception;



public class Main {
    
        private static AccountManagerProxyImpl port;
        
        
	public static void main(String args []){
                InputStreamReader istream = new InputStreamReader(System.in) ;
                BufferedReader bufRead = new BufferedReader(istream) ;
		
                AccountManagerProxyImplService serv = new AccountManagerProxyImplService();
                port = serv.getAccountManagerProxyImplPort();
                
                String i;
                      
                System.out.println("Welcome To this Client Service");
	
                try {
                    while(true){
                        System.out.println("Press  1: credit    2: debit  3: transfer   4: Calculate exposure   5: exit");
                        String c = bufRead.readLine();
                        if(c.isEmpty()) continue;
                        int choice;
                        try{
                            choice = Integer.parseInt(c);
                        }
                        catch(NumberFormatException e){
                            continue;
                        }
                        try{
                        switch (choice) {
                                case 1: System.out.println("Please Give Branch id");
                                		int Bid,Aid;
                                		double Am;
                                        i = bufRead.readLine();
                                        Bid = Integer.parseInt(i.trim());
                                        System.out.println("Please Give Account id");
                                        i = bufRead.readLine();
                                        Aid = Integer.parseInt(i.trim());
                                        System.out.println("Please Give Ammount");
                                        i = bufRead.readLine();
                                        Am = Double.parseDouble(i.trim());
                                        credit(Bid, Aid, Am);
                                        
                                        break;
                                case 2: System.out.println("Please Give Branch id");
		                                i = bufRead.readLine();
		                                Bid = Integer.parseInt(i.trim());
		                                System.out.println("Please Give Account id");
		                                i = bufRead.readLine();
		                                Aid = Integer.parseInt(i.trim());
		                                System.out.println("Please Give Ammount");
		                                i = bufRead.readLine();
		                                Am = Double.parseDouble(i.trim());
		                                debit(Bid, Aid, Am);
		                                break;
                                case 3: System.out.println("Please Give Branch id");
		                                int DAid,OAid;
                                		i = bufRead.readLine();
		                                Bid = Integer.parseInt(i.trim());
		                                System.out.println("Please Give  From Account id");
		                                i = bufRead.readLine();
		                                OAid = Integer.parseInt(i.trim());
		                                System.out.println("Please Give  To Account id");
		                                i = bufRead.readLine();
		                                DAid = Integer.parseInt(i.trim());
		                                System.out.println("Please Give Ammount");
		                                i = bufRead.readLine();
		                                Am = Double.parseDouble(i.trim());
		                                transfer(Bid,OAid,DAid,Am);
                                        break;
                                case 4: System.out.println("Please Give Branch id");
		                        		i = bufRead.readLine();
		                                Bid = Integer.parseInt(i.trim());
		                                calculateExposure(Bid);
                                        break;
                                case 5: return;    
                                default: break;

                            }
                        } catch (java.io.IOException e) {
                            System.out.println("IO error");
                            continue;
                        }
                        
                    }
                }
                catch (java.io.IOException e) {
                        System.out.println("Error reading line");
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
        
     
    public static void debit(int b, int a, double am){
   	 try {
			port.debit(b, a, am);
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
    
    public static void transfer(int b, int Oa, int Da, double am){
      	 try {
   			port.transfer(b, Oa, Da, am);
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

    public static void calculateExposure(int b){
     	 try {
  			double exp = port.calculateExposure(b);
  			System.out.printf("Exposure is %f\n", exp);
  		} catch (InexistentBranchException_Exception e) {
  			System.out.println("The branch does not exist");
  		} catch (PartitionIsDownException_Exception e) {
  			System.out.println("UnAvailable System for this operation");
		}
      }
 
     

}
                

               
