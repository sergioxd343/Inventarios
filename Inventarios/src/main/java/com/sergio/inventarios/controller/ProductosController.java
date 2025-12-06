/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sergio.inventarios.controller;

import com.sergio.inventarios.config.Conexion;
import com.sergio.inventarios.dao.ProductoDAO;
import com.sergio.inventarios.model.Producto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author SISTEMAS DESARROLLO2
 */

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductosController {

    @GET
    @Path("/activos")
    public Response listarActivos() {
        try {
            Connection conn = Conexion.getConexion();

            ProductoDAO dao = new ProductoDAO(conn);

            List<Producto> productos = dao.listar(1);

            return Response.ok(productos).build();

        } catch (Exception e) {
            e.printStackTrace();

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Ocurrió un error al obtener los productos\"}")
                    .build();
        }
    }

    @GET
    @Path("/inactivos")
    public Response listarInactivos() {
        try {
            Connection conn = Conexion.getConexion();

            ProductoDAO dao = new ProductoDAO(conn);

            List<Producto> productos = dao.listar(0);

            return Response.ok(productos).build();

        } catch (Exception e) {
            e.printStackTrace();

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Ocurrió un error al obtener los productos\"}")
                    .build();
        }
    }

    @POST
    public Response agregar(Producto p) {
        try {
            Connection conn = Conexion.getConexion();
            ProductoDAO dao = new ProductoDAO(conn);

            boolean insertado = dao.insertar(p);

            if (!insertado) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo registrar el producto\"}")
                        .build();
            }

            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\":\"Producto registrado correctamente\"}")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Ocurrió un error al registrar el producto\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Producto p) {
        try {
            Connection conn = Conexion.getConexion();
            ProductoDAO dao = new ProductoDAO(conn);

            p.setIdProducto(id); // aseguramos el ID de la URL

            boolean actualizado = dao.actualizar(p);

            if (!actualizado) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo actualizar el producto\"}")
                        .build();
            }

            return Response.ok("{\"mensaje\":\"Producto actualizado correctamente\"}").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Ocurrió un error al actualizar el producto\"}")
                    .build();
        }
    }

    @PUT
    @Path("/inventario/{id}")
    public Response aumentarInventario(@PathParam("id") int id, @QueryParam("cantidad") int cantidad) {
        try {
            Connection conn = Conexion.getConexion();
            ProductoDAO dao = new ProductoDAO(conn);

            boolean actualizado = dao.aumentarInventario(id, cantidad);

            if (!actualizado) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo aumentar el inventario\"}")
                        .build();
            }

            return Response.ok("{\"mensaje\":\"Inventario actualizado correctamente\"}").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Ocurrió un error al actualizar inventario\"}")
                    .build();
        }
    }
    
    @PUT
    @Path("/inventario-disminuir/{id}")
    public Response disminuirInventario(@PathParam("id") int id, @QueryParam("cantidad") int cantidad) {
        try {
            Connection conn = Conexion.getConexion();
            ProductoDAO dao = new ProductoDAO(conn);

            boolean actualizado = dao.aumentarInventario(id, cantidad);

            if (!actualizado) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo aumentar el inventario\"}")
                        .build();
            }

            return Response.ok("{\"mensaje\":\"Inventario actualizado correctamente\"}").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Ocurrió un error al actualizar inventario\"}")
                    .build();
        }
    }

    @PUT
    @Path("/baja/{id}")
    public Response darDeBaja(@PathParam("id") int id) {
        try {
            Connection conn = Conexion.getConexion();
            ProductoDAO dao = new ProductoDAO(conn);

            boolean actualizado = dao.darDeBaja(id);

            if (!actualizado) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo dar de baja el producto\"}")
                        .build();
            }

            return Response.ok("{\"mensaje\":\"Producto dado de baja correctamente\"}").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Ocurrió un error al dar de baja el producto\"}")
                    .build();
        }
    }

    @PUT
    @Path("/alta/{id}")
    public Response darDeAlta(@PathParam("id") int id) {
        try {
            Connection conn = Conexion.getConexion();
            ProductoDAO dao = new ProductoDAO(conn);

            boolean actualizado = dao.darDeAlta(id);

            if (!actualizado) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"No se pudo dar de alta el producto\"}")
                        .build();
            }

            return Response.ok("{\"mensaje\":\"Producto dado de alta correctamente\"}").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Ocurrió un error al dar de alta el producto\"}")
                    .build();
        }
    }

}