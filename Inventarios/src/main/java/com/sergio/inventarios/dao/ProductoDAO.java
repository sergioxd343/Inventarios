/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sergio.inventarios.dao;

import com.sergio.inventarios.model.Producto;
import com.sergio.inventarios.model.Movimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SISTEMAS DESARROLLO2
 */
public class ProductoDAO {

    private Connection conn;

    public ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Producto> listar(int estatus) throws Exception {
        String sql = "SELECT * FROM productos WHERE estatus = ?";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ps.setInt(1, estatus);
        ResultSet rs = ps.executeQuery();

        List<Producto> lista = new ArrayList<>();

        while (rs.next()) {
            Producto p = new Producto();
            p.setIdProducto(rs.getInt("idProducto"));
            p.setNombre(rs.getString("nombre"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setCantidad(rs.getInt("cantidad"));

            lista.add(p);
        }

        rs.close();
        ps.close();

        return lista;
    }

    public boolean insertar(Producto p) throws Exception {
        String sql = "INSERT INTO productos (nombre, descripcion, cantidad) VALUES (?, ?, ?)";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ps.setString(1, p.getNombre());
        ps.setString(2, p.getDescripcion());
        ps.setInt(3, p.getCantidad());

        int filas = ps.executeUpdate();
        ps.close();

        return filas > 0;
    }

    public boolean actualizar(Producto p) throws Exception {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, cantidad = ?, estatus = ? WHERE idProducto = ?";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ps.setString(1, p.getNombre());
        ps.setString(2, p.getDescripcion());
        ps.setInt(3, p.getCantidad());
        ps.setLong(4, p.getEstatus());
        ps.setInt(5, p.getIdProducto());

        int filas = ps.executeUpdate();
        ps.close();

        return filas > 0;
    }

    public boolean insertarMovimiento(Movimiento m) throws Exception {
        String sql = "INSERT INTO `bd_inventario`.`movimientos` (`idProducto`, `idUsuario`, `tipo`, `cantidad`) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ps.setInt(1, m.getIdProducto());
        ps.setInt(2, m.getIdUsuario());
        ps.setString(3, m.getTipo());
        ps.setInt(4, m.getCantidad());

        int filas = ps.executeUpdate();

        ps.close();

        return filas > 0;
    }

    public List<Movimiento> listarHistorialGeneral() throws Exception {

        String sql = "SELECT m.idMovimiento, m.idProducto, "
                + "p.nombre AS nombreProducto, "
                + "m.idUsuario, "
                + "u.nombre AS nombreUsuario, "
                + "m.tipo, m.cantidad, m.fechaMovimiento "
                + "FROM movimientos m "
                + "INNER JOIN productos p ON m.idProducto = p.idProducto "
                + "LEFT JOIN usuarios u ON m.idUsuario = u.idUsuario "
                + "ORDER BY m.fechaMovimiento DESC";

        PreparedStatement ps = this.conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<Movimiento> lista = new ArrayList<>();

        while (rs.next()) {
            Movimiento mov = new Movimiento();
            mov.setIdMovimiento(rs.getInt("idMovimiento"));
            mov.setIdProducto(rs.getInt("idProducto"));
            mov.setNombreProducto(rs.getString("nombreProducto"));
            mov.setIdUsuario(rs.getInt("idUsuario"));
            mov.setNombreUsuario(rs.getString("nombreUsuario"));
            mov.setTipo(rs.getString("tipo"));
            mov.setCantidad(rs.getInt("cantidad"));
            mov.setFechaMovimiento(rs.getTimestamp("fechaMovimiento"));
            lista.add(mov);
        }

        rs.close();
        ps.close();

        return lista;
    }

    public boolean aumentarInventario(int id, int cantidad) throws SQLException {
        String sql = "UPDATE productos SET cantidad = cantidad + ? WHERE idProducto = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, cantidad);
        ps.setInt(2, id);
        return ps.executeUpdate() > 0;
    }

    public boolean disminuirInventario(int id, int cantidad) throws SQLException {
        String sql = "UPDATE productos SET cantidad = cantidad - ? WHERE idProducto = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, cantidad);
        ps.setInt(2, id);
        return ps.executeUpdate() > 0;
    }

    public boolean darDeBaja(int id) throws SQLException {
        String sql = "UPDATE productos SET estatus = 0 WHERE idProducto = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    }

    public boolean darDeAlta(int id) throws SQLException {
        String sql = "UPDATE productos SET estatus = 1 WHERE idProducto = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    }

}
