/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sergio.inventarios.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 *
 * @author SISTEMAS DESARROLLO2
 */
public class SesionesDAO {

    private Connection conn;

    public SesionesDAO(Connection conn) {
        this.conn = conn;
    }

    public void crearSesion(int idUsuario, String token, Timestamp fechaExpiracion) throws Exception {
        String sql = "INSERT INTO sesiones (idUsuario, token, fechaExpiracion) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idUsuario);
        ps.setString(2, token);
        ps.setTimestamp(3, fechaExpiracion);
        ps.executeUpdate();
        ps.close();
    }

    public void eliminarSesion(String token) throws Exception {
        String sql = "UPDATE sesiones SET fechaExpiracion = NOW() WHERE token = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error al expirar la sesión: " + e.getMessage(), e);
        }
    }

    public int validarToken(String token) throws Exception {
        String sql = "SELECT idUsuario FROM sesiones WHERE token = ? AND fechaExpiracion > NOW()";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, token);
        ResultSet rs = ps.executeQuery();

        int idUsuario = -1;
        if (rs.next()) {
            idUsuario = rs.getInt("idUsuario");
        }

        rs.close();
        ps.close();
        return idUsuario;
    }
}
