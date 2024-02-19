/*
AUTHOR: AMIRREZA HAZRATI
 */
package com.example.jcrm;
import com.example.jcrm.controllers.customer_controllers.CustomerController;
import com.example.jcrm.dao.CustomerDao;
import com.example.jcrm.dao.ProductsDao;
import com.example.jcrm.dao.SupportDao;
import com.example.jcrm.models.Products;
import com.example.jcrm.models.Support;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

@SpringBootApplication
public class JcrmApplication
{
    public static void main(String[] args) throws NoSuchAlgorithmException
    {
        SpringApplication.run(JcrmApplication.class, args);
        CustomerDao customerDao = new CustomerDao();
        Products products = new Products();
        Scanner scanner = new Scanner(System.in);
        ProductsDao productsDao = new ProductsDao();
        customerDao.showAllCustomers();
        SupportDao supportDao = new SupportDao();
        String password = "123";
        String enc = customerDao.encryptPassword(password);
        System.out.println(supportDao.getAdminByEmailAndPassword("sarah@gmail.com",enc).getId());
        CustomerController customerController = new CustomerController();
    }//END OF MAIN

}//END OF CLASS