-- =============================================
-- SISTEMA BANCARIO - Script de Base de Datos
-- =============================================

-- Crear la base de datos si no existe
DROP DATABASE IF EXISTS sistema_bancario;
CREATE DATABASE sistema_bancario;
USE sistema_bancario;

-- =============================================
-- TABLA: Usuario
-- =============================================
CREATE TABLE Usuario (
    cedula VARCHAR(20) PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    correo_electronico VARCHAR(100) UNIQUE NOT NULL,
    contrasenia VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ADMINISTRADOR', 'CLIENTE')),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_correo (correo_electronico),
    INDEX idx_tipo (tipo)
);

-- =============================================
-- TABLA: Cliente
-- =============================================
CREATE TABLE Cliente (
    cedula VARCHAR(20) PRIMARY KEY,
    sexo CHAR(1) NOT NULL CHECK (sexo IN ('M', 'F', 'O')),
    profesion VARCHAR(100),
    direccion VARCHAR(200),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cedula) REFERENCES Usuario(cedula) ON DELETE CASCADE,
    INDEX idx_sexo (sexo),
    INDEX idx_profesion (profesion)
);

-- =============================================
-- TABLA: Cuenta
-- =============================================
CREATE TABLE Cuenta (
    numero_cuenta VARCHAR(50) PRIMARY KEY,
    cedula_cliente VARCHAR(20) NOT NULL,
    saldo DECIMAL(15, 2) DEFAULT 0.00,
    activa BOOLEAN DEFAULT TRUE,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('AHORRO', 'DEBITO', 'CREDITO')),
    porcentaje_interes DECIMAL(5, 2),
    limite_credito DECIMAL(15, 2),
    tipo_credito VARCHAR(50),
    fecha_apertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_ultimo_movimiento DATE,
    FOREIGN KEY (cedula_cliente) REFERENCES Cliente(cedula) ON DELETE CASCADE,
    INDEX idx_cliente (cedula_cliente),
    INDEX idx_tipo (tipo),
    INDEX idx_activa (activa),
    INDEX idx_fecha_movimiento (fecha_ultimo_movimiento),
    CHECK (saldo >= -1000000000 AND saldo <= 1000000000),
    CHECK (
        (tipo = 'CREDITO' AND limite_credito IS NOT NULL) OR
        (tipo != 'CREDITO' AND (limite_credito IS NULL OR limite_credito = 0))
    )
);

-- =============================================
-- TABLA: Transaccion
-- =============================================
CREATE TABLE Transaccion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('DEPOSITO', 'RETIRO', 'TRANSFERENCIA', 'PAGO_CREDITO', 'INTERES')),
    monto DECIMAL(15, 2) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    numero_cuenta_origen VARCHAR(50) NOT NULL,
    numero_cuenta_destino VARCHAR(50),
    descripcion VARCHAR(200),
    estado VARCHAR(20) DEFAULT 'COMPLETADA' CHECK (estado IN ('COMPLETADA', 'FALLIDA', 'PENDIENTE')),
    FOREIGN KEY (numero_cuenta_origen) REFERENCES Cuenta(numero_cuenta) ON DELETE CASCADE,
    FOREIGN KEY (numero_cuenta_destino) REFERENCES Cuenta(numero_cuenta) ON DELETE SET NULL,
    INDEX idx_tipo (tipo),
    INDEX idx_fecha (fecha),
    INDEX idx_cuenta_origen (numero_cuenta_origen),
    INDEX idx_cuenta_destino (numero_cuenta_destino),
    INDEX idx_estado (estado),
    CHECK (monto > 0),
    CHECK (
        (tipo = 'TRANSFERENCIA' AND numero_cuenta_destino IS NOT NULL) OR
        (tipo != 'TRANSFERENCIA')
    )
);

-- =============================================
-- INSERCIÓN DE DATOS DE PRUEBA
-- =============================================

-- Insertar usuarios administradores
INSERT INTO Usuario (cedula, nombre_completo, correo_electronico, contrasenia, tipo) VALUES
('000000000', 'Administrador Principal', 'admin@banco.com', 'admin123', 'ADMINISTRADOR'),
('111111111', 'Supervisor Sistema', 'supervisor@banco.com', 'super123', 'ADMINISTRADOR');

