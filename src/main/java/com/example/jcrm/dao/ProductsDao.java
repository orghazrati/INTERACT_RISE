package com.example.jcrm.dao;

import com.example.jcrm.configs.HibernateUtil;
import com.example.jcrm.models.Products;
import jakarta.transaction.Transactional;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ProductsDao
{

    @Transactional
    public void saveProduct(Products product) //METHOD FOR CREATE A NEW PRODUCT
    {
        Transaction transaction;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession())
        {
            transaction = session.beginTransaction();
            session.merge(product);
            transaction.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }//END OF METHOD


    @Transactional
    public void removeProduct(Products products, Long id) //METHOD FOR REMOVE PRODUCT
    {
        Transaction transaction;
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            products = session.get(Products.class,id);
            session.remove(products);
            transaction.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }//END OF METHOD

    public List<Products> showAllProducts()
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            List<Products> productsList = (List<Products>) session.createQuery("from Products ",Products.class).list();
            for (Products e : productsList)
            {
                System.out.println("name: "+e.getName());
            }
            return productsList;
        }
    }//END OF METHOD

    public Products showProduct(Long id)
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            Products products = session.get(Products.class,id);
            transaction.commit();
            return products;
        }
    }//END OF METHOD.

    @Transactional
    public void removeAllProducts()
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            String stringQuery = "DELETE FROM Products ";
            Query query = session.createQuery(stringQuery);
            query.executeUpdate();
            transaction.commit();
        }
    }//END OF METHOD

    @Transactional
    public void saveAllProducts(Products[] products)
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            for (int counter = 0;counter<products.length;counter++)
            {
                session.persist(products[counter]);
            }
            transaction.commit();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }//END OF METHOD

    public void productExcelReporter()
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            List<Products> data = session.createQuery("from Products ", Products.class).list();
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");
            int rowNum = 0;
            Date date = new Date();
            String time = date.toString();
            for (Products products : data)
            {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(products.getOrder_id());
                row.createCell(1).setCellValue(products.getName());
                row.createCell(7).setCellValue(time);
            }
            transaction.commit();
            try(FileOutputStream outputStream = new FileOutputStream("product_data.xlsx"))
            {
                workbook.write(outputStream);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }//END OF METHOD

    public void editProduct(Products products,Long id)
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            Products products_obj = session.get(Products.class,id);
            products_obj.setName(products.getName());
            session.merge(products_obj);
            transaction.commit();
        }
    }//end of method

}//END OF METHOD
