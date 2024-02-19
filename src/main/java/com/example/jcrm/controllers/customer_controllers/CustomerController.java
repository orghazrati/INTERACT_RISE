package com.example.jcrm.controllers.customer_controllers;

import com.example.jcrm.dao.CustomerDao;
import com.example.jcrm.dao.ProductsDao;
import com.example.jcrm.models.Customer;
import com.example.jcrm.models.Products;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;

@Controller
public class CustomerController
{
    private Long customerIdValue;

    public Long getCustomerIdValue()
    {
        return customerIdValue;
    }

    public void setCustomerIdValue(Long customerIdValue)
    {
        this.customerIdValue = customerIdValue;
    }

    @GetMapping("/customer")
    public String viewCustomerIndex()
    {
        return "customerIndex";
    }//end of method.

    @GetMapping("customerDashboard")
    public String showCustomerDashboardPage()
    {
        return "customerDashboard";
    }//end of method.

    @GetMapping("/customerlogin")
    public String modelCustomerLogin(Model model)
    {
        Customer customer = new Customer();
        model.addAttribute("customerlogin",customer);
        return "customerLogin";
    }//end of method.

    @PostMapping("/customerlogin")
    public String customerLogin(@ModelAttribute Customer customer) throws NoSuchAlgorithmException
    {
        CustomerDao customerDao = new CustomerDao();
        if (customerDao.authenticateCustomer(customerDao.getCustomerByEmail(customer.getEmail()).getEmail(),customer.getPassword()))
        {
            setCustomerIdValue(customerDao.getCustomerByEmail(customer.getEmail()).getId());
            return "redirect:/customerDashboard";
        }
        return "customerLogin";
    }//end of method.

    @GetMapping("/customerDashboard/changeOrder")
    public String modelAddOrderCustomer(Model model)
    {
        Customer customer = new Customer();
        model.addAttribute("changeorderbycustomer",customer);
        return "customerDashboard";
    }//end of method.

    @PostMapping("/customerDashboard/changeOrder")
    public String addOrderCustomer(@ModelAttribute Customer customer)
    {
        CustomerDao customerDao = new CustomerDao();
        customerDao.changeOrder(getCustomerIdValue(),customer.getOrders(),customer.getOrder_description());
        return "customerDashboard";
    }//end of method.

    @PostMapping("/customerDashboard/showOrder")
    public String modelShowOrder(Model model)
    {
        CustomerDao customerDao = new CustomerDao();
        model.addAttribute("customershoworder",customerDao.showCustomer(getCustomerIdValue()));
        return "customerDashboard";
    }//end of method

}//end of method.
