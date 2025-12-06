/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sergio.inventarios.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 *
 * @author SISTEMAS DESARROLLO2
 */

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class JWTFilter {
    // La misma llave que usas en AuthService
    private static final String SECRET_KEY = "Todopoderosa2024";

    public void filter(ContainerRequestContext request) {

        String path = request.getUriInfo().getPath();

        // Rutas públicas
        if (path.startsWith("auth/login")) {
            return;
        }

        // Obtener el header Authorization
        String authHeader = request.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            request.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{\"error\":\"No autorizado: falta token\"}")
                            .build()
            );
            return;
        }

        String token = authHeader.substring("Bearer".length()).trim();

        try {
            // Validación del JWT
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);

        } catch (SignatureException e) {
            request.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{\"error\":\"Token inválido\"}")
                            .build()
            );
        } catch (Exception e) {
            request.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{\"error\":\"Token expirado o corrupto\"}")
                            .build()
            );
        }
    }
}
