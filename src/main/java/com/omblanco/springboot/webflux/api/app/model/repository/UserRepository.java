package com.omblanco.springboot.webflux.api.app.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omblanco.springboot.webflux.api.app.model.entity.User;

/**
 * Repositorio de Usuarios
 * @author oscar.martinezblanco
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
