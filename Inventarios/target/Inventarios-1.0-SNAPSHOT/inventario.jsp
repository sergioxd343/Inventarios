<!DOCTYPE html>
<html>
    <head>
        <title>Inventario</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
     

        <style>
            body {
                overflow-x: hidden;
            }
            #sidebar {
                min-width: 200px;
                max-width: 200px;
                min-height: 100vh;
                background: #343a40;
                color: #fff;
            }
            #sidebar .nav-link {
                color: #fff;
            }
            #sidebar .nav-link.active {
                background-color: #495057;
            }
        </style>
    </head>
    <body>

        <div class="d-flex">
            <%@ include file="menu.jsp" %>
            <div class="container mt-4">

                <h2>Inventario</h2>
                <% if (session.getAttribute("idRol").equals(1)) { %>
                <button class="btn btn-primary mb-3" id="btnAgregar">Agregar Producto</button>
                <% } %>
                <div class="mb-3" style="max-width: 200px;">
                    <label class="form-label">Mostrar:</label>
                    <select id="filtroEstado" class="form-select">
                        <option value="activos" selected>Activos</option>
                        <option value="inactivos">Inactivos</option>
                    </select>
                </div>
                <table class="table table-bordered table-striped" id="tablaInventario">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Descripción</th>
                            <th>Cantidad</th>
                                <% if (session.getAttribute("idRol").equals(1)) { %>
                            <th>Acciones</th>
                                <% }%>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>

            </div>
        </div>

        <!-- Modal Agregar -->
        <div class="modal fade" id="modalAgregar" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title">Agregar Producto</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>

                    <div class="modal-body">
                        <form id="formAgregar">
                            <div class="mb-3">
                                <label class="form-label">Nombre</label>
                                <input type="text" class="form-control" id="nombreProducto" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Descripcion</label>
                                <textarea class="form-control" id="descripcionProducto" required></textarea>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Cantidad</label>
                                <input type="number" class="form-control" id="cantidadProducto" required min="0">
                            </div>

                            <button type="submit" class="btn btn-primary">Guardar</button>
                        </form>
                    </div>

                </div>
            </div>
        </div>

        <script>

            const api = "./api/productos";

            let tablaDT = null;

            function listarProductos(tipo = "activos") {

                $.ajax({
                    url: api + "/" + tipo,
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    success: function (data) {

                        // Si ya existe un DataTable, se destruye
                        if (tablaDT !== null) {
                            tablaDT.destroy();
                        }

                        const tbody = $("#tablaInventario tbody");
                        tbody.empty();

                        data.forEach(p => {

                            var id = p.idProducto ?? "-";
                            var nombre = p.nombre ?? "-";
                            var descripcion = p.descripcion ?? "-";
                            var cantidad = p.cantidad ?? 0;

                            var fila =
                                    '<tr>' +
                                    '<td>' + id + '</td>' +
                                    '<td>' + nombre + '</td>' +
                                    '<td>' + descripcion + '</td>' +
                                    '<td>' + cantidad + '</td>';

                            if (rol === 1) {

                                fila += '<td>';

                                if (tipo === 'activos') {
                                    fila +=
                                            '<button class="btn btn-secondary btn-sm btnAumentar" data-id="' + id + '">+ Inventario</button> ' +
                                            '<button class="btn btn-danger btn-sm btnBaja" data-id="' + id + '">Dar de baja</button>';
                                } else {
                                    fila +=
                                            '<button class="btn btn-secondary btn-sm btnAumentar" data-id="' + id + '">+ Inventario</button> ' +
                                            '<button class="btn btn-success btn-sm btnAlta" data-id="' + id + '">Dar de alta</button>';
                                }

                                fila += '</td>';
                            }

                            fila += '</tr>';


                            tbody.append(fila);
                        });

                        // Inicializar DataTable nuevamente
                        tablaDT = $("#tablaInventario").DataTable({
                            pageLength: 10,
                            language: {
                                url: "https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-MX.json"
                            }
                        });

                    },
                    error: function (err) {
                        console.error("Error al listar productos:", err);
                    }
                });

            }

            $("#filtroEstado").change(function () {
                const tipo = $(this).val();
                listarProductos(tipo);
            });


            $("#btnAgregar").click(function () {
                $("#modalAgregar").modal("show");
            });

            $("#formAgregar").submit(function (e) {
                e.preventDefault();

                const data = {
                    nombre: $("#nombreProducto").val(),
                    descripcion: $("#descripcionProducto").val(),
                    cantidad: $("#cantidadProducto").val()
                };

                $.ajax({
                    url: api,
                    method: "POST",
                    headers: {"Authorization": "Bearer " + token},
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success: function () {
                        $("#modalAgregar").modal("hide");
                        $("#formAgregar")[0].reset();

                        Swal.fire({
                            icon: "success",
                            title: "Producto guardado",
                            timer: 1200,
                            showConfirmButton: false
                        });

                        listarProductos("activos");
                    }
                });
            });


            // AUMENTAR INVENTARIO
            $(document).on("click", ".btnAumentar", async function () {
                const id = $(this).data("id");

                const {value: cantidad} = await Swal.fire({
                    title: "Aumentar inventario",
                    input: "number",
                    inputAttributes: {min: 1},
                    inputLabel: "Cantidad",
                    inputPlaceholder: "Ingrese cantidad a aumentar",
                    showCancelButton: true,
                    confirmButtonText: "Aceptar"
                });

                if (!cantidad)
                    return;

                const data = {
                    idProducto: id,
                    tipo: "entrada",
                    cantidad: cantidad
                };

                $.ajax({
                    url: "api/movimientos",
                    method: "PUT",
                    headers: {"Authorization": "Bearer " + token},
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success: function () {

                        Swal.fire({
                            icon: "success",
                            title: "Inventario actualizado",
                            timer: 1200,
                            showConfirmButton: false
                        });

                        listarProductos("activos");
                    }
                });
            });


            // DAR DE ALTA
            $(document).on("click", ".btnAlta", function () {
                const id = $(this).data("id");

                Swal.fire({
                    title: "Confirmar",
                    text: "¿Desea dar de alta este producto?",
                    icon: "question",
                    showCancelButton: true,
                    confirmButtonText: "Sí, continuar",
                    cancelButtonText: "Cancelar"
                }).then(result => {
                    if (!result.isConfirmed)
                        return;

                    $.ajax({
                        url: api + "/alta/" + id,
                        method: "PUT",
                        headers: {"Authorization": "Bearer " + token},
                        success: function () {

                            Swal.fire({
                                icon: "success",
                                title: "Producto dado de alta",
                                timer: 1200,
                                showConfirmButton: false
                            });

                            listarProductos("inactivos");
                        }
                    });
                });
            });


            // DAR DE BAJA
            $(document).on("click", ".btnBaja", function () {
                const id = $(this).data("id");

                Swal.fire({
                    title: "Confirmar",
                    text: "¿Desea dar de baja este producto?",
                    icon: "warning",
                    showCancelButton: true,
                    confirmButtonText: "Sí, continuar",
                    cancelButtonText: "Cancelar"
                }).then(result => {
                    if (!result.isConfirmed)
                        return;

                    $.ajax({
                        url: api + "/baja/" + id,
                        method: "PUT",
                        headers: {"Authorization": "Bearer " + token},
                        success: function () {

                            Swal.fire({
                                icon: "success",
                                title: "Producto dado de baja",
                                timer: 1200,
                                showConfirmButton: false
                            });

                            listarProductos("activos");
                        }
                    });
                });
            });


            $(document).ready(function () {
                listarProductos("activos");
            });

        </script>


        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
