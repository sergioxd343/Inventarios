/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sergio.inventarios.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.sergio.inventarios.model.Usuario;

/**
 *
 * @author SISTEMAS DESARROLLO2
 */
public class UsuarioRepository {
   @PersistenceContext
    private EntityManager em;

    public Usuario buscar(Long id) {
        return em.find(Usuario.class, id);
    } 
}
