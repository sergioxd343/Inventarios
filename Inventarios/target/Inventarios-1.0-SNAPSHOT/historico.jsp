<!DOCTYPE html>
<html>
    <head>
        <title>Histórico de Movimientos</title>
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

                <h2>Histórico de Movimientos</h2>
                <div class="mb-3" style="max-width:200px;">
                    <label class="form-label">Filtrar por tipo:</label>
                    <select id="filtroTipo" class="form-select">
                        <option value="">Todos</option>
                        <option value="entrada">Entradas</option>
                        <option value="salida">Salidas</option>
                    </select>
                </div>
                <table class="table table-bordered table-striped" id="tablaMovimientos">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Producto</th>
                            <th>Usuario</th>
                            <th>Tipo</th>
                            <th>Cantidad</th>
                            <th>Fecha</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>

            </div>
        </div>

        <script>

            let tablaMovimientosDT = null;
            const apiMovimientos = './api/movimientos';

            function listarMovimientos() {

                $.ajax({
                    url: apiMovimientos,
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token
                    },
                    success: function (data) {

                        if (tablaMovimientosDT !== null) {
                            tablaMovimientosDT.destroy();
                        }

                        const tbody = $('#tablaMovimientos tbody');
                        tbody.empty();

                        data.forEach(m => {

                            const id = m.idMovimiento ?? '-';
                            const producto = m.nombreProducto ?? '-';
                            const usuario = m.nombreUsuario ?? '-';
                            const tipo = m.tipo ?? '-';
                            const cantidad = m.cantidad ?? 0;
                            const fecha = m.fechaMovimiento ?? '-';

                            let fila =
                                    '<tr>' +
                                    '<td>' + id + '</td>' +
                                    '<td>' + producto + '</td>' +
                                    '<td>' + usuario + '</td>' +
                                    '<td>' + tipo + '</td>' +
                                    '<td>' + cantidad + '</td>' +
                                    '<td>' + fecha + '</td>' +
                                    '</tr>';

                            tbody.append(fila);
                        });

                        tablaMovimientosDT = $('#tablaMovimientos').DataTable({
                            pageLength: 10,
                            order: [[5, 'desc']],
                            language: {
                                url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-MX.json'
                            }
                        });

                        $('#filtroTipo').off().on('change', function () {
                            let valor = $(this).val();

                            tablaMovimientosDT.column(3).search(valor).draw();
                        });

                    },
                    error: function (err) {
                        console.error('Error al obtener historial:', err);
                    }
                });
            }

            $(document).ready(function () {
                listarMovimientos();
            });

        </script>
    </body>
</html>
