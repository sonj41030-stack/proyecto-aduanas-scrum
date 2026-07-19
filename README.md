# Sistema de Gestión Aduanera — proyecto-aduanas-scrum

Sistema de gestión aduanera desarrollado como proyecto de **Ingeniería de
Software, DuocUC**. Implementa una arquitectura de microservicios en Spring
Boot con un frontend HTML/JS que consume los servicios a través de un API
Gateway.

## Arquitectura

```
                    ┌─────────────────┐
                    │   AduanaUI       │  (HTML + JS, sin framework)
                    │  localhost:5500  │
                    └────────┬─────────┘
                             │ fetch()
                             ▼
                    ┌─────────────────┐
                    │   api-gateway    │  puerto 8090
                    │  (api-wateway)   │  enruta + CORS
                    └────────┬─────────┘
      ┌───────────┬──────────┼──────────┬───────────┐
      ▼           ▼          ▼          ▼           ▼
┌──────────┐┌───────────┐┌───────────┐┌──────────┐┌──────────┐
│ ms-auth  ││ms-tramites││ms-reportes││ ms-sag   ││ ms-pdi   │
│puerto 8081││puerto 8082││puerto 8083││puerto8084││puerto8085│
└──────────┘└───────────┘└───────────┘└──────────┘└──────────┘
      │           │          │           │           │
      └───────────┴──────────┴───────────┴───────────┘
                             ▼
                    ┌─────────────────┐
                    │      MySQL       │
                    │  localhost:3306  │
                    │ (1 BD por micro) │
                    └─────────────────┘
```

> **Nota:** el puerto del gateway es `8090` (no `8090`). Todo el frontend
> (`vehiculos.html`, `menores.html`, `sag.html`, `estado-pdi.html`,
> `dashboard.html`, `mis-tramites.html`) apunta a
> `http://localhost:8090` a través de la constante `API_GATEWAY`.

## Microservicios

| Servicio | Puerto | Responsabilidad |
|---|---|---|
| `ms-auth` | 8081 | Registro y login de usuarios, emisión de token |
| `ms-tramites` | 8082 | Vehículos, salidas temporales, viajeros/menores |
| `ms-reportes` | 8083 | Reportes (context-path `/api/v1`) |
| `ms-sag` | 8084 | Declaraciones SAG (alimentos y mascotas) |
| `ms-pdi` | 8085 | Consulta y registro de estado PDI |
| `api-gateway` (`api-wateway`) | 8090 | Enrutamiento + CORS hacia el frontend |

### Rutas del Gateway

El `GatewayController` reescribe las rutas antes de reenviarlas:

| Ruta pública (frontend) | Reenvía a |
|---|---|
| `http://localhost:8090/auth/...` | `ms-auth:8081/auth/...` |
| `http://localhost:8090/tramites/...` | `ms-tramites:8082/api/v1/...` |
| `http://localhost:8090/reportes/...` | `ms-reportes:8083/api/v1/reportes/...` |
| `http://localhost:8090/sag/...` | `ms-sag:8084/api/v1/sag/...` |
| `http://localhost:8090/pdi/...` | `ms-pdi:8085/api/v1/pdi/...` |

## Frontend (`AduanaUI`)

HTML + JavaScript plano (sin build step). Páginas principales:

| Página | Estado | Descripción |
|---|---|---|
| `aduana.html` | ✅ Conectada | Login (`POST /auth/login`) |
| `registro.html` | ✅ Conectada | Registro de usuario (`POST /auth/registro`) |
| `vehiculos.html` | ✅ Conectada | Registro de vehículos + salidas temporales |
| `panel.html` | ✅ | Panel principal / navegación |
| `menores.html` | ✅ Conectada | Elige vehículo → salida temporal → registra viajeros (`POST /tramites/viajeros`), exige datos notariales si el viajero es menor de edad |
| `sag.html` | ✅ Conectada | Declaraciones SAG (`POST/GET/DELETE /sag`), pasa a estado OBSERVADA automáticamente si falta certificado sanitario |
| `estado-pdi.html` | ✅ Conectada | Consulta de estado PDI (`POST /pdi/consultas`) + historial |
| `dashboard.html` | ✅ Conectada | Estadísticas en vivo desde `ms-tramites`, `ms-sag` y `ms-pdi` |
| `mis-tramites.html` | ✅ Conectada | Agrega vehículos, menores, declaraciones SAG y consultas PDI reales |

Cada página que consume el backend define al inicio de su `<script>` una
constante `API_GATEWAY` apuntando al gateway (`http://localhost:8090`). Si
cambias el puerto del gateway, hay que actualizar esa constante en **cada**
archivo conectado.

## Requisitos

- **Java 17** (probado con Eclipse Temurin 17.0.19)
- **Apache Maven 3.9+** (o usar los wrappers `mvnw` incluidos en `ms-auth` y `api-gateway`)
- **MySQL** corriendo en `localhost:3306`, usuario `root`
- Un servidor HTTP simple para el frontend (Node.js con `npx serve`, o Python)

## Cómo levantar el proyecto

### 1. Base de datos

Asegúrate de tener MySQL corriendo en `localhost:3306`. Cada microservicio
crea su propia base de datos automáticamente al iniciar
(`db_sag_aduanas`, `db_pdi_aduanas`, etc.).

