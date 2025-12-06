DELIMITER $$

CREATE TRIGGER trg_salida_inventario
BEFORE INSERT ON movimientos
FOR EACH ROW
BEGIN
    DECLARE inventarioActual INT;
    
    SELECT cantidad INTO inventarioActual
    FROM productos
    WHERE idProducto = NEW.idProducto;

    IF NEW.tipo = 'SALIDA' THEN
        IF inventarioActual < NEW.cantidad THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'No hay suficiente inventario para realizar la salida.';
        END IF;
    END IF;
END$$

DELIMITER ;



DELIMITER $$

CREATE TRIGGER trg_actualizar_inventario
AFTER INSERT ON movimientos
FOR EACH ROW
BEGIN
    IF NEW.tipo = 'ENTRADA' THEN
        UPDATE productos
        SET cantidad = cantidad + NEW.cantidad
        WHERE idProducto = NEW.idProducto;

    ELSEIF NEW.tipo = 'SALIDA' THEN
        UPDATE productos
        SET cantidad = cantidad - NEW.cantidad
        WHERE idProducto = NEW.idProducto;
    END IF;
END$$

DELIMITER ;