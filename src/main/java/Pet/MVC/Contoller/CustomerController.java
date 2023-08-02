package Pet.MVC.Contoller;

import Pet.MVC.Model.Customer;
import Pet.MVC.Model.MedicalRecord;

import java.util.ArrayList;
import java.util.List;

public class CustomerController {
    private List<Customer> customers;
    private MedicalRecordController recordController;

    public CustomerController(MedicalRecordController recordController) {
        this.customers = new ArrayList<>();
        this.recordController = recordController;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void removeCustomer(String phoneNumber) {
        for(int i =0 ; i<customers.size(); i++) {
            if(customers.get(i).getPhoneNumber().equals(phoneNumber)) {
                customers.remove(i);
                recordController.removeMedicalRecord(phoneNumber);
                break;
            }
        }
    }

    public Customer findCustomer(String phoneNumber) {
        for(Customer customer : customers) {
            if(customer.getPhoneNumber().equals(phoneNumber)) {
                return customer;
            }
        }
        return null;
    }

    public List<Customer> allCustomer() {
        return customers;
    }
    public boolean isPhoneNumberExist(String phoneNumber) {
        for(Customer customer:customers) {
            if(customer.getPhoneNumber().equals(phoneNumber)) {
                return true;
            }
        }
        return false;
    }
}
