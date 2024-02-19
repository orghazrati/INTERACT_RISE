package com.example.jcrm.configs;

import com.example.jcrm.models.Customer;
import com.example.jcrm.models.Products;
import com.example.jcrm.models.Support;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import java.util.Properties;

public class HibernateUtil
{

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory()
    {
        if (sessionFactory == null)
        {
            try
            {
                Properties settings = new Properties();
                Configuration configuration = new Configuration();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/jcrm?useSSL=false");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "");

                settings.put(Environment.SHOW_SQL, "true");

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                settings.put(Environment.HBM2DDL_AUTO, "update");

                configuration.setProperties(settings);
                configuration.addAnnotatedClass(Customer.class);
                configuration.addAnnotatedClass(Support.class);
                configuration.addAnnotatedClass(Products.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }//END OF METHOD

}//END OF CLASS