-- Insertar usuarios clientes
INSERT INTO Usuario (cedula, nombre_completo, correo_electronico, contrasenia, tipo) VALUES
('1234567890', 'Juan Pérez Martínez', 'juan.perez@email.com', 'cliente123', 'CLIENTE'),
('0987654321', 'María González López', 'maria.gonzalez@email.com', 'cliente123', 'CLIENTE'),
('1122334455', 'Carlos Rodríguez Sánchez', 'carlos.rodriguez@email.com', 'cliente123', 'CLIENTE'),
('5566778899', 'Ana Fernández García', 'ana.fernandez@email.com', 'cliente123', 'CLIENTE'),
('6677889900', 'Pedro Ramírez Díaz', 'pedro.ramirez@email.com', 'cliente123', 'CLIENTE');

-- Insertar clientes
INSERT INTO Cliente (cedula, sexo, profesion, direccion) VALUES
('1234567890', 'M', 'Ingeniero de Software', 'Av. Principal #123, Ciudad'),
('0987654321', 'F', 'Médico', 'Calle Central #456, Ciudad'),
('1122334455', 'M', 'Arquitecto', 'Boulevard Norte #789, Ciudad'),
('5566778899', 'F', 'Contadora', 'Calle Sur #101, Ciudad'),
('6677889900', 'M', 'Profesor Universitario', 'Av. Este #202, Ciudad');

-- Insertar cuentas bancarias
INSERT INTO Cuenta (numero_cuenta, cedula_cliente, saldo, tipo, porcentaje_interes) VALUES
('AHO-001-2024', '1234567890', 5000.00, 'AHORRO', 2.5),
('AHO-002-2024', '1234567890', 3000.00, 'AHORRO', 3.0),
('DEB-001-2024', '0987654321', 15000.00, 'DEBITO', 1.5),
('DEB-002-2024', '1122334455', 8000.00, 'DEBITO', 2.0),
('CRE-001-2024', '5566778899', -2000.00, 'CREDITO', NULL, 5000.00, 'PLATINUM'),
('CRE-002-2024', '6677889900', 0.00, 'CREDITO', NULL, 10000.00, 'GOLD');

-- Insertar transacciones de ejemplo
INSERT INTO Transaccion (tipo, monto, numero_cuenta_origen, descripcion, estado) VALUES
('DEPOSITO', 1000.00, 'AHO-001-2024', 'Depósito inicial', 'COMPLETADA'),
('DEPOSITO', 2000.00, 'AHO-002-2024', 'Ahorro mensual', 'COMPLETADA'),
('RETIRO', 500.00, 'DEB-001-2024', 'Retiro en cajero', 'COMPLETADA'),
('DEPOSITO', 3000.00, 'DEB-002-2024', 'Pago de nómina', 'COMPLETADA'),
('PAGO_CREDITO', 1000.00, 'CRE-001-2024', 'Pago tarjeta crédito', 'COMPLETADA');

INSERT INTO Transaccion (tipo, monto, numero_cuenta_origen, numero_cuenta_destino, descripcion, estado) VALUES
('TRANSFERENCIA', 1000.00, 'AHO-001-2024', 'DEB-001-2024', 'Transferencia a María', 'COMPLETADA'),
('TRANSFERENCIA', 500.00, 'DEB-002-2024', 'AHO-002-2024', 'Transferencia a ahorros', 'COMPLETADA');

-- =============================================
-- VISTAS PARA REPORTES
-- =============================================

-- Vista para clientes con su información consolidada
CREATE VIEW vista_clientes_consolidados AS
SELECT
    c.cedula,
    u.nombre_completo,
    u.correo_electronico,
    c.sexo,
    c.profesion,
    c.direccion,
    COUNT(cu.numero_cuenta) AS total_cuentas,
    COALESCE(SUM(cu.saldo), 0) AS saldo_total,
    c.fecha_creacion
FROM Cliente c
JOIN Usuario u ON c.cedula = u.cedula
LEFT JOIN Cuenta cu ON c.cedula = cu.cedula_cliente AND cu.activa = TRUE
GROUP BY c.cedula, u.nombre_completo, u.correo_electronico, c.sexo, c.profesion, c.direccion, c.fecha_creacion;

