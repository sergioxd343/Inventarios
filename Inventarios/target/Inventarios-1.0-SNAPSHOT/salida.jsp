<!DOCTYPE html>
<html>
    <head>
        <title>Salidas de Inventario</title>
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

                <h2>Salidas de Inventario</h2>

                <table class="table table-bordered" id="tablaSalidas">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Producto</th>
                            <th>Descripción</th>
                            <th>Cantidad disponible</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>

            </div>
        </div>

        <div class="modal fade" id="modalSalida" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title">Registrar salida</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>

                    <div class="modal-body">
                        <form id="formSalida">

                            <input type="hidden" id="idProductoSalida">

                            <div class="mb-3">
                                <label class="form-label">Producto</label>
                                <input type="text" class="form-control" id="nombreProductoSalida" readonly>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Existencia actual</label>
                                <input type="text" class="form-control" id="cantidadActualSalida" readonly>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Cantidad a sacar</label>
                                <input type="number" class="form-control" id="cantidadSalida" required min="1">
                            </div>

                            <button type="submit" class="btn btn-primary">Registrar salida</button>

                        </form>
                    </div>

                </div>
            </div>
        </div>

        <script>

            const apiProductos = "./api/productos/activos";
            const apiMovimientos = "./api/movimientos";

            function listarProductos() {
                $.ajax({
                    url: apiProductos,
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    success: function (data) {

                        const tbody = $("#tablaSalidas tbody");
                        tbody.empty();

                        data.forEach(p => {

                            var id = p.idProducto != null ? p.idProducto : "-";
                            var nombre = p.nombre != null ? p.nombre : "-";
                            var descripcion = p.descripcion != null ? p.descripcion : "-";
                            var cantidad = p.cantidad != null ? p.cantidad : 0;

                            var fila =
                                    "<tr>" +
                                    "<td>" + id + "</td>" +
                                    "<td>" + nombre + "</td>" +
                                    "<td>" + descripcion + "</td>" +
                                    "<td>" + cantidad + "</td>" +
                                    "<td><button class=\"btn btn-warning btn-sm btnSalida\" " +
                                    "data-id=\"" + id + "\" data-nombre=\"" + nombre + "\" data-cantidad=\"" + cantidad + "\">" +
                                    "Sacar</button></td>" +
                                    "</tr>";

                            tbody.append(fila);
                        });
                    }
                });
            }

            $(document).on("click", ".btnSalida", function () {

                $("#idProductoSalida").val($(this).data("id"));
                $("#nombreProductoSalida").val($(this).data("nombre"));
                $("#cantidadActualSalida").val($(this).data("cantidad"));
                $("#cantidadSalida").val("");

                $("#modalSalida").modal("show");
            });

            $("#formSalida").submit(function (e) {
                e.preventDefault();

                const idProducto = $("#idProductoSalida").val();
                const cantidad = $("#cantidadSalida").val();
                const cantidadActual = $("#cantidadActualSalida").val();

                if (parseInt(cantidad) > parseInt(cantidadActual)) {
                    Swal.fire({
                        icon: "error",
                        title: "Cantidad inválida",
                        text: "No puedes sacar más de lo disponible."
                    });
                    return;
                }

                const data = {
                    idProducto: idProducto,
                    tipo: "salida",
                    cantidad: cantidad
                };

                $.ajax({
                    url: apiMovimientos,
                    method: "PUT",
                    headers: {"Authorization": "Bearer " + token},
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success: function () {

                        $("#modalSalida").modal("hide");

                        Swal.fire({
                            icon: "success",
                            title: "Salida registrada",
                            timer: 1200,
                            showConfirmButton: false
                        });

                        listarProductos();
                    },
                    error: function (err) {
                        console.error("Error al registrar salida:", err);

                        Swal.fire({
                            icon: "error",
                            title: "Error",
                            text: "Ocurrió un problema al registrar la salida."
                        });
                    }
                });
            });


            $(document).ready(function () {
                listarProductos();
            });

        </script>
    </body>
</html>
