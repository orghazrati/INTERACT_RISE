package com.example.jcrm.dao;

import com.example.jcrm.configs.HibernateUtil;
import com.example.jcrm.models.Customer;
import com.example.jcrm.models.Support;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

public class SupportDao
{
    @Transactional
    public void saveSupport(Support support) //METHOD FOR CREATE A NEW SUPPORTER
    {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            CustomerDao customerDao = new CustomerDao();
            String password = customerDao.encryptPassword(support.getPassword());
            support.setPassword(password);
            session.merge(support);
            transaction.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }//END OF METHOD


    @Transactional
    public void editAdmin(Support support,Long id) throws NoSuchAlgorithmException
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            CustomerDao customerDao = new CustomerDao();
            transaction = session.beginTransaction();
            Support support_obj = session.get(Support.class,id);
            support_obj.setName(support.getName());
            String password = customerDao.encryptPassword(support.getPassword());
            support_obj.setPassword(password);
            support_obj.setEmail(support.getEmail());
            support_obj.setLevel(support.getLevel());
            session.merge(support_obj);
            transaction.commit();
        }
    }//end of method

    @Transactional
    public void removeSupporter(Support support,Long id) //METHOD FOR REMOVE SUPPORTER
    {
        Transaction transaction;
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            session.load(support,id);
            session.remove(support);
            transaction.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }//END OF METHOD

    public List<Support> showAllSupporters()
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            List<Support> entityList = session.createQuery("from Support ",Support.class).list();
            for (Support e : entityList)
            {
                return entityList;
            }
            return entityList;
        }
    }//END OF METHOD

    @Transactional
    public void saveAllSupporters(Support[] supports)
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            for (Support support : supports)
            {
                session.persist(support);
            }
            transaction.commit();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }//END OF METHOD.

    public void removeAllSupporters()
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            String string_query = "delete from Support";
            Query query = session.createQuery(string_query);
            query.executeUpdate();
            transaction.commit();
        }
    }//END OF METHOD

    public void adminExcelReporter()
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            transaction = session.beginTransaction();
            List<Support> data = session.createQuery("from Support ", Support.class).list();
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");
            int rowNum = 0;
            Date date = new Date();
            String time = date.toString();
            for (Support support : data)
            {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(support.getId());
                row.createCell(1).setCellValue(support.getName());
                row.createCell(2).setCellValue(support.getEmail());
                row.createCell(3).setCellValue(support.getLevel());
                row.createCell(4).setCellValue(support.getPassword());
                row.createCell(7).setCellValue(time);
            }
            transaction.commit();
            try(FileOutputStream outputStream = new FileOutputStream("support_data.xlsx"))
            {
                workbook.write(outputStream);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }//END OF METHOD

    public Support getAdminByName(String username)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            return session.createQuery("from Support where name = :username", Support.class).setParameter("username",username).uniqueResult();
        }
    }//END OF METHOD.

    public Support getAdminByPassword(String password)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            return session.createQuery("from Support where password = :password", Support.class).setParameter("password",password).uniqueResult();
        }
    }//END OF METHOD.

    public Support getAdminLevelByName(String name)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            return session.createQuery("from Support where name = :name",Support.class).setParameter("name",name).uniqueResult();
        }
    }//END OF METHOD.

    public boolean authenticateAdmin(String email,String password) //method for login
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {

                if (getAdminByEmail(email).getEmail().equals(email)&&getAdminByEmail(email).getPassword().equals(password))
                {
                    return true;
                }
            }catch (Exception e)
            {
            return false;
            }
            return false;
    }//END OF METHOD.

    public Support getAdminByEmail(String email)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            return session.createQuery("from Support  where email = :email",Support.class).setParameter("email",email).uniqueResult();
        }
    }//end of method.

    public Support getAdminByEmailAndPassword(String email,String password)
    {
       try(Session session = HibernateUtil.getSessionFactory().openSession())
       {
          return session.createQuery("from Support where email = :email and password = :password", Support.class).setParameter("email",email).setParameter("password",password).uniqueResult();
       }
    }//end of method.


    public Support getAdminById(Long id)
    {
            try(Session session = HibernateUtil.getSessionFactory().openSession())
            {
               return session.createQuery("from Support where id = :id", Support.class).setParameter("id",id).uniqueResult();
            }
    }//end of method.

}//end of class
