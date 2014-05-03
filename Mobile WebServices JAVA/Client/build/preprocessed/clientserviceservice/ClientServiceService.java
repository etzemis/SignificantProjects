package clientserviceservice;
import javax.xml.namespace.QName;

public interface ClientServiceService extends java.rmi.Remote {

    /**
     *
     */
    public String getCredits(String username, String password, int money) throws java.rmi.RemoteException;

}
