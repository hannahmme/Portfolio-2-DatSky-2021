package portfolio2.packages.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomerRegister {

    @Autowired
    private static ArrayList<Customer> customerRegister = new ArrayList<>();

    @Autowired
    public static ArrayList<Customer> getCustomerRegister() {
        return customerRegister;
    }

    @Autowired
    public static void addCustomer(Customer customer){
        customerRegister.add(customer);
    }

    @Autowired
    public static Customer getCustomerByID(String customerID){
        Customer ret = null;
        for(Customer c : customerRegister){
            if(c.getCustomerID().equals(customerID)){
                ret = c;
            }
        }
        return ret;
    }

    @Autowired
    public static String generateCustomerID(){
        String customerID = "customer";
        return customerID;
    }
}