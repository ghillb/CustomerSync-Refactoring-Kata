package codingdojo;

import java.util.List;
import java.util.Optional;

public class CustomerBusinessLogic implements ICustomerBusinessLogic {
    private final CustomerMatchMaker customerMatchMaker;
    private final CustomerDataAccess customerDataAccess;

    public CustomerBusinessLogic(CustomerDataAccess customerDataAccess) {
        this.customerDataAccess = customerDataAccess;
        this.customerMatchMaker = new CustomerMatchMaker(this.customerDataAccess);
    }

    public Customer processExternalCustomer(ExternalCustomer externalCustomer) {
        CustomerMatches customerMatches = customerMatchMaker.loadCustomerMatches(externalCustomer);
        handleCustomerMatchesDuplicates(externalCustomer, customerMatches);
        Customer customer = catchNullOrGetCustomer(externalCustomer, customerMatches);
        populateCustomerFields(externalCustomer, customer);
        return customer;
    }

    public boolean createOrUpdateCustomer(Customer customer) {
        return Optional.ofNullable(customer.getInternalId())
                .map(id -> {
                    customerDataAccess.updateCustomerRecord(customer);
                    return false;
                })
                .orElseGet(() -> {
                    customerDataAccess.createCustomerRecord(customer);
                    return true;
                });
    }

    private void populateCustomerFields(ExternalCustomer externalCustomer, Customer customer) {
        customer.setName(externalCustomer.getName());
        if (externalCustomer.isCompany()) {
            customer.setCompanyNumber(externalCustomer.getCompanyNumber());
            customer.setCustomerType(CustomerType.COMPANY);
            customer.setBonusPoints(0);
        } else {
            customer.setCustomerType(CustomerType.PERSON);
            int externalPoints = externalCustomer.getBonusPoints();
            int currentPoints = customer.getBonusPoints();
            if (externalPoints != currentPoints) {
                customer.setBonusPoints(externalPoints);
            }
        }

        updateContactInfo(externalCustomer, customer);
        updateRelations(externalCustomer, customer);
        updatePreferredStore(externalCustomer, customer);
    }

    private void updateContactInfo(ExternalCustomer externalCustomer, Customer customer) {
        customer.setAddress(externalCustomer.getPostalAddress());
    }

    private void updateRelations(ExternalCustomer externalCustomer, Customer customer) {
        List<ShoppingList> consumerShoppingLists = externalCustomer.getShoppingLists();
        for (ShoppingList consumerShoppingList : consumerShoppingLists) {
            this.customerDataAccess.updateShoppingList(customer, consumerShoppingList);
        }
    }

    private void updatePreferredStore(ExternalCustomer externalCustomer, Customer customer) {
        customer.setPreferredStore(externalCustomer.getPreferredStore());
    }

    private void handleCustomerMatchesDuplicates(ExternalCustomer externalCustomer, CustomerMatches customerMatches) {

        if (customerMatches.hasDuplicates()) {
            for (Customer duplicate : customerMatches.getDuplicates()) {
                customerDataAccess.updateDuplicate(externalCustomer, duplicate);
            }
        }
    }

    private Customer catchNullOrGetCustomer(ExternalCustomer externalCustomer, CustomerMatches customerMatches) {
        Customer customer = customerMatches.getCustomer();

        if (customer == null) {
            customer = new Customer();
            customer.setExternalId(externalCustomer.getExternalId());
            customer.setMasterExternalId(externalCustomer.getExternalId());
        }
        return customer;
    }
}
