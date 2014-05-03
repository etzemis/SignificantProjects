package Exceptions;

public class PartitionIsDownException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PartitionIsDownException() {
		// TODO Auto-generated constructor stub
		super("The account does not exist");
	}

}
