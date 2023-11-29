package codingdojo;

public interface ICustomerBusinessLogic {
    Customer processExternalCustomer(ExternalCustomer externalCustomer);
    boolean createOrUpdateCustomer(Customer customer);
}
