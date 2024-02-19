package com.example.jcrm.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
@Entity
@AllArgsConstructor
public class Products
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;

    @Column(length = 30,nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;

    public Products(String name, Customer customer)
    {
        this.name = name;
    }


    public Products()
    {

    }

    public Long getOrder_id()
    {
        return order_id;
    }

    public void setOrder_id(Long order_id)
    {
        this.order_id = order_id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

}//END OF METHOD
