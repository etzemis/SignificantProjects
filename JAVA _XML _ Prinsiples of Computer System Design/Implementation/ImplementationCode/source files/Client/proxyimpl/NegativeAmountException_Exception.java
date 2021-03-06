
package proxyimpl;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "NegativeAmountException", targetNamespace = "http://ProxyImpl/")
public class NegativeAmountException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private NegativeAmountException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public NegativeAmountException_Exception(String message, NegativeAmountException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public NegativeAmountException_Exception(String message, NegativeAmountException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: proxyimpl.NegativeAmountException
     */
    public NegativeAmountException getFaultInfo() {
        return faultInfo;
    }

}
