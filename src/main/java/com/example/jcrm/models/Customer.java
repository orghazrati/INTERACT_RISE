package com.example.jcrm.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@AllArgsConstructor
public class Customer
{
    //VARIABLES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50,nullable = false,unique = true)
    private String name;

    @Column(length = 33,nullable = false,unique = true)
    private String email;

    @Column(length = 15,nullable = false,unique = true)
    private String number;

    @Column(length = 22)
    private String orders;

    @Column(length = 45)
    private String order_description;

    @Column(length = 30,nullable = false)
    private String password;

    @OneToMany(mappedBy = "customer")
    private List<Products> products;

    @Column(length = 11)
    private String status;

    //CONSTRUCTORS
    public Customer() //NO ARG CONSTRUCTOR
    {
        setStatus("queued");
    }
    public Customer(String name, String password,String email, String number, String order)
    {
        setStatus("queued");
        this.name = name;
        this.email = email;
        this.number = number;
        this.orders = order;
        this.password =password;
    }

    public Customer(String name, String password,String email, String number, String order,String order_description) //ALL ARG CONSTRUCTOR
    {
        setStatus("queued");
        this.name = name;
        this.password =password;
        this.email = email;
        this.number = number;
        this.orders = order;
        this.order_description = order_description;
    }

    //GETTERS & SETTERS

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getNumber()
    {

        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getOrders()
    {
        return orders;
    }

    public void setOrders(String orders)
    {
        this.orders = orders;
    }

    public String getOrder_description()
    {
        return order_description;
    }

    public void setOrder_description(String order_description)
    {
        this.order_description = order_description;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public List<Products> getProducts()
    {
        return products;
    }

    public void setProducts(List<Products> products)
    {
        this.products = products;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

}//END OF CLASS
