CREATE TABLE vehiculo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patente VARCHAR(10) NOT NULL UNIQUE,
    marca VARCHAR(60) NOT NULL,
    modelo VARCHAR(60) NOT NULL,
    anio INT NOT NULL,
    color VARCHAR(30) NOT NULL,
    tipo_vehiculo VARCHAR(30) NOT NULL,
    propietario_rut VARCHAR(15) NOT NULL,
    propietario_nombre VARCHAR(120) NOT NULL,
    fecha_registro DATETIME NOT NULL
);

CREATE TABLE salida_temporal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id BIGINT NOT NULL,
    fecha_salida DATETIME NOT NULL,
    fecha_retorno_estimada DATETIME NOT NULL,
    fecha_retorno_real DATETIME NULL,
    motivo_viaje VARCHAR(200) NOT NULL,
    pais_destino VARCHAR(60) NOT NULL,
    conductor_rut VARCHAR(15) NOT NULL,
    conductor_nombre VARCHAR(120) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    observaciones VARCHAR(300) NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_salida_vehiculo FOREIGN KEY (vehiculo_id) REFERENCES vehiculo(id)
);

CREATE TABLE viajero (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    salida_temporal_id BIGINT NOT NULL,
    nombre_completo VARCHAR(120) NOT NULL,
    rut VARCHAR(15) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    tipo_viajero VARCHAR(20) NOT NULL,
    es_menor_edad BOOLEAN NOT NULL,
    es_propietario BOOLEAN NOT NULL DEFAULT FALSE,

    -- Datos de autorización notarial: solo se completan cuando es_menor_edad = TRUE
    nombre_autorizante VARCHAR(120) NULL,
    rut_autorizante VARCHAR(15) NULL,
    parentesco_autorizante VARCHAR(60) NULL,
    numero_escritura_notarial VARCHAR(60) NULL,
    notaria_nombre VARCHAR(120) NULL,
    ciudad_notaria VARCHAR(80) NULL,
    fecha_autorizacion DATE NULL,
    url_documento_autorizacion VARCHAR(300) NULL,
    validada BOOLEAN NULL,

    created_at DATETIME NOT NULL,
    CONSTRAINT fk_viajero_salida FOREIGN KEY (salida_temporal_id) REFERENCES salida_temporal(id)
);
