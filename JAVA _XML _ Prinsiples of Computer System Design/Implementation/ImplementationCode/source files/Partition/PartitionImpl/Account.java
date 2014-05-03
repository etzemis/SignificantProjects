package PartitionImpl;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import Exceptions.NegativeAmountException;

public class Account {
	private int id;
	private double amount = 0.0;;
	ReentrantReadWriteLock Atomicity = new ReentrantReadWriteLock(true);
	
	public Account(){
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getAmount() {
		Atomicity.readLock().lock();
		try{
		return amount;
		}
		finally{
			Atomicity.readLock().unlock();
		}
	}
	public void setAmount(double amount) {
		Atomicity.writeLock().lock();
		try{
			this.amount = amount;
		}
		finally{
			Atomicity.writeLock().unlock();
		}
	}
	
	public void printAccount(){
		System.out.printf("Id %d \tAmount %f\n", this.id,this.amount);
	}
	
	public void credit(double amount) throws NegativeAmountException{
		if(amount < 0.0){
			throw new NegativeAmountException(amount);
		}
		else{
			Atomicity.writeLock().lock();
			try{
				this.amount += amount;
			}
			finally{
				Atomicity.writeLock().unlock();
			}
		}
	}
	
	public void debit(double amount) throws NegativeAmountException{
		if(amount < 0.0){
			throw new NegativeAmountException(amount);
		}
		else{
			
			Atomicity.writeLock().lock();
			try{
				this.amount -= amount;
			}
			finally{
				Atomicity.writeLock().unlock();
			}
		}
	}
	
	public double returnExposure(){
		Atomicity.readLock().lock();
		try{
			if(amount<0.0){
				return amount*(-1.0);
			}
			else return 0.0;
		}
		finally{
			Atomicity.readLock().unlock();
		}
		
	}

}
