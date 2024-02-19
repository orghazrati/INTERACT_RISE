package com.example.jcrm.controllers.support_controllers;

import com.example.jcrm.dao.CustomerDao;
import com.example.jcrm.dao.ProductsDao;
import com.example.jcrm.dao.SupportDao;
import com.example.jcrm.models.Customer;
import com.example.jcrm.models.Products;
import com.example.jcrm.models.Support;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;

@Controller
public class OperatorController
{
    @GetMapping("register")
    public String viewRegisterPage(Model model)
    {
        Support support = new Support();
        model.addAttribute("register",support);
        return "redirect:/adminRegister";
    }//end of method.

    @PostMapping("register")
    public String register(@ModelAttribute Support support)
    {
        SupportDao supportDao = new SupportDao();
        supportDao.saveSupport(support);
        return "redirect:/adminIndex";
    }//end of method.

    @GetMapping("operatorDashboard")
    public String showOperatorPage()
    {
        return "operatorDashboard";
    }

    @GetMapping("/operator/showAllCustomers")
    public String modelShowCustomers(Model model)
    {
        CustomerDao customerDao = new CustomerDao();
        model.addAttribute("customer",customerDao.showAllCustomers());
        return "operatorDashboard";
    }//end of method

    @GetMapping("/operator/addCustomer")
    public String modelAddCustomer(Model model)
    {
        Customer customer = new Customer();
        model.addAttribute("addcustomer",customer);
        return "redirect:/operatorDashboard";
    }//END OF METHOD

    @PostMapping("/operator/addCustomer")
    public String addCustomer(@ModelAttribute Customer customer) throws NoSuchAlgorithmException
    {
        CustomerDao customerDao = new CustomerDao();
        customer.setPassword(customerDao.encryptPassword(customer.getPassword()));
        Products products = new Products();
        ProductsDao productsDao = new ProductsDao();
        while (!customerDao.checkUsernameAndSave(customer.getName(), customer))
        {
            customerDao.checkUsernameAndSave(customer.getName(), customer);
        }
        customerDao.flushTheCustomer();
        products.setName(customer.getOrders());
        products.setCustomer(customer);
        productsDao.saveProduct(products);
        return "redirect:/operatorDashboard";
    }//END OF METHOD

    @PostMapping("/operator/showAllOrders")
    public String modelShowAllOrders(Model model)
    {
        ProductsDao productsDao = new ProductsDao();
        model.addAttribute("showallorders",productsDao.showAllProducts());
        return "operatorDashboard";
    }//end of method.

    @PostMapping("/operator/showOrderById")
    public String modelShowOrderById(Model model,Long id)
    {
        ProductsDao productsDao = new ProductsDao();
        model.addAttribute("showorderbyid",productsDao.showProduct(id));
        return "operatorDashboard";
    }//end of method.

    @PostMapping("/operator/showAllProducts")
    public String modelShowAllProducts(Model model)
    {
        ProductsDao productsDao = new ProductsDao();
        model.addAttribute("showallproducts",productsDao.showAllProducts());
        return "operatorDashboard";
    }//end of method.

    @GetMapping("/operator/showCustomerById")
    public String modelShowCustomerId(Model model,Long id)
    {
        CustomerDao customerDao = new CustomerDao();
        model.addAttribute("customerId",customerDao.showCustomer(id));
        return "operatorDashboard";
    }//END OF METHOD
}//end of method.
