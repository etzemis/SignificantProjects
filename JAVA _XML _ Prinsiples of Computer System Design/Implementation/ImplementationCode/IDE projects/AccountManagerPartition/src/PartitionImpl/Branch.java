package PartitionImpl;

import java.util.ArrayList;
import Exceptions.InexistentAccountException;
import Exceptions.NegativeAmountException;

public class Branch {
	private ArrayList<Account> accounts = new ArrayList<Account>();
	private int id;
	
	public Branch(){
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void addAccount(Account a) {
		accounts.add(a);
	}
	
	public void printAccounts(){
		for(int i=0; i<accounts.size();i++){
			accounts.get(i).printAccount();
		}
	}
	
	

	public void credit(int accountId, double amount) throws InexistentAccountException, NegativeAmountException{
		int index;
		if((index = GetAccountIterator(accountId)) == -1){
			throw new InexistentAccountException(accountId);
		}
		else{
			accounts.get(index).credit(amount);
		}
	}
	
	public void debit(int accountId, double amount) throws InexistentAccountException, NegativeAmountException{
		int index;
		if((index = GetAccountIterator(accountId)) == -1){
			throw new InexistentAccountException(accountId);
		}
		else{
			accounts.get(index).debit(amount);
		}
	}
	
	public void transfer(int accountIdOrig, int accountIdDest,double amount) throws InexistentAccountException, NegativeAmountException{
		int index1, index2;
		if((index1 = GetAccountIterator(accountIdOrig)) == -1){
			throw new InexistentAccountException(accountIdOrig);
		}
		else if((index2 = GetAccountIterator(accountIdDest)) == -1){
			throw new InexistentAccountException(accountIdDest);
		}
		else{
			accounts.get(index1).debit(amount);
			accounts.get(index2).credit(amount);
		}
	}

	public int GetAccountIterator(int accountId){
		for(int i=0; i<accounts.size(); i++){
			if(accounts.get(i).getId() == accountId){
				return i;
			}
		}
		return -1;			// if not found
	}
	
	public double calculateExposure(){
		double ExposureSum = 0.0;
		for(int i=0;i<accounts.size();i++){
			ExposureSum += accounts.get(i).returnExposure();
		}
		return ExposureSum;
	}

}
