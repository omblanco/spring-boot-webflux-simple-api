package com.omblanco.springboot.webflux.api.client.dto;

import java.util.Date;

/**
 * Tipo de dato del id
 * @author ombla
 *
 * @param <K> Id del usuario
 */
public class UserDTO<K> {

    private K id;
    
    private String name;
    
    private String surname;
    
    private String email;
    
    private Date birthdate;
    
    private String password;

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", name=" + name + ", surname=" + surname + ", email=" + email + ", birthdate="
                + birthdate + ", password=" + password + "]";
    }
}
