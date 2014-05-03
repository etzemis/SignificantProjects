import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.ws.Endpoint;

import Exceptions.InexistentAccountException;
import Exceptions.InexistentBranchException;
import Exceptions.NegativeAmountException;
import Exceptions.PartitionIsDownException;
import ProxyImpl.AccountManagerProxyImpl;
import ProxyImpl.TestingThroughput;

import partitionimpl.AccountManagerPartition;
import partitionimpl.AccountManagerPartitionService;
import partitionimpl.PrintBranchAndAccounts;

public class Main {


	public static void main(String[] args) throws IOException, InexistentBranchException, InexistentAccountException, NegativeAmountException, PartitionIsDownException {
		InputStreamReader istream = new InputStreamReader(System.in) ;
        BufferedReader bufRead = new BufferedReader(istream) ;
        
        System.out.println("Welcome to this Proxy!\nPlease Give the name of the Input file");
        String c = bufRead.readLine();
		//AccountManagerProxyImpl ProxyI = new AccountManagerProxyImpl(c);
        AccountManagerProxyImpl ProxyI = new AccountManagerProxyImpl(c);
        TestingThroughput T = new TestingThroughput(ProxyI);
        new Thread(T).start();
		Endpoint.publish("http://localhost:12349/AccountManagerProxy1", ProxyI);
	}

}