### 2. Microservicios

Levanta cada uno en su propia terminal:

```bash
# ms-auth (tiene Maven Wrapper)
cd ms-auth/ms-auth
./mvnw spring-boot:run          # puerto 8081

# ms-tramites (usa Maven global, no trae wrapper)
cd ms-tramites
mvn spring-boot:run             # puerto 8082

# ms-reportes (usa Maven global, no trae wrapper)
cd ms-reportes
mvn spring-boot:run             # puerto 8083

# ms-sag (usa Maven global, no trae wrapper)
cd ms-sag
mvn spring-boot:run             # puerto 8084

# ms-pdi (usa Maven global, no trae wrapper)
cd ms-pdi
mvn spring-boot:run             # puerto 8085

# api-gateway (tiene Maven Wrapper) — levantar al final
cd api-gateway/api-wateway
./mvnw spring-boot:run          # puerto 8090
```

En **Windows / PowerShell**, reemplaza `./mvnw` por `.\mvnw.cmd`.

Espera a ver `Started ... Application` en cada consola antes de continuar
con la siguiente.

### 3. Frontend

Sirve la carpeta `AduanaUI` con un servidor local — **nunca la abras con
doble clic / `file://`**, porque el navegador manda `Origin: null` y el
CORS del gateway la rechaza.

```bash
cd AduanaUI
npx serve .
# o, si no tienes Node.js:
python3 -m http.server 5500
```

Luego abre `http://localhost:5500/aduana.html`.

### 4. Flujo de prueba sugerido

1. `registro.html` → crear un usuario.
2. `aduana.html` → iniciar sesión con ese usuario.
3. `vehiculos.html` → registrar un vehículo y verificar que se cree la
   salida temporal asociada.
4. `menores.html` → elegir ese vehículo, elegir la salida temporal creada,
   registrar un viajero con fecha de nacimiento menor a 18 años y completar
   los datos notariales; luego usar el botón **Validar** para simular la
   revisión del funcionario de aduana.
5. `sag.html` → registrar una declaración marcando "Sí" en alimentos o
   mascotas y "No" en certificado sanitario, para ver cómo queda en estado
   **OBSERVADA**.
6. `estado-pdi.html` → consultar con documento `11111111-1` (queda con
   observaciones) o `22222222-2` (queda bloqueado); cualquier otro
   documento queda **HABILITADO** por defecto (`ms-pdi` trae estos dos
   registros de ejemplo precargados).
7. `dashboard.html` y `mis-tramites.html` → verificar que reflejen los
   datos reales recién creados.

Usa la pestaña **Network** de las DevTools del navegador para depurar
cualquier error de conexión (404, CORS, 500, etc.).

## Estructura del repositorio

```
proyecto-aduanas-scrum-main/
├── AduanaUI/                  # Frontend HTML/JS
├── ms-auth/ms-auth/           # Microservicio de autenticación (con mvnw)
├── ms-tramites/               # Microservicio de trámites (sin mvnw)
├── ms-reportes/                # Microservicio de reportes (sin mvnw)
├── ms-sag/                     # Microservicio de declaraciones SAG (sin mvnw)
├── ms-pdi/                     # Microservicio de consultas PDI (sin mvnw)
├── api-gateway/api-wateway/   # API Gateway (con mvnw)
└── README.md
```

## Notas de implementación de `ms-sag` y `ms-pdi`

Ambos microservicios se construyeron replicando exactamente el patrón ya
usado en `ms-reportes` (entity / dto / repository / service / controller /
`GlobalExceptionHandler`, Spring Data JPA con `ddl-auto=update`, sin Flyway).

- **`ms-sag`**: la regla de negocio (declarar alimentos o mascotas sin
  certificado sanitario deja la declaración en estado `OBSERVADA`) vive en
  `DeclaracionSagServiceImpl`, no en el frontend — el frontend solo
  refleja el estado que devuelve el backend.
- **`ms-pdi`**: separa un registro base `PersonaPdi` (simula el archivo
  que consultaría la PDI) de un historial `ConsultaPdi` (cada consulta que
  hace un usuario desde `estado-pdi.html` queda guardada). Si el documento
  consultado no está en `PersonaPdi`, se asume `HABILITADO`. Un
  `DataLoader` precarga dos personas de ejemplo para poder demostrar los
  tres estados sin depender de datos reales.

## Problemas conocidos / trabajo pendiente

- `ms-tramites` y `ms-reportes` no incluyen Maven Wrapper (tampoco
  `ms-sag` ni `ms-pdi`); requieren Maven instalado globalmente en la
  máquina donde se ejecuten.
- `ms-sag` y `ms-pdi` no tienen aún Swagger/OpenAPI ni pruebas unitarias
  (JUnit/Mockito), a diferencia de `ms-tramites`. Si se quiere mantener
  consistencia con el resto del proyecto, es el siguiente paso natural.
- No se agregó autenticación/token a las rutas nuevas (`/sag`, `/pdi`);
  hoy son públicas igual que `/tramites`, consistente con el resto del
  gateway.

## Equipo

Proyecto desarrollado para el ramo de Ingeniería de Software, DuocUC.