-- Vista para transacciones detalladas
CREATE VIEW vista_transacciones_detalladas AS
SELECT
    t.id,
    t.tipo,
    t.monto,
    t.fecha,
    t.numero_cuenta_origen,
    cu_origen.tipo AS tipo_cuenta_origen,
    cl_origen.cedula AS cedula_cliente_origen,
    u_origen.nombre_completo AS nombre_cliente_origen,
    t.numero_cuenta_destino,
    cu_destino.tipo AS tipo_cuenta_destino,
    t.descripcion,
    t.estado
FROM Transaccion t
JOIN Cuenta cu_origen ON t.numero_cuenta_origen = cu_origen.numero_cuenta
JOIN Cliente cl_origen ON cu_origen.cedula_cliente = cl_origen.cedula
JOIN Usuario u_origen ON cl_origen.cedula = u_origen.cedula
LEFT JOIN Cuenta cu_destino ON t.numero_cuenta_destino = cu_destino.numero_cuenta
ORDER BY t.fecha DESC;

-- Vista para cuentas con información del cliente
CREATE VIEW vista_cuentas_clientes AS
SELECT
    cu.numero_cuenta,
    cu.tipo,
    cu.saldo,
    cu.activa,
    cu.porcentaje_interes,
    cu.limite_credito,
    cu.tipo_credito,
    cu.fecha_apertura,
    cu.fecha_ultimo_movimiento,
    c.cedula,
    u.nombre_completo,
    u.correo_electronico,
    c.sexo,
    c.profesion
FROM Cuenta cu
JOIN Cliente c ON cu.cedula_cliente = c.cedula
JOIN Usuario u ON c.cedula = u.cedula
WHERE cu.activa = TRUE;

-- =============================================
-- PROCEDIMIENTOS ALMACENADOS
-- =============================================

