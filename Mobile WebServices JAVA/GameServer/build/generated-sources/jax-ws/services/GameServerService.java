
package services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2-hudson-752-
 * Generated source version: 2.2
 * 
 */
@WebService(name = "GameServerService", targetNamespace = "http://services/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface GameServerService {


    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "init", targetNamespace = "http://services/", className = "services.Init")
    @ResponseWrapper(localName = "initResponse", targetNamespace = "http://services/", className = "services.InitResponse")
    @Action(input = "http://services/GameServerService/initRequest", output = "http://services/GameServerService/initResponse")
    public String init();

    /**
     * 
     * @param username
     * @param money
     * @param password
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetCredits")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "GetCredits", targetNamespace = "http://services/", className = "services.GetCredits")
    @ResponseWrapper(localName = "GetCreditsResponse", targetNamespace = "http://services/", className = "services.GetCreditsResponse")
    @Action(input = "http://services/GameServerService/GetCreditsRequest", output = "http://services/GameServerService/GetCreditsResponse")
    public String getCredits(
        @WebParam(name = "username", targetNamespace = "")
        String username,
        @WebParam(name = "password", targetNamespace = "")
        String password,
        @WebParam(name = "money", targetNamespace = "")
        int money);

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "closeDB", targetNamespace = "http://services/", className = "services.CloseDB")
    @ResponseWrapper(localName = "closeDBResponse", targetNamespace = "http://services/", className = "services.CloseDBResponse")
    @Action(input = "http://services/GameServerService/closeDBRequest", output = "http://services/GameServerService/closeDBResponse")
    public String closeDB();

    /**
     * 
     * @param username
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "updateCoins", targetNamespace = "http://services/", className = "services.UpdateCoins")
    @ResponseWrapper(localName = "updateCoinsResponse", targetNamespace = "http://services/", className = "services.UpdateCoinsResponse")
    @Action(input = "http://services/GameServerService/updateCoinsRequest", output = "http://services/GameServerService/updateCoinsResponse")
    public String updateCoins(
        @WebParam(name = "username", targetNamespace = "")
        String username);

}