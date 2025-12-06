/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sergio.inventarios.security;

import com.sergio.inventarios.config.Conexion;
import com.sergio.inventarios.dao.SesionesDAO;
import com.sergio.inventarios.dao.UsuarioDAO;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author SISTEMAS DESARROLLO2
 */
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)

public class AuthFilter implements ContainerRequestFilter {

    private static final List<String> rutasProtegidas = Arrays.asList(
            "productos/alta",
            "productos/baja",
            "productos/inventario"
    );

    @Override
    public void filter(ContainerRequestContext request) {

        String path = request.getUriInfo().getPath();

        if (path.startsWith("auth/login")) {
            return;
        }

        String header = request.getHeaderString("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            request.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{\"error\":\"Token requerido\"}")
                            .build()
            );
            return;
        }

        String token = header.substring("Bearer ".length()).trim();

        try {
            Connection conn = Conexion.getConexion();
            SesionesDAO sesionesDAO = new SesionesDAO(conn);
            UsuarioDAO ususarioDAO = new UsuarioDAO(conn);

            int idUsuario = sesionesDAO.validarToken(token);

            if (idUsuario == -1) {
                request.abortWith(
                        Response.status(Response.Status.UNAUTHORIZED)
                                .entity("{\"error\":\"Token inválido o expirado\"}")
                                .build()
                );
            }

            int idRol = ususarioDAO.obtenerRolUsuario(idUsuario);
            String metodo = request.getMethod();
            boolean protegido = metodo.equalsIgnoreCase("POST")
                    || metodo.equalsIgnoreCase("DELETE");

            if (protegido && idRol != 1) {
                request.abortWith(
                        Response.status(Response.Status.FORBIDDEN)
                                .entity("{\"error\":\"No tienes permisos para realizar esta acción\"}")
                                .build()
                );
                return;
            }

            for (String ruta : rutasProtegidas) {
                if (path.startsWith(ruta) && idRol != 1) {
                    request.abortWith(
                            Response.status(Response.Status.FORBIDDEN)
                                    .entity("{\"error\":\"No tienes permisos para acceder a esta API\"}")
                                    .build()
                    );
                    return;
                }
            }

            request.setProperty("idUsuario", idUsuario);

        } catch (Exception e) {
            request.abortWith(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("{\"error\":\"Error validando token\"}")
                            .build()
            );
        }
    }
}
