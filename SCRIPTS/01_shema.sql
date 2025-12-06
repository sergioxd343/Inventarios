CREATE DATABASE bd_inventario;

USE bd_inventario;

CREATE TABLE bd_inventario.roles (
    idRol INT AUTO_INCREMENT PRIMARY KEY,
    nombreRol VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255),
    estatus TINYINT DEFAULT 1,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fechaActualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE bd_inventario.usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    idRol INT NOT NULL,
    estatus TINYINT DEFAULT 1,
    FOREIGN KEY (idRol) REFERENCES bd_inventario.roles(idRol)
);

CREATE TABLE bd_inventario.sesiones (
    idSesion INT AUTO_INCREMENT PRIMARY KEY,
    idUsuario INT NOT NULL,
    token TEXT NOT NULL,
    fechaInicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fechaExpiracion TIMESTAMP,
    FOREIGN KEY (idUsuario) REFERENCES bd_inventario.usuarios(idUsuario)
);

CREATE TABLE bd_inventario.productos (
    idProducto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    cantidad INT NOT NULL DEFAULT 0,
    estatus TINYINT DEFAULT 1,
    fechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fechaActualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
        ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE bd_inventario.movimientos (
    idMovimiento INT AUTO_INCREMENT PRIMARY KEY,
    idProducto INT NOT NULL,
    idUsuario INT NOT NULL,
    tipo ENUM('ENTRADA', 'SALIDA') NOT NULL,
    cantidad INT NOT NULL,
    fechaMovimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idProducto) REFERENCES productos(idProducto),
    FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario)
);
