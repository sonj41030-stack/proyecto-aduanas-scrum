-- Script de creación de base de datos y tablas para ms-reportes
-- Ejecutar en MySQL

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS ms_reportes_db;
USE ms_reportes_db;

-- Crear tabla de reportes
CREATE TABLE reportes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_reporte VARCHAR(50) NOT NULL UNIQUE,
    placa_vehiculo VARCHAR(20) NOT NULL,
    tipo_vehiculo VARCHAR(100) NOT NULL,
    descripcion_egreso VARCHAR(255) NOT NULL,
    monto_egreso DECIMAL(12, 2) NOT NULL,
    prioridad ENUM('BAJA', 'MEDIA', 'ALTA', 'CRITICA') NOT NULL,
    estado ENUM('PENDIENTE', 'EN_REVISION', 'APROBADO', 'RECHAZADO', 'CERRADO') NOT NULL DEFAULT 'PENDIENTE',
    responsable VARCHAR(150) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    fecha_modificacion DATETIME,
    fecha_cierre DATETIME,
    observaciones VARCHAR(500),
    completado BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Índices para optimizar consultas
    INDEX idx_numero_reporte (numero_reporte),
    INDEX idx_placa_vehiculo (placa_vehiculo),
    INDEX idx_estado (estado),
    INDEX idx_prioridad (prioridad),
    INDEX idx_responsable (responsable),
    INDEX idx_fecha_creacion (fecha_creacion),
    INDEX idx_estado_prioridad (estado, prioridad)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear vista para reportes pendientes
CREATE VIEW reportes_pendientes AS
SELECT * FROM reportes WHERE estado = 'PENDIENTE';

-- Crear vista para reportes por prioridad
CREATE VIEW reportes_por_prioridad AS
SELECT 
    prioridad,
    COUNT(*) as cantidad,
    SUM(monto_egreso) as monto_total
FROM reportes
GROUP BY prioridad;

-- Crear vista para reportes completados
CREATE VIEW reportes_completados AS
SELECT * FROM reportes WHERE completado = TRUE;

-- Insertar datos de ejemplo
INSERT INTO reportes (
    numero_reporte, 
    placa_vehiculo, 
    tipo_vehiculo, 
    descripcion_egreso, 
    monto_egreso, 
    prioridad, 
    responsable, 
    fecha_creacion, 
    observaciones
) VALUES
(
    'REP-2024001',
    'ABC-1234',
    'Camión',
    'Reparación de freno delantero',
    150000.00,
    'MEDIA',
    'Juan Pérez',
    NOW(),
    'Reparación completada en taller autorizado'
),
(
    'REP-2024002',
    'XYZ-5678',
    'Automóvil',
    'Cambio de aceite y filtro',
    45000.00,
    'BAJA',
    'María García',
    NOW(),
    'Mantenimiento preventivo'
),
(
    'REP-2024003',
    'ABC-1234',
    'Camión',
    'Reemplazo de llantas',
    850000.00,
    'ALTA',
    'Carlos López',
    NOW(),
    'Llantas desgastadas por uso intensivo'
);
