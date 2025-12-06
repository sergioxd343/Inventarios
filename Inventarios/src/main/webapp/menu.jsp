<%-- 
    Document   : menu
    Created on : 4 dic 2025, 2:38:34 p.m.
    Author     : SISTEMAS DESARROLLO2
--%>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>


<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav id="sidebar" class="d-flex flex-column p-3">
    <h4 class="text-center">Menu</h4>
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <a href="tablero.jsp" class="nav-link <%= "tablero.jsp".equals(request.getServletPath()) ? "active" : ""%>">Inicio</a>
        </li>
        <li class="nav-item">
            <a href="inventario.jsp" class="nav-link" id="linkInventario">Inventario</a>
        </li>
        <li class="nav-item">
            <a href="salida.jsp" class="nav-link" id="linkSalida">Salida productos</a>
        </li>
        <li class="nav-item">
            <a href="historico.jsp" class="nav-link" id="linkHistorico">Historico</a>
        </li>
    </ul>

    <!-- Botón cerrar sesión -->
    <div class="mt-auto pt-3">
        <button id="btnCerrar" class="btn btn-danger w-100">Cerrar sesión</button>
    </div>
</nav>

<script>
    const token = localStorage.getItem("token");
    const rol = parseInt(localStorage.getItem("rol"));

    if (!token) {
        window.location.href = "login.html";
    }

    // Ocultar enlaces según rol
    if (rol !== 1 && rol !== 2)
        document.getElementById("linkInventario").style.display = "none";
    if (rol !== 2)
        document.getElementById("linkSalida").style.display = "none";
    if (rol !== 1)
        document.getElementById("linkHistorico").style.display = "none";

    // Cerrar sesión
    document.getElementById("btnCerrar").addEventListener("click", function () {

        // Limpieza del navegador
        localStorage.removeItem("token");
        localStorage.removeItem("rol");
        localStorage.removeItem("usuario");


        fetch("api/auth/logout", {
            method: "POST",
            headers: {"Authorization": "Bearer " + token}
        });

        window.location.href = "login.html";
    });
</script>
