package portfolio2.packages.DAL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import portfolio2.packages.Objects.Customer;
import java.util.List;

@Repository
public class CustomerRepository {

    @Autowired
    public JdbcTemplate db;

    public String addCustomer(Customer customer) {
        String sql;
        int postoffice;
        //TODO: Kan sjekke opp mot postnummer og adresse senere.
   /*     try {
            sql = "SELECT count(*) FROM Postoffice WHERE Postnumber =?";
            postoffice = db.queryForObject(sql, Integer.class, customer.getPostnumber());
        } catch (Exception e) {
            return "Could not connect to database, failed adding customer.";
        }
        if (postoffice == 0) {
            //If postoffice not in database, add this postoffice
            try {
                sql = "INSERT INTO Postoffice (Postnumber, Postoffice) VALUES (?,?)";
                db.update(sql, customer.getPostnumber(), customer.getPostoffice());
            } catch (Exception e) {
                return "Could not add customer with their postoffice";
            }
        }*/
        try {
            sql = "INSERT INTO Customer (firstname, lastname, addresse, postnumber, tlfnumber, email, password) VALUES (?,?,?,?,?,?,?)";
            db.update(sql, customer.getFirstname(), customer.getLastname(), customer.getAddress(),
                    customer.getPostnumber(), customer.getTlphNumber(), customer.getEmail(), customer.getPassword());
        } catch (Exception e) {
            return "Something went wrong trying to add customer.";
        }
        return "Customer added!";
    }

    public Customer getLoggedInCustomer(String email, String password){
        if(email.isBlank() || email.isEmpty() || password.isEmpty() || password.isBlank()){
            return null;
        }
        try{
            String sql = "SELECT * FROM Customer WHERE email = ? AND password = ?";
            List<Customer> loggedOnCustomers = db.query(sql, new BeanPropertyRowMapper<>(Customer.class), email, password);
            return loggedOnCustomers.get(0);
        }catch(Exception e){
            return null;
        }
    }

    public Customer getCustomerByID(String customerID){
        if(customerID == null){
            return null;
        }
        try{
            String sql = "SELECT * FROM Customer WHERE customerID = ?";
            List<Customer> customers = db.query(sql, new BeanPropertyRowMapper<>(Customer.class));
            return customers.get(0);
        }catch(Exception e){
            return null;
        }
    }

    public List<Customer> getCustomers() {
        try {
            String sql = "SELECT * FROM Customer " +
                    "JOIN Postoffice ON Customer.zipcode = City.zipcode " +
                    "ORDER BY customerID";
            List<Customer> customers = db.query(sql, new BeanPropertyRowMapper<>(Customer.class));
            return customers;
        } catch (Exception e) {
            return null;
        }
    }
}
