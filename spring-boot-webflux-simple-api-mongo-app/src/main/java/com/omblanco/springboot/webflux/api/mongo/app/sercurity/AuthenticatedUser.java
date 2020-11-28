package com.omblanco.springboot.webflux.api.mongo.app.sercurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * Clase que representa los detalles de un usuario autenticado
 * @author oscar.martinezblanco
 *
 */
public class AuthenticatedUser implements Authentication {

    private static final long serialVersionUID = 6861381095901879822L;
    
    private String email;
    
    private boolean authenticated = true;
    
    private List<SimpleGrantedAuthority> roles;

    public AuthenticatedUser(String email, List<SimpleGrantedAuthority> roles){
        this.email = email;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public Object getCredentials() {
        return this.email;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.email;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.authenticated = b;
    }

    @Override
    public String getName() {
        return this.email;
    }
}