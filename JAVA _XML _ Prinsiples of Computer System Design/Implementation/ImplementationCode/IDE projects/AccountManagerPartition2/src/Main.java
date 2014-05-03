import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.ws.Endpoint;

import PartitionImpl.AccountManagerPartition;


public class Main {

	public static void main(String[] args) throws IOException {
		InputStreamReader istream = new InputStreamReader(System.in) ;
        BufferedReader bufRead = new BufferedReader(istream) ;
        
        System.out.println("Welcome to this partition!\nPlease Give the name of the Input file");
        String c = bufRead.readLine();
		AccountManagerPartition P = new AccountManagerPartition(c);
		P.printBranchAndAccounts();
		Endpoint.publish("http://localhost:12348/AccountManagerPartition2", P);

	}

}
