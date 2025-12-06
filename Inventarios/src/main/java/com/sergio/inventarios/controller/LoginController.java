/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sergio.inventarios.controller;

import com.sergio.inventarios.config.Conexion;
import com.sergio.inventarios.dao.SesionesDAO;
import com.sergio.inventarios.services.AuthService;
import com.sergio.inventarios.dao.UsuarioDAO;
import com.sergio.inventarios.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.*;
import java.sql.Connection;

/**
 *
 * @author SISTEMAS DESARROLLO2
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginController {

    @POST
    @Path("/login")
    public Response login(@Context HttpServletRequest request, Credentials cred) {
        try {
            Connection conn = Conexion.getConexion();

            UsuarioDAO usuarioDAO = new UsuarioDAO(conn);
            SesionesDAO sesionesDAO = new SesionesDAO(conn);

            AuthService authService = new AuthService(usuarioDAO, sesionesDAO);

            String token = authService.login(cred.correo, cred.contrasena);
            if (token == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"mensaje\":\"Credenciales inválidas\"}")
                        .build();
            }

            Usuario usuario = usuarioDAO.buscarPorCorreo(cred.correo);

            HttpSession session = request.getSession(true);
            session.setAttribute("token", token);
            session.setAttribute("idUsuario", usuario.getIdUsuario());
            session.setAttribute("idRol", usuario.getIdRol());

            return Response.ok("{\"token\":\"" + token + "\", \"idRol\":" + usuario.getIdRol() + "}").build();

        } catch (Exception e) {
            return Response.status(500)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    public static class Credentials {

        public String correo;
        public String contrasena;
    }

    @POST
    @Path("/logout")
    public Response logout(@Context HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);

            if (session == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"mensaje\":\"No hay sesión activa\"}")
                        .build();
            }

            String token = (String) session.getAttribute("token");

            Connection conn = Conexion.getConexion();
            SesionesDAO sesionesDAO = new SesionesDAO(conn);

            if (token != null) {
                sesionesDAO.eliminarSesion(token);
            }

            session.invalidate();

            return Response.ok("{\"mensaje\":\"Logout exitoso\"}").build();

        } catch (Exception e) {
            return Response.status(500)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/validate")
    public Response validate(@Context ContainerRequestContext req) {
        Integer idUsuario = (Integer) req.getProperty("idUsuario");

        if (idUsuario == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"valid\":false}")
                    .build();
        }

        return Response.ok("{\"valid\":true,\"idUsuario\":" + idUsuario + "}").build();
    }

}