-- Procedimiento para crear un nuevo cliente
DELIMITER $$
CREATE PROCEDURE sp_crear_cliente(
    IN p_cedula VARCHAR(20),
    IN p_nombre_completo VARCHAR(100),
    IN p_correo_electronico VARCHAR(100),
    IN p_sexo CHAR(1),
    IN p_profesion VARCHAR(100),
    IN p_direccion VARCHAR(200)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Error: No se pudo crear el cliente' AS mensaje;
    END;

    START TRANSACTION;

    -- Insertar usuario
    INSERT INTO Usuario (cedula, nombre_completo, correo_electronico, contrasenia, tipo)
    VALUES (p_cedula, p_nombre_completo, p_correo_electronico, 'cliente123', 'CLIENTE');

    -- Insertar cliente
    INSERT INTO Cliente (cedula, sexo, profesion, direccion)
    VALUES (p_cedula, p_sexo, p_profesion, p_direccion);

    COMMIT;
    SELECT 'Cliente creado exitosamente' AS mensaje;
END$$
DELIMITER ;

-- Procedimiento para registrar una transacción
DELIMITER $$
CREATE PROCEDURE sp_registrar_transaccion(
    IN p_tipo VARCHAR(20),
    IN p_monto DECIMAL(15,2),
    IN p_cuenta_origen VARCHAR(50),
    IN p_cuenta_destino VARCHAR(50),
    IN p_descripcion VARCHAR(200)
)
BEGIN
    DECLARE v_saldo_actual DECIMAL(15,2);
    DECLARE v_tipo_cuenta VARCHAR(20);
    DECLARE v_limite_credito DECIMAL(15,2);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        INSERT INTO Transaccion (tipo, monto, numero_cuenta_origen, numero_cuenta_destino, descripcion, estado)
        VALUES (p_tipo, p_monto, p_cuenta_origen, p_cuenta_destino, p_descripcion, 'FALLIDA');
        SELECT 'Error en la transacción' AS mensaje;
    END;

    START TRANSACTION;

    -- Obtener información de la cuenta origen
    SELECT saldo, tipo, limite_credito INTO v_saldo_actual, v_tipo_cuenta, v_limite_credito
    FROM Cuenta WHERE numero_cuenta = p_cuenta_origen;

    -- Validar según tipo de transacción
    IF p_tipo = 'RETIRO' THEN
        IF v_tipo_cuenta = 'AHORRO' AND (v_saldo_actual - p_monto < 100) THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Saldo mínimo no permitido para cuenta de ahorro';
        END IF;

        IF v_tipo_cuenta = 'DEBITO' AND v_saldo_actual < p_monto THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Saldo insuficiente';
        END IF;

        IF v_tipo_cuenta = 'CREDITO' AND (v_saldo_actual - p_monto < -v_limite_credito) THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Límite de crédito excedido';
        END IF;

        -- Actualizar saldo
        UPDATE Cuenta
        SET saldo = saldo - p_monto,
            fecha_ultimo_movimiento = CURDATE()
        WHERE numero_cuenta = p_cuenta_origen;

    ELSEIF p_tipo = 'DEPOSITO' THEN
        UPDATE Cuenta
        SET saldo = saldo + p_monto,
            fecha_ultimo_movimiento = CURDATE()
        WHERE numero_cuenta = p_cuenta_origen;

    ELSEIF p_tipo = 'TRANSFERENCIA' THEN
        -- Retirar de cuenta origen
        UPDATE Cuenta
        SET saldo = saldo - p_monto,
            fecha_ultimo_movimiento = CURDATE()
        WHERE numero_cuenta = p_cuenta_origen;

        -- Depositar en cuenta destino
        UPDATE Cuenta
        SET saldo = saldo + p_monto,
            fecha_ultimo_movimiento = CURDATE()
        WHERE numero_cuenta = p_cuenta_destino;
    END IF;

    -- Registrar transacción
    INSERT INTO Transaccion (tipo, monto, numero_cuenta_origen, numero_cuenta_destino, descripcion, estado)
    VALUES (p_tipo, p_monto, p_cuenta_origen, p_cuenta_destino, p_descripcion, 'COMPLETADA');

    COMMIT;
    SELECT 'Transacción realizada exitosamente' AS mensaje;
END$$
DELIMITER ;

-- Procedimiento para aplicar intereses a todas las cuentas de ahorro
DELIMITER $$
CREATE PROCEDURE sp_aplicar_intereses_mensuales()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_numero_cuenta VARCHAR(50);
    DECLARE v_saldo DECIMAL(15,2);
    DECLARE v_interes DECIMAL(5,2);
    DECLARE v_interes_calculado DECIMAL(15,2);

    DECLARE cur_cuentas CURSOR FOR
        SELECT numero_cuenta, saldo, porcentaje_interes
        FROM Cuenta
        WHERE tipo IN ('AHORRO', 'DEBITO')
        AND activa = TRUE
        AND porcentaje_interes > 0;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    START TRANSACTION;

    OPEN cur_cuentas;

    read_loop: LOOP
        FETCH cur_cuentas INTO v_numero_cuenta, v_saldo, v_interes;
        IF done THEN
            LEAVE read_loop;
        END IF;

        -- Calcular intereses
        SET v_interes_calculado = v_saldo * (v_interes / 100);

        -- Actualizar saldo
        UPDATE Cuenta
        SET saldo = saldo + v_interes_calculado,
            fecha_ultimo_movimiento = CURDATE()
        WHERE numero_cuenta = v_numero_cuenta;

        -- Registrar transacción de interés
        INSERT INTO Transaccion (tipo, monto, numero_cuenta_origen, descripcion)
        VALUES ('INTERES', v_interes_calculado, v_numero_cuenta, 'Aplicación de interés mensual');

    END LOOP;

    CLOSE cur_cuentas;

    COMMIT;
    SELECT 'Intereses aplicados exitosamente' AS mensaje;
END$$
DELIMITER ;

-- =============================================
-- TRIGGERS
-- =============================================

-- Trigger para actualizar fecha de último movimiento automáticamente
DELIMITER $$
CREATE TRIGGER trg_actualizar_saldo_cuenta
AFTER INSERT ON Transaccion
FOR EACH ROW
BEGIN
    UPDATE Cuenta
    SET fecha_ultimo_movimiento = CURDATE()
    WHERE numero_cuenta = NEW.numero_cuenta_origen
       OR numero_cuenta = NEW.numero_cuenta_destino;
END$$
DELIMITER ;

-- Trigger para validar que un cliente tenga máximo 5 cuentas activas
DELIMITER $$
CREATE TRIGGER trg_validar_max_cuentas
BEFORE INSERT ON Cuenta
FOR EACH ROW
BEGIN
    DECLARE v_cuentas_activas INT;

    SELECT COUNT(*) INTO v_cuentas_activas
    FROM Cuenta
    WHERE cedula_cliente = NEW.cedula_cliente
      AND activa = TRUE;

    IF v_cuentas_activas >= 5 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Un cliente no puede tener más de 5 cuentas activas';
    END IF;
END$$
DELIMITER ;

-- =============================================
-- FUNCIONES
-- =============================================

-- Función para calcular saldo total de un cliente
DELIMITER $$
CREATE FUNCTION fn_calcular_saldo_cliente(p_cedula VARCHAR(20))
RETURNS DECIMAL(15,2)
DETERMINISTIC
BEGIN
    DECLARE v_saldo_total DECIMAL(15,2);

    SELECT COALESCE(SUM(saldo), 0) INTO v_saldo_total
    FROM Cuenta
    WHERE cedula_cliente = p_cedula
      AND activa = TRUE;

    RETURN v_saldo_total;
END$$
DELIMITER ;

-- Función para contar transacciones de una cuenta en un periodo
DELIMITER $$
CREATE FUNCTION fn_contar_transacciones_periodo(
    p_numero_cuenta VARCHAR(50),
    p_fecha_inicio DATE,
    p_fecha_fin DATE
)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE v_total_transacciones INT;

    SELECT COUNT(*) INTO v_total_transacciones
    FROM Transaccion
    WHERE (numero_cuenta_origen = p_numero_cuenta OR numero_cuenta_destino = p_numero_cuenta)
      AND fecha BETWEEN p_fecha_inicio AND p_fecha_fin;

    RETURN v_total_transacciones;
END$$
DELIMITER ;

-- =============================================
-- CONSULTAS DE PRUEBA
-- =============================================

-- Consulta 1: Clientes con mayor saldo
SELECT
    ROW_NUMBER() OVER (ORDER BY saldo_total DESC) AS ranking,
    nombre_completo,
    cedula,
    saldo_total
FROM vista_clientes_consolidados
ORDER BY saldo_total DESC
LIMIT 5;

-- Consulta 2: Transacciones del último mes
SELECT
    tipo,
    COUNT(*) AS total_transacciones,
    SUM(monto) AS monto_total
FROM Transaccion
WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
GROUP BY tipo
ORDER BY total_transacciones DESC;

-- Consulta 3: Cuentas inactivas (sin movimientos en más de 6 meses)
SELECT
    c.numero_cuenta,
    c.tipo,
    c.saldo,
    u.nombre_completo,
    DATEDIFF(CURDATE(), COALESCE(c.fecha_ultimo_movimiento, c.fecha_apertura)) AS dias_inactivo
FROM Cuenta c
JOIN Cliente cl ON c.cedula_cliente = cl.cedula
JOIN Usuario u ON cl.cedula = u.cedula
WHERE c.activa = TRUE
  AND (c.fecha_ultimo_movimiento IS NULL
       OR DATEDIFF(CURDATE(), c.fecha_ultimo_movimiento) > 180)
ORDER BY dias_inactivo DESC;

-- =============================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- =============================================

-- Índices para búsquedas frecuentes
CREATE INDEX idx_transaccion_fecha_tipo ON Transaccion(fecha, tipo);
CREATE INDEX idx_cuenta_saldo_tipo ON Cuenta(saldo, tipo);
CREATE INDEX idx_cliente_nombre ON Usuario(nombre_completo);
CREATE INDEX idx_transaccion_cuenta_fecha ON Transaccion(numero_cuenta_origen, fecha);

-- =============================================
-- USUARIO DE APLICACIÓN (para conexión Java)
-- =============================================

-- Crear usuario para la aplicación (ajusta la contraseña según necesites)
CREATE USER 'app_bancaria'@'localhost' IDENTIFIED BY 'AppBancaria123';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON sistema_bancario.* TO 'app_bancaria'@'localhost';
FLUSH PRIVILEGES;

-- =============================================
-- MENSAJE DE ÉXITO
-- =============================================
SELECT 'Base de datos "sistema_bancario" creada exitosamente' AS mensaje;
SELECT 'Datos de prueba insertados' AS mensaje;
SELECT 'Vistas, procedimientos y funciones creadas' AS mensaje;