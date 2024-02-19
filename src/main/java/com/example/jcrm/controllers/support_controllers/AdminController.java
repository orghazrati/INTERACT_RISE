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
public class AdminController
{

    //for view home page
    @GetMapping("/")
    public String viewHomePage()
    {
        return "adminIndex";
    }//END OF METHOD.

    @GetMapping("/login")
    public String viewLoginPage(Model model)
    {
        Support support = new Support();
        model.addAttribute("adminlogin",support);
     return "adminLogin";
    }//END OF METHOD.

    @PostMapping("/login")
    public String loginChecker(@ModelAttribute Support support) throws NoSuchAlgorithmException
    {
        SupportDao supportDao = new SupportDao();
        String level = supportDao.getAdminByEmail(support.getEmail()).getLevel();
        CustomerDao customerDao = new CustomerDao();
        String password = support.getPassword();
        String enc_password = customerDao.encryptPassword(password);
        if (supportDao.authenticateAdmin(support.getEmail(),enc_password))
        {
            if (level.equals("admin"))
            {
                return "redirect:/adminDashboard";
            }
            return "redirect:/operatorDashboard";
        }
        return "adminLogin";
    }//END OF METHOD.

    @GetMapping("/adminDashboard")
    public String viewDashboardPage(Model model)
    {
        Customer customer = new Customer();
        model.addAttribute("admindashboard",customer);
        return "adminDashboard";
    }//END OF METHOD

    @GetMapping("/showAdminByName")
    public String modelAdminByLevel(Model model,String name)
    {
        SupportDao supportDao = new SupportDao();
        model.addAttribute("support",supportDao.getAdminByName(name));
            return "adminDashboard";
    }//END OF METHOD

    @GetMapping("/showAllCustomers")
    public String modelShowCustomers(Model model)
    {
        CustomerDao customerDao = new CustomerDao();
        model.addAttribute("customer",customerDao.showAllCustomers());
        return "adminDashboard";
    }//end of method

    @GetMapping("/showCustomerById")
    public String modelShowCustomerId(Model model,Long id)
    {
        CustomerDao customerDao = new CustomerDao();
        model.addAttribute("customerId",customerDao.showCustomer(id));
        return "adminDashboard";
    }//END OF METHOD

    @GetMapping("addCustomer")
    public String modelAddCustomer(Model model)
    {
        Customer customer = new Customer();
        model.addAttribute("addcustomer",customer);
        return "redirect:/adminDashboard";
    }//END OF METHOD

