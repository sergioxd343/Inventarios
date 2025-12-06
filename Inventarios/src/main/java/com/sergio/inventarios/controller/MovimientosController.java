/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sergio.inventarios.controller;

import com.sergio.inventarios.config.Conexion;
import com.sergio.inventarios.dao.ProductoDAO;
import com.sergio.inventarios.dao.SesionesDAO;
import com.sergio.inventarios.model.Movimiento;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.sql.Connection;

/**
 *
 * @author SISTEMAS DESARROLLO2
 */
@Path("/movimientos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovimientosController {

    @GET
    public Response listarGeneral(
            @HeaderParam("Authorization") String header) {

        try {
            if (header == null || !header.startsWith("Bearer ")) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"Token no proporcionado\"}")
                        .build();
            }

            String token = header.substring("Bearer ".length()).trim();

            Connection conn = Conexion.getConexion();
            SesionesDAO sesionesDAO = new SesionesDAO(conn);

            int idUsuario = sesionesDAO.validarToken(token);

            if (idUsuario == 0) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"Token inválido\"}")
                        .build();
            }

            ProductoDAO dao = new ProductoDAO(conn);

            return Response.ok(dao.listarHistorialGeneral()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener historial\"}")
                    .build();
        }
    }

    @PUT
    public Response insertar(Movimiento m,
            @HeaderParam("Authorization") String header) {
        try {

            if (header == null || !header.startsWith("Bearer ")) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"Token no proporcionado\"}")
                        .build();
            }
            String token = header.substring("Bearer ".length()).trim();

            Connection conn = Conexion.getConexion();
            ProductoDAO dao = new ProductoDAO(conn);
            SesionesDAO sesionesDAO = new SesionesDAO(conn);

            int idUsuario = sesionesDAO.validarToken(token);

            m.setIdUsuario(idUsuario);

            boolean insertado = dao.insertarMovimiento(m);

            if (!insertado) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo registrar el movimiento\"}")
                        .build();
            }

            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\":\"Movimiento registrado correctamente\"}")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Ocurrió un error al registrar el movimiento\"}")
                    .build();
        }
    }
}
