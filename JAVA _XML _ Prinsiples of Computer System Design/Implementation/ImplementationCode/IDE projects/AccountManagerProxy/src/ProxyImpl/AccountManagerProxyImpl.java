package ProxyImpl;



import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.namespace.QName;

import partitionimpl.AccountManagerPartition;
import partitionimpl.AccountManagerPartitionService;
import partitionimpl.InexistentAccountException_Exception;
import partitionimpl.NegativeAmountException_Exception;

import Exceptions.InexistentAccountException;
import Exceptions.InexistentBranchException;
import Exceptions.NegativeAmountException;
import Exceptions.PartitionIsDownException;

@WebService
public class AccountManagerProxyImpl {
	ArrayList<PartitionInfo> Partitions = new ArrayList<PartitionInfo>();
	private long NoLogs = 0;
	
	public AccountManagerProxyImpl(String InputFileName) throws IOException {
        FileInputStream fstream;
		fstream = new FileInputStream(InputFileName);
		DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String strLine;
        while ((strLine = br.readLine()) != null)   {


          /*** insert data into partition  ****/  
        	if(strLine.equals("partition")){		// Start of a new branch
        		PartitionInfo PI = new PartitionInfo();
        		/* read URL*/
        		strLine = br.readLine();
        		/**************************   DO SOMETHING WITH THE URLS     ********************************/
        		URL baseUrl = partitionimpl.AccountManagerPartitionService.class.getResource(".");
                URL url = new URL(baseUrl, strLine);
        		
        		AccountManagerPartitionService ser = new AccountManagerPartitionService(url,new QName("http://PartitionImpl/", "AccountManagerPartitionService") );
        		AccountManagerPartition P = ser.getAccountManagerPartitionPort();
        		PI.setP(P);
        		PI.setState(true);
        		Partitions.add(PI);
        	}
        	else{								//account to be inserted
        		Partitions.get(Partitions.size() - 1).addBrId(Integer.parseInt(strLine));
        	}
        }
        in.close();
	}
	
	public void PrintBranchAndAccounts(){
		 for(int i=0; i<Partitions.size(); i++){
			 if(Partitions.get(i).isState() == false){
					continue;
			 }
			 Partitions.get(i).getP().printBranchAndAccounts();
		 }
	 }
	 
	@WebMethod
	public void credit(int branchId, int accountId, double amount) throws InexistentBranchException, InexistentAccountException, NegativeAmountException, PartitionIsDownException{
		for(int i=0; i<Partitions.size(); i++){
			if(Partitions.get(i).hasBr(branchId)){
				try {
					if(Partitions.get(i).isState() == false){
						throw new PartitionIsDownException();
					}
					Partitions.get(i).getP().credit(branchId, accountId, amount);
					NoLogs++;
					return;
				} catch (InexistentAccountException_Exception e) {
					throw new InexistentAccountException();
				} catch (NegativeAmountException_Exception e) {
					throw new NegativeAmountException();
				} catch (Exception e){   /*The partition is down*/
					Partitions.get(i).setState(false);
					throw new PartitionIsDownException();
				}
			}
		}
		throw new InexistentBranchException();
	}
	
	@WebMethod
	public void debit(int branchId, int accountId, double amount) throws InexistentBranchException, InexistentAccountException, NegativeAmountException, PartitionIsDownException{
		for(int i=0; i<Partitions.size(); i++){
			if(Partitions.get(i).hasBr(branchId)){
				try {
					if(Partitions.get(i).isState() == false){
						throw new PartitionIsDownException();
					}
					Partitions.get(i).getP().debit(branchId, accountId, amount);
					return;
				} catch (InexistentAccountException_Exception e) {
					throw new InexistentAccountException();
				} catch (NegativeAmountException_Exception e) {
					throw new NegativeAmountException();
				} catch (Exception e){   /*The partition is down*/
					Partitions.get(i).setState(false);
					throw new PartitionIsDownException();
				}
			}
		}
		throw new InexistentBranchException();
	}
	
	@WebMethod		
	public void transfer(int branchId, int accountIdOrig, int accountIdDest,double amount) throws InexistentBranchException, InexistentAccountException, NegativeAmountException, PartitionIsDownException{
		for(int i=0; i<Partitions.size(); i++){
			if(Partitions.get(i).hasBr(branchId)){
				try {
					if(Partitions.get(i).isState() == false){
						throw new PartitionIsDownException();
					}
					Partitions.get(i).getP().transfer(branchId, accountIdOrig, accountIdDest, amount);
					return;
				} catch (InexistentAccountException_Exception e) {
					throw new InexistentAccountException();
				} catch (NegativeAmountException_Exception e) {
					throw new NegativeAmountException();
				} catch (Exception e){   /*The partition is down*/
					Partitions.get(i).setState(false);
					throw new PartitionIsDownException();
				}
			}
		}
		throw new InexistentBranchException();
	}
	
	@WebMethod					
	public double calculateExposure(int branchId) throws InexistentBranchException, PartitionIsDownException{
		double e = 0.0;
		for(int i=0; i<Partitions.size(); i++){
			if(Partitions.get(i).hasBr(branchId)){
				if(Partitions.get(i).isState() == false){
					throw new PartitionIsDownException();
				}
				try{
					e = Partitions.get(i).getP().calculateExposure(branchId);
					return e;
				}
				catch (Exception ee){   /*The partition is down*/
					Partitions.get(i).setState(false);
					throw new PartitionIsDownException();
				}
			}
		}
		throw new InexistentBranchException();
	}

	public long getNoLogs() {
		// TODO Auto-generated method stub
		return NoLogs;
	}
}
