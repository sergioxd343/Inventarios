/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sergio.inventarios.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author SISTEMAS DESARROLLO2
 */
@WebFilter("/*")
public class PageFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String context = req.getContextPath();

        String path = req.getRequestURI();
        String page = path.substring(path.lastIndexOf("/") + 1);

        if (page.equals("login.html")
                || path.contains("/assets/")
                || path.contains("/css/")
                || path.contains("/js/")
                || path.contains("/auth")
                || path.endsWith("LoginController")
                || path.contains("/api/")) {

            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("token") == null) {
            resp.sendRedirect(context + "/login.html"); 
            return;
        }

        Integer idRol = (Integer) session.getAttribute("idRol");
        if (page.equals("inventario.jsp") && (idRol != 1 && idRol != 2)) { 
            resp.sendRedirect(context + "/tablero.jsp");
            return;
        }
        if (page.equals("salida.jsp") && idRol != 2) {
            resp.sendRedirect(context + "/tablero.jsp");
            return;
        }
        if (page.equals("historico.jsp") && idRol != 1) {
            resp.sendRedirect(context + "/tablero.jsp");
            return;
        }
        chain.doFilter(request, response);
    }
}
