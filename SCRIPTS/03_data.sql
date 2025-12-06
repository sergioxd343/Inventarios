INSERT INTO bd_inventario.roles (nombreRol, descripcion) VALUES
('Administrador', 'Administrador del sistema con todos los privilegios'),
('Almacenista', 'Usuario con permisos limitados');

INSERT INTO bd_inventario.usuarios (nombre, correo, contrasena, idRol) VALUES
('Admin', 'admin@example.com', '$2a$10$1RUPGOo2FpvWq.bl6MwuguxmYlujaBNpJGPVH/zjvpFW7UvHB.C2q', 1);

INSERT INTO bd_inventario.usuarios (nombre, correo, contrasena, idRol) VALUES
('Almacenista', 'almacenista@example.com', '$2a$10$aHr5/lrDa6EpgbtO1oBl4uI2WJHo96UW54pwK1ii1wYTZS12TtChm', 2);