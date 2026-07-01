# Microservicio MS-Reportes

Microservicio de gestión de reportes de egresos de vehículos para el proyecto aduanas-scrum.

## Descripción

MS-Reportes es un microservicio REST desarrollado con Spring Boot 3.x que proporciona funcionalidades CRUD completas para la gestión de reportes de egresos de vehículos. Incluye funcionalidades de búsqueda, filtrado por estado, prioridad, responsable y rangos de fecha.

## Requisitos Previos

- Java 17 o superior
- Maven 3.8.x o superior
- MySQL 5.7 o superior
- Git

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/sonj41030-stack/proyecto-aduanas-scrum.git
cd proyecto-aduanas-scrum
git checkout feature/ms-reportes
```

### 2. Crear la base de datos

```bash
mysql -u root -p < sql/schema.sql
```

O ejecutar manualmente en MySQL:

```sql
CREATE DATABASE IF NOT EXISTS ms_reportes_db;
USE ms_reportes_db;
```

### 3. Configurar la conexión a base de datos

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ms_reportes_db
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

### 4. Compilar y ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

La aplicación se ejecutará en: `http://localhost:8081/api/v1`

## Estructura del Proyecto

```
ms-reportes/
├── src/
│   ├── main/
│   │   ├── java/com/proyecto/aduanas/msreportes/
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # Entidades JPA
│   │   │   ├── repository/      # Repositorios Spring Data
│   │   │   ├── service/         # Lógica de negocio
│   │   │   └── MsReportesApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── sql/
│   └── schema.sql              # Scripts de base de datos
├── pom.xml
└── README.md
```

## API Endpoints

### Crear Reporte
```http
POST /api/v1/reportes
Content-Type: application/json

{
  "placaVehiculo": "ABC-1234",
  "tipoVehiculo": "Camión",
  "descripcionEgreso": "Reparación de motor",
  "montoEgreso": 500000.00,
  "prioridad": "ALTA",
  "responsable": "Juan Pérez",
  "observaciones": "Revisión completa del sistema"
}
```

### Obtener Reporte por ID
```http
GET /api/v1/reportes/{id}
```

### Obtener Reporte por Número
```http
GET /api/v1/reportes/numero/{numeroReporte}
```

### Obtener Todos los Reportes
```http
GET /api/v1/reportes
```

### Obtener Reportes por Placa
```http
GET /api/v1/reportes/placa/{placa}
```

### Obtener Reportes por Estado
```http
GET /api/v1/reportes/estado/{estado}
```

Estados disponibles: PENDIENTE, EN_REVISION, APROBADO, RECHAZADO, CERRADO

### Obtener Reportes por Prioridad
```http
GET /api/v1/reportes/prioridad/{prioridad}
```

Prioridades disponibles: BAJA, MEDIA, ALTA, CRITICA

### Obtener Reportes por Responsable
```http
GET /api/v1/reportes/responsable/{responsable}
```

### Obtener Reportes por Rango de Fecha
```http
GET /api/v1/reportes/rango?inicio=2024-01-01T00:00:00&fin=2024-12-31T23:59:59
```

### Actualizar Reporte
```http
PUT /api/v1/reportes/{id}
Content-Type: application/json

{
  "placaVehiculo": "ABC-1234",
  "tipoVehiculo": "Camión",
  "descripcionEgreso": "Reparación de motor actualizada",
  "montoEgreso": 550000.00,
  "prioridad": "ALTA",
  "responsable": "Juan Pérez",
  "observaciones": "Actualizado el 15/01/2024"
}
```

### Cambiar Estado de Reporte
```http
PATCH /api/v1/reportes/{id}/estado/{estado}
```

### Marcar como Completado
```http
PATCH /api/v1/reportes/{id}/completar
```

### Eliminar Reporte
```http
DELETE /api/v1/reportes/{id}
```

### Contar Reportes por Estado
```http
GET /api/v1/reportes/estadisticas/contar/{estado}
```

## Tecnologías Utilizadas

- **Spring Boot 3.2.0** - Framework web
- **Spring Data JPA** - Acceso a datos
- **MySQL** - Base de datos relacional
- **Lombok** - Reducción de código boilerplate
- **MapStruct** - Mapeo de objetos
- **Jakarta Validation** - Validación de datos
- **Maven** - Gestor de dependencias

## Configuración de Logging

El microservicio utiliza SLF4J con configuración en `application.properties`:

```properties
logging.level.root=INFO
logging.level.com.proyecto.aduanas=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

## Notas Importantes

- Los números de reporte se generan automáticamente con el prefijo "REP-" seguido de UUID
- Las fechas de creación y modificación se gestionan automáticamente
- La base de datos usa character set UTF-8 para soporte de caracteres especiales
- Los índices de base de datos están optimizados para consultas frecuentes

## Desarrollo Futuro

- Integración con Apache Kafka para eventos
- Generación de reportes en Excel/PDF
- Autenticación y autorización con OAuth2/JWT
- Caché con Redis
- Documentación OpenAPI/Swagger
- Tests unitarios y de integración

## Contribuidores

- Sonj41030-Stack (Klare Stella)

## Licencia

Este proyecto es parte del programa académico de Ciberseguridad.
