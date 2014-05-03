
package partitionimpl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the partitionimpl package. 
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

    private final static QName _TransferResponse_QNAME = new QName("http://PartitionImpl/", "transferResponse");
    private final static QName _GetBranchIterator_QNAME = new QName("http://PartitionImpl/", "GetBranchIterator");
    private final static QName _CreditResponse_QNAME = new QName("http://PartitionImpl/", "creditResponse");
    private final static QName _NegativeAmountException_QNAME = new QName("http://PartitionImpl/", "NegativeAmountException");
    private final static QName _CalculateExposure_QNAME = new QName("http://PartitionImpl/", "calculateExposure");
    private final static QName _DebitResponse_QNAME = new QName("http://PartitionImpl/", "debitResponse");
    private final static QName _Debit_QNAME = new QName("http://PartitionImpl/", "debit");
    private final static QName _CalculateExposureResponse_QNAME = new QName("http://PartitionImpl/", "calculateExposureResponse");
    private final static QName _InexistentAccountException_QNAME = new QName("http://PartitionImpl/", "InexistentAccountException");
    private final static QName _Credit_QNAME = new QName("http://PartitionImpl/", "credit");
    private final static QName _PrintBranchAndAccountsResponse_QNAME = new QName("http://PartitionImpl/", "printBranchAndAccountsResponse");
    private final static QName _Transfer_QNAME = new QName("http://PartitionImpl/", "transfer");
    private final static QName _PrintBranchAndAccounts_QNAME = new QName("http://PartitionImpl/", "printBranchAndAccounts");
    private final static QName _GetBranchIteratorResponse_QNAME = new QName("http://PartitionImpl/", "GetBranchIteratorResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: partitionimpl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InexistentAccountException }
     * 
     */
    public InexistentAccountException createInexistentAccountException() {
        return new InexistentAccountException();
    }

    /**
     * Create an instance of {@link Debit }
     * 
     */
    public Debit createDebit() {
        return new Debit();
    }

    /**
     * Create an instance of {@link Transfer }
     * 
     */
    public Transfer createTransfer() {
        return new Transfer();
    }

    /**
     * Create an instance of {@link CalculateExposure }
     * 
     */
    public CalculateExposure createCalculateExposure() {
        return new CalculateExposure();
    }

    /**
     * Create an instance of {@link PrintBranchAndAccountsResponse }
     * 
     */
    public PrintBranchAndAccountsResponse createPrintBranchAndAccountsResponse() {
        return new PrintBranchAndAccountsResponse();
    }

    /**
     * Create an instance of {@link GetBranchIterator }
     * 
     */
    public GetBranchIterator createGetBranchIterator() {
        return new GetBranchIterator();
    }

    /**
     * Create an instance of {@link DebitResponse }
     * 
     */
    public DebitResponse createDebitResponse() {
        return new DebitResponse();
    }

    /**
     * Create an instance of {@link GetBranchIteratorResponse }
     * 
     */
    public GetBranchIteratorResponse createGetBranchIteratorResponse() {
        return new GetBranchIteratorResponse();
    }

    /**
     * Create an instance of {@link CalculateExposureResponse }
     * 
     */
    public CalculateExposureResponse createCalculateExposureResponse() {
        return new CalculateExposureResponse();
    }

    /**
     * Create an instance of {@link CreditResponse }
     * 
     */
    public CreditResponse createCreditResponse() {
        return new CreditResponse();
    }

    /**
     * Create an instance of {@link PrintBranchAndAccounts }
     * 
     */
    public PrintBranchAndAccounts createPrintBranchAndAccounts() {
        return new PrintBranchAndAccounts();
    }

    /**
     * Create an instance of {@link NegativeAmountException }
     * 
     */
    public NegativeAmountException createNegativeAmountException() {
        return new NegativeAmountException();
    }

    /**
     * Create an instance of {@link TransferResponse }
     * 
     */
    public TransferResponse createTransferResponse() {
        return new TransferResponse();
    }

    /**
     * Create an instance of {@link Credit }
     * 
     */
    public Credit createCredit() {
        return new Credit();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "transferResponse")
    public JAXBElement<TransferResponse> createTransferResponse(TransferResponse value) {
        return new JAXBElement<TransferResponse>(_TransferResponse_QNAME, TransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBranchIterator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "GetBranchIterator")
    public JAXBElement<GetBranchIterator> createGetBranchIterator(GetBranchIterator value) {
        return new JAXBElement<GetBranchIterator>(_GetBranchIterator_QNAME, GetBranchIterator.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreditResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "creditResponse")
    public JAXBElement<CreditResponse> createCreditResponse(CreditResponse value) {
        return new JAXBElement<CreditResponse>(_CreditResponse_QNAME, CreditResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NegativeAmountException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "NegativeAmountException")
    public JAXBElement<NegativeAmountException> createNegativeAmountException(NegativeAmountException value) {
        return new JAXBElement<NegativeAmountException>(_NegativeAmountException_QNAME, NegativeAmountException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateExposure }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "calculateExposure")
    public JAXBElement<CalculateExposure> createCalculateExposure(CalculateExposure value) {
        return new JAXBElement<CalculateExposure>(_CalculateExposure_QNAME, CalculateExposure.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DebitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "debitResponse")
    public JAXBElement<DebitResponse> createDebitResponse(DebitResponse value) {
        return new JAXBElement<DebitResponse>(_DebitResponse_QNAME, DebitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Debit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "debit")
    public JAXBElement<Debit> createDebit(Debit value) {
        return new JAXBElement<Debit>(_Debit_QNAME, Debit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateExposureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "calculateExposureResponse")
    public JAXBElement<CalculateExposureResponse> createCalculateExposureResponse(CalculateExposureResponse value) {
        return new JAXBElement<CalculateExposureResponse>(_CalculateExposureResponse_QNAME, CalculateExposureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InexistentAccountException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "InexistentAccountException")
    public JAXBElement<InexistentAccountException> createInexistentAccountException(InexistentAccountException value) {
        return new JAXBElement<InexistentAccountException>(_InexistentAccountException_QNAME, InexistentAccountException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Credit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "credit")
    public JAXBElement<Credit> createCredit(Credit value) {
        return new JAXBElement<Credit>(_Credit_QNAME, Credit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrintBranchAndAccountsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "printBranchAndAccountsResponse")
    public JAXBElement<PrintBranchAndAccountsResponse> createPrintBranchAndAccountsResponse(PrintBranchAndAccountsResponse value) {
        return new JAXBElement<PrintBranchAndAccountsResponse>(_PrintBranchAndAccountsResponse_QNAME, PrintBranchAndAccountsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Transfer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "transfer")
    public JAXBElement<Transfer> createTransfer(Transfer value) {
        return new JAXBElement<Transfer>(_Transfer_QNAME, Transfer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrintBranchAndAccounts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "printBranchAndAccounts")
    public JAXBElement<PrintBranchAndAccounts> createPrintBranchAndAccounts(PrintBranchAndAccounts value) {
        return new JAXBElement<PrintBranchAndAccounts>(_PrintBranchAndAccounts_QNAME, PrintBranchAndAccounts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBranchIteratorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://PartitionImpl/", name = "GetBranchIteratorResponse")
    public JAXBElement<GetBranchIteratorResponse> createGetBranchIteratorResponse(GetBranchIteratorResponse value) {
        return new JAXBElement<GetBranchIteratorResponse>(_GetBranchIteratorResponse_QNAME, GetBranchIteratorResponse.class, null, value);
    }

}
