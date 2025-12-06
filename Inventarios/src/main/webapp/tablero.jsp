<!DOCTYPE html>
<html>
<head>
    <title>Inventario</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { overflow-x: hidden; }
        #sidebar { min-width: 200px; max-width: 200px; min-height: 100vh; background: #343a40; color: #fff; }
        #sidebar .nav-link { color: #fff; }
        #sidebar .nav-link.active { background-color: #495057; }
    </style>
</head>
<body>
    <div class="d-flex">
        <%@ include file="menu.jsp" %>

        <div class="flex-grow-1 p-4">
            <h1>Bienvenido</h1>
            <p>Tablero</p>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const token = localStorage.getItem("token");
        const rol = parseInt(localStorage.getItem("rol"));

        if (!token) window.location.href = "login.jsp";

        if (rol !== 1 && rol !== 2) document.getElementById("linkInventario").style.display = "none";
        if (rol !== 2) document.getElementById("linkSalida").style.display = "none";
        if (rol !== 1) document.getElementById("linkHistorico").style.display = "none";
    </script>
</body>
</html>
