/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sergio.inventarios.dao;

import com.sergio.inventarios.model.Usuario;
import java.sql.*;

/**
 *
 * @author SISTEMAS DESARROLLO2
 */
public class UsuarioDAO {

    private Connection conn;

    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    public Usuario buscarPorCorreo(String correo) throws Exception {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, correo);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getInt("idUsuario"));
            u.setNombre(rs.getString("nombre"));
            u.setCorreo(rs.getString("correo"));
            u.setContrasena(rs.getString("contrasena"));
            u.setIdRol(rs.getInt("idRol"));
            u.setEstatus(rs.getInt("estatus"));
            return u;
        }
        return null;
    }

    public int obtenerRolUsuario(int idUsuario) throws Exception {
        String sql = "SELECT * FROM usuarios WHERE idUsuario = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idUsuario);
        ResultSet rs = ps.executeQuery();

        int idRol = -1;
        if (rs.next()) {
            idRol = rs.getInt("idRol");
        }
        rs.close();
        ps.close();

        return idUsuario;
    }

}
