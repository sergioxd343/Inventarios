# Inventarios

# Sistema de Inventario – Java EE

Este proyecto es una aplicación para la gestión de inventarios, desarrollada con Java EE con Web Pages y conectada a una base de datos MySQL. Incluye módulos de Inventario, Salidas de inventario, historial de movimientos, sesiones y seguridad basada en tokens.

---

## Tecnologías utilizadas

### IDE de desarrollo

Apache NetBeans (Versión recomendada: 28)

### Lenguaje de programación

JDK 21

### DBMS

MySQL 8.0

### Servidor de aplicaciones

Payara Server 7 (compatible con Jakarta EE)

### Administrador de dependencias

Maven 3.9.11

---

## Contenido del repositorio

Este proyecto debe incluir los siguientes archivos relevantes para su ejecución:

- README.md

- Carpeta "/Inventarios" 

- Carpeta "/SCRIPTS"
  - `01_creacion_bd.sql` 
  - `02_triggers.sql` 
  - `03_datos_iniciales.sql` 

---

## Pasos para ejecutar la aplicación

Sigue estos pasos para correr el sistema en un entorno local:

### Clonar el repositorio

- git clone https://github.com/sergioxd343/Inventarios.git

### Abrir el proyecto en NetBeans

- Abrir Apache NetBeans.

- Ir a File / Open Project.

- Seleccionar la carpeta /Inventarios que viene dentro del repositorio.

- Esperar a que Maven descargue todas las dependencias automáticamente.

## Abrir MySQL Workbench o tu cliente preferido.

Ejecutar los scripts en la carpeta /SCRIPTS en este orden:

        01_creacion_bd.sql
        02_triggers.sql
        03_datos_iniciales.sql

## Configurar la conexión a la base de datos

- En el proyecto, ubicar la clase:

src/java/com/sergio/inventarios/config/Conexion.java

-Verificar o modificar:

        Usuario de MySQL
        Contraseña
        Nombre de la base de datos
        URL de conexión JDBC

-Ejemplo

private static final String URL = "jdbc:mysql://localhost:3306/bd_inventario?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "root";

## Configurar Payara Server 7

En NetBeans, ir a Tools / Servers.

Hacer clic en Add Server.

Seleccionar Payara Server / Next.

Elegir nuevamente Payara Server → Next.

Buscar la carpeta tools/payara7 dentro del repositorio clonado.

Seleccionarla y continuar.

Asignar un nombre para el dominio y finalizar la configuración.

## Ejecutar la aplicación

En NetBeans, hacer clic derecho sobre el proyecto.

Seleccionar Run.

Esperar a que Payara despliegue la aplicación.

La aplicación estará disponible en:

http://localhost:8080/Inventarios

### Notas

## Credenciales de prueba
Administrador

Correo: admin@example.com
Contraseña: Todopoderosa2024

Almacenista

Correo: almacenista@example.com
Contraseña: Todopoderosa2024
