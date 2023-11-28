package codingdojo;

public class CustomerSync {

    private final CustomerDataAccess customerDataAccess;
    private final CustomerBusinessLogic customerBusinessLogic;

    public CustomerSync(CustomerDataLayer customerDataLayer) {
        this(new CustomerDataAccess(customerDataLayer));
    }

    public CustomerSync(CustomerDataAccess db) {
        this.customerDataAccess = db;
        this.customerBusinessLogic = new CustomerBusinessLogic(this.customerDataAccess);

    }

    public boolean syncWithDataLayer(ExternalCustomer externalCustomer) {
        Customer internalCustomer = customerBusinessLogic.processExternalCustomer(externalCustomer);
        boolean createdNewInternalCustomer = customerBusinessLogic.createOrUpdateCustomer(internalCustomer);
        return createdNewInternalCustomer;
    }

}
