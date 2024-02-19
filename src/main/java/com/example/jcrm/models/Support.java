package com.example.jcrm.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@AllArgsConstructor
public class Support
{
    //VARIABLES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30,nullable = false)
    private String name;

    @Column(length = 30,nullable = false)
    private String password;

    @Column(length = 35,nullable = false,unique = true)
    private String email;

    @Column
    private String level;

    //CONSTRUCTORS
    public Support()
    {
        this.level = "operator";
    }
    public Support(String name, String password,String email,String level)
    {
        this.name = name;
        this.email = email;
        this.level = level;
        this.password =password;
    }

    public Support(String name, String password,String email)
    {
        this.name = name;
        this.email = email;
        this.level = "operator";
        this.password =password;
    }

    //GETTERS % SETTERS
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

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}//END OF CLASS
