
package services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the services package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _InitResponse_QNAME = new QName("http://services/", "initResponse");
    private final static QName _CloseDBResponse_QNAME = new QName("http://services/", "closeDBResponse");
    private final static QName _UpdateCoinsResponse_QNAME = new QName("http://services/", "updateCoinsResponse");
    private final static QName _UpdateCoins_QNAME = new QName("http://services/", "updateCoins");
    private final static QName _GetCredits_QNAME = new QName("http://services/", "GetCredits");
    private final static QName _Init_QNAME = new QName("http://services/", "init");
    private final static QName _CloseDB_QNAME = new QName("http://services/", "closeDB");
    private final static QName _GetCreditsResponse_QNAME = new QName("http://services/", "GetCreditsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CloseDBResponse }
     * 
     */
    public CloseDBResponse createCloseDBResponse() {
        return new CloseDBResponse();
    }

    /**
     * Create an instance of {@link UpdateCoins }
     * 
     */
    public UpdateCoins createUpdateCoins() {
        return new UpdateCoins();
    }

    /**
     * Create an instance of {@link Init }
     * 
     */
    public Init createInit() {
        return new Init();
    }

    /**
     * Create an instance of {@link CloseDB }
     * 
     */
    public CloseDB createCloseDB() {
        return new CloseDB();
    }

    /**
     * Create an instance of {@link UpdateCoinsResponse }
     * 
     */
    public UpdateCoinsResponse createUpdateCoinsResponse() {
        return new UpdateCoinsResponse();
    }

    /**
     * Create an instance of {@link GetCreditsResponse }
     * 
     */
    public GetCreditsResponse createGetCreditsResponse() {
        return new GetCreditsResponse();
    }

    /**
     * Create an instance of {@link GetCredits }
     * 
     */
    public GetCredits createGetCredits() {
        return new GetCredits();
    }

    /**
     * Create an instance of {@link InitResponse }
     * 
     */
    public InitResponse createInitResponse() {
        return new InitResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "initResponse")
    public JAXBElement<InitResponse> createInitResponse(InitResponse value) {
        return new JAXBElement<InitResponse>(_InitResponse_QNAME, InitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CloseDBResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "closeDBResponse")
    public JAXBElement<CloseDBResponse> createCloseDBResponse(CloseDBResponse value) {
        return new JAXBElement<CloseDBResponse>(_CloseDBResponse_QNAME, CloseDBResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateCoinsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "updateCoinsResponse")
    public JAXBElement<UpdateCoinsResponse> createUpdateCoinsResponse(UpdateCoinsResponse value) {
        return new JAXBElement<UpdateCoinsResponse>(_UpdateCoinsResponse_QNAME, UpdateCoinsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateCoins }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "updateCoins")
    public JAXBElement<UpdateCoins> createUpdateCoins(UpdateCoins value) {
        return new JAXBElement<UpdateCoins>(_UpdateCoins_QNAME, UpdateCoins.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCredits }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "GetCredits")
    public JAXBElement<GetCredits> createGetCredits(GetCredits value) {
        return new JAXBElement<GetCredits>(_GetCredits_QNAME, GetCredits.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Init }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "init")
    public JAXBElement<Init> createInit(Init value) {
        return new JAXBElement<Init>(_Init_QNAME, Init.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CloseDB }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "closeDB")
    public JAXBElement<CloseDB> createCloseDB(CloseDB value) {
        return new JAXBElement<CloseDB>(_CloseDB_QNAME, CloseDB.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCreditsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services/", name = "GetCreditsResponse")
    public JAXBElement<GetCreditsResponse> createGetCreditsResponse(GetCreditsResponse value) {
        return new JAXBElement<GetCreditsResponse>(_GetCreditsResponse_QNAME, GetCreditsResponse.class, null, value);
    }

}
