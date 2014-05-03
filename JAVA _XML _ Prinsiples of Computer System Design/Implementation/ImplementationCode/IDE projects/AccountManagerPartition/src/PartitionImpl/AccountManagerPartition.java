package PartitionImpl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;

import Exceptions.InexistentAccountException;
import Exceptions.NegativeAmountException;

@WebService
public class AccountManagerPartition {
	private ArrayList<Branch> branches = new ArrayList<Branch>();
	
	public AccountManagerPartition(String InputFileName) throws IOException {
		//read file and fill in accounts and branches
        
        FileInputStream fstream;
		fstream = new FileInputStream(InputFileName);
		DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String strLine;
        while ((strLine = br.readLine()) != null)   {

            String[] temp = new String [2];
            try{

                temp = strLine.split("\t");
                temp[0] = temp[0].trim();
                temp[1] = temp[1].trim();
            }
            catch(IndexOutOfBoundsException e){
                System.out.println("String error");
            }
          /*** insert data into partition  ****/  
        	if(temp[0].equals("branch")){		// Start of a new branch
        		Branch B = new Branch();
        		B.setId(Integer.parseInt(temp[1]));
        		branches.add(B);
        	}
        	else{								//account to be inserted
        		Account ac = new Account();
        		ac.setId(Integer.parseInt(temp[0]));
        		ac.setAmount(Double.parseDouble(temp[1]));
        		branches.get(branches.size() - 1).addAccount(ac);
        	}
        }
        in.close();
	}
	@WebMethod
	public void printBranchAndAccounts(){
		for(int i=0; i<branches.size();i++){
			System.out.printf("Branch %d Accounts: \n", branches.get(i).getId());
			branches.get(i).printAccounts();
		}
	}
	
	@WebMethod
	public void credit(int branchId, int accountId, double amount) throws InexistentAccountException, NegativeAmountException{
		
		int index = GetBranchIterator(branchId);
		branches.get(index).credit(accountId, amount);
	}
	
	@WebMethod
	public void debit(int branchId, int accountId, double amount) throws InexistentAccountException, NegativeAmountException{

		int index = GetBranchIterator(branchId);
		branches.get(index).debit(accountId, amount);
	}
	
	@WebMethod		
	public void transfer(int branchId, int accountIdOrig, int accountIdDest,double amount) throws InexistentAccountException, NegativeAmountException{

		int index = GetBranchIterator(branchId);
		branches.get(index).transfer(accountIdOrig, accountIdDest, amount);
		
	}
	
	@WebMethod					
	public double calculateExposure(int branchId){

		int index = GetBranchIterator(branchId);
		return branches.get(index).calculateExposure();
		
	}
	
	public int GetBranchIterator(int branchId){
		for(int i=0; i<branches.size(); i++){
			if(branches.get(i).getId() == branchId){
				return i;
			}
		}
		return -1;			// if not found
	}

}
