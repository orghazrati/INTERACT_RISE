package com.example.jcrm.dao;

import com.example.jcrm.configs.HibernateUtil;
import com.example.jcrm.models.Customer;
import com.example.jcrm.models.Products;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.crypto.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class CustomerDao
{

    @Transactional
    public void removeCustomer(Customer customer,Long id) //METHOD FOR REMOVE CUSTOMER
    {
        Transaction transaction;
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            session.load(customer,id);
            session.remove(customer);
            transaction.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }//END OF METHOD

    public List<Customer> showAllCustomers()
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            List<Customer> entityList = (List<Customer>) session.createQuery("from Customer ",Customer.class).list();
            for (Customer e : entityList)
            {
                System.out.println("name: "+e.getName());
                System.out.println("email: "+e.getEmail());
                System.out.println("password: "+e.getPassword());
                System.out.println("number: "+e.getNumber());
                System.out.println("id: "+e.getId()+"\n==========================================");
            }
            return entityList;
        }
    }//END OF METHOD

    public Customer showCustomer(Long id)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
          Customer customer = session.get(Customer.class,id);
          return customer;
        }
    }//END OF METHOD.

    @Transactional
    public void removeAllCustomers()
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            String stringQuery = "DELETE FROM Customer ";
            Query query = session.createQuery(stringQuery);
            query.executeUpdate();
            transaction.commit();
        }
    }//END OF METHOD

    @Transactional
    public void saveAllCustomers(Customer[] customers)
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            for (int counter = 0;counter<customers.length;counter++)
            {
                session.persist(customers[counter]);
            }
            transaction.commit();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }//END OF METHOD

    public void showAllOrders()
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            List<Customer> customers = (List<Customer>) session.createQuery("from Customer ",Customer.class).list();
            for (Customer customer : customers)
            {
                System.out.println("order: "+customer.getOrders()+"\nfrom user: "+customer.getId()+"\norder description: "+customer.getOrder_description()+"\n=========================");
            }
            transaction.commit();
        }
    }//END OF METHOD

    public void showCustomerOrder(Long id)
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            Customer customer = session.get(Customer.class,id);
            System.out.println("customer name: "+customer.getName()+"\ncustomer id: "+customer.getId()+"\norder: "+customer.getOrders()+"\ndescription: "+customer.getOrder_description());
            transaction.commit();
        }
    }//END OF METHOD.

    public void updateCustomer(Long id,Customer customer) throws NoSuchAlgorithmException
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            Customer customer_obj = session.get(Customer.class,id);
            customer_obj.setName(customer.getName());
            String password = customer.getPassword();
            customer_obj.setPassword(encryptPassword(password));
            customer_obj.setEmail(customer.getEmail());
            customer_obj.setNumber(customer.getNumber());
            customer_obj.setOrders(customer.getOrders());
            session.merge(customer_obj);
            Products products = new Products();
            ProductsDao productsDao = new ProductsDao();
            products.setName(customer.getOrders());
            products.setCustomer(customer);
            productsDao.saveProduct(products);
            transaction.commit();
        }
    }//END OF METHOD

    public void changeOrder(Long id, String order, @Nullable String order_description)
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
           Customer customer = session.get(Customer.class,id);
           customer.setOrders(order);
           customer.setOrder_description(order_description);
           session.merge(customer);
           transaction.commit();
        }
    }//end of method.

    public void flushTheCustomer()
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            session.flush();
            transaction.commit();
        }
    }//END OF METHOD

    public void statusChanger(String new_status,Long id)
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            Customer customer = session.get(Customer.class,id);
            customer.setStatus(new_status);
            session.flush();
            transaction.commit();
        }
    }//END OF METHOD

    public void customerExcelReporter()
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            List<Customer> data = session.createQuery("from Customer ", Customer.class).list();
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");
            int rowNum = 0;
            Date date = new Date();
            String time = date.toString();
            for (Customer customer : data)
            {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(customer.getId());
                row.createCell(1).setCellValue(customer.getName());
                row.createCell(2).setCellValue(customer.getOrders());
                row.createCell(3).setCellValue(customer.getNumber());
                row.createCell(4).setCellValue(customer.getEmail());
                row.createCell(5).setCellValue(customer.getOrder_description());
                row.createCell(6).setCellValue(customer.getStatus());
                row.createCell(7).setCellValue(time);
            }
            transaction.commit();
            try(FileOutputStream outputStream = new FileOutputStream("customer_data.xlsx"))
            {
                workbook.write(outputStream);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }//END OF METHOD

    public Customer getUserByName(String username)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
           return session.createQuery("from Customer where name = :username", Customer.class).setParameter("username",username).uniqueResult();
        }
    }//END OF METHOD.

    public Customer getUserByPassword(String password)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            return session.createQuery("from Customer where password = :password", Customer.class).setParameter("password",password).uniqueResult();
        }
    }//END OF METHOD


    @Transactional
    public boolean checkUsernameAndSave(String name,Customer customer_obj)
    {
        Transaction transaction;
        Session session = HibernateUtil.getSessionFactory().openSession();

            transaction = session.beginTransaction();
            Customer customer = session.createQuery("from Customer where name = : name",Customer.class).setParameter("name",name).uniqueResult();
            if (customer != null)
            {
                System.out.println("this name is already used,enter different name.");
                return true;
            }else
            {
                session.persist(customer_obj);
                transaction.commit();
                return false;
            }

    }//END OF METHOD

    public boolean authenticateCustomer(String email,String password) throws NoSuchAlgorithmException //method for login
    {
        try
        {
                if (getCustomerByEmail(email).getEmail().equals(email))
                {
                    String encryptedData = encryptPassword(password);
                    String data = getCustomerByEmail(email).getPassword();
                    if (encryptedData.equals(data))
                    {
                        return true;
                    }
                }
            } catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }//END OF METHOD.

    public Customer getCustomerByEmail(String email)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
           return session.createQuery("from Customer where email = :email", Customer.class).setParameter("email",email).uniqueResult();
        }
    }//end of method.

    public String encryptPassword(String data) throws NoSuchAlgorithmException
    {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(data.getBytes());

        byte[] hashed_data = messageDigest.digest();

        StringBuilder builder = new StringBuilder();
        for (int i = 0;i < Math.min(14, hashed_data.length);i++)
        {
            builder.append(String.format("%02x",hashed_data[i]));
        }
        return builder.toString();
    }//END OF METHOD

}//END OF CLASS