    @PostMapping("addCustomer")
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
        return "redirect:/adminDashboard";
    }//END OF METHOD

    @GetMapping("deleteCustomerByOrderId")
    public String modelDeleteCustomerByOrderId(Model model)
    {
        Products products = new Products();
        model.addAttribute("deletecustomerbyorderid",products);
        return "redirect:/adminDashboard";
    }//END OF METHOD.

    @PostMapping("deleteCustomerByOrderId")
    public String deleteCustomerByOrderId(@ModelAttribute Products products)
    {
        ProductsDao productsDao = new ProductsDao();
        productsDao.removeProduct(products,products.getOrder_id());
        return "redirect:/adminDashboard";
    }//END OF METHOD.

    @GetMapping("deleteCustomerById")
    public String modelDeleteCustomerById(Model model)
    {
        Customer customer = new Customer();
        model.addAttribute("deletecustomerbyid",customer);
        return "redirect:/adminDashboard";
    }//end of method.

    @PostMapping("deleteCustomerById")
    public String deleteCustomerById(@ModelAttribute Customer customer)
    {
        CustomerDao customerDao = new CustomerDao();
        customerDao.removeCustomer(customer, customer.getId());
        return "redirect:/adminDashboard";
    }//end of method

    @PostMapping("showAllOrders")
    public String modelShowAllOrders(Model model)
    {
            ProductsDao productsDao = new ProductsDao();
            model.addAttribute("showallorders",productsDao.showAllProducts());
            return "adminDashboard";
    }//end of method.

    @PostMapping("showOrderById")
    public String modelShowOrderById(Model model,Long id)
    {
        ProductsDao productsDao = new ProductsDao();
        model.addAttribute("showorderbyid",productsDao.showProduct(id));
        return "adminDashboard";
    }//end of method.

    @PostMapping("showAdminById")
    public String modelShowAdminById(Model model,Long id)
    {
        SupportDao supportDao = new SupportDao();
        model.addAttribute("showadminbyid",supportDao.getAdminById(id));
        return "adminDashboard";
    }//end of method.

    @PostMapping("showAllProducts")
    public String modelShowAllProducts(Model model)
    {
        ProductsDao productsDao = new ProductsDao();
        model.addAttribute("showallproducts",productsDao.showAllProducts());
        return "adminDashboard";
    }//end of method.

    @GetMapping("editCustomer")
    public String modelEditCustomer(Model model,Long id)
    {
        Customer customer = new Customer();
        model.addAttribute("editcustomer",customer);
        return "redirect:/adminDashboard";
    }//end of method.

    @PostMapping("editCustomer")
    public String editCustomer(@ModelAttribute Customer customer) throws NoSuchAlgorithmException
    {
        CustomerDao customerDao = new CustomerDao();
        customerDao.updateCustomer(customer.getId(), customer);
        return "redirect:/adminDashboard";
    }//end of method.

    @PostMapping("showAllAdmins")
    public String modelShowAllAdmins(Model model)
    {
        SupportDao supportDao = new SupportDao();
        model.addAttribute("showalladmins",supportDao.showAllSupporters());
        return "adminDashboard";
    }//end of method.

    @GetMapping("deleteProductById")
    public String modelDeleteProduct(Model model,Long id)
    {
        Products products = new Products();
        model.addAttribute("deleteproductbyid",products);
        return "redirect:/adminDashboard";
    }//end of method.

    @PostMapping("deleteProductById")
    public String deleteProduct(@ModelAttribute Products products)
    {
            ProductsDao productsDao = new ProductsDao();
            productsDao.removeProduct(products,products.getOrder_id());
            return "redirect:/adminDashboard";
    }//end of method.

    @GetMapping("editProductById")
    public String modelEditProduct(Model model)
    {
        Products products = new Products();
        model.addAttribute("editproduct",products);
        return "redirect:/adminDashboard";
    }//end of method

    @PostMapping("editProductById")
    public String editProduct(@ModelAttribute Products products)
    {
        ProductsDao productsDao = new ProductsDao();
        productsDao.editProduct(products,products.getOrder_id());
        return "redirect:/adminDashboard";
    }//end of method

    @GetMapping("addAdmin")
    public String modelAddAdmin(Model model)
    {
        Support support = new Support();
        model.addAttribute("addadmin",support);
        return "redirect:/adminDashboard";
    }//end of method.


    @PostMapping("addAdmin")
    public String addAdmin(@ModelAttribute Support support)
    {
        SupportDao supportDao = new SupportDao();
        supportDao.saveSupport(support);
        return "redirect:/adminDashboard";
    }//end of method.


    @GetMapping("deleteAdminById")
    public String modelDeleteAdmin(Model model)
    {
        Support support = new Support();
        model.addAttribute("deleteadmin",support);
        return "redirect:/adminDashboard";
    }//end of method


    @PostMapping("deleteAdminById")
    public String deleteAdmin(@ModelAttribute Support support)
    {
        SupportDao supportDao = new SupportDao();
        supportDao.removeSupporter(support, support.getId());
        return "redirect:/adminDashboard";
    }//end of method.


    @GetMapping("editAdminById")
    public String modelEditById(Model model)
    {
        Support support = new Support();
        model.addAttribute("editadmin",support);
        return "redirect:/adminDashboard";
    }//end of method


    @PostMapping("editAdminById")
    public String editAdminById(@ModelAttribute Support support) throws NoSuchAlgorithmException
    {
        SupportDao supportDao = new SupportDao();
        supportDao.editAdmin(support, support.getId());
        return "redirect:/adminDashboard";
    }//end of method


}//END OF CONTROLLER
