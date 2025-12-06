/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sergio.inventarios.services;

import com.sergio.inventarios.dao.UsuarioDAO;
import com.sergio.inventarios.dao.SesionesDAO;
import com.sergio.inventarios.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;
import java.util.UUID;


/**
 *
 * @author SISTEMAS DESARROLLO2
 */
public class AuthService {

    private UsuarioDAO usuarioDAO;
    private SesionesDAO sesionesDAO;

    public AuthService(UsuarioDAO usuarioDAO, SesionesDAO sesionesDAO) {
        this.usuarioDAO = usuarioDAO;
        this.sesionesDAO = sesionesDAO;
    }

    public String login(String correo, String contrasena) throws Exception {

        Usuario user = usuarioDAO.buscarPorCorreo(correo);

        if (user == null) {
            return null;
        }

        String hashBD = user.getContrasena();

        boolean coincide = BCrypt.checkpw(contrasena, hashBD);

        if (!coincide) {
            return null; 
        }

        String token = UUID.randomUUID().toString();

        Timestamp expiracion = new Timestamp(System.currentTimeMillis() + (12 * 60 * 60 * 1000));

        sesionesDAO.crearSesion(user.getIdUsuario(), token, expiracion);

        return token;
    }
}
