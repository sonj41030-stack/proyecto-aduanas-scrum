# Conexión del frontend (AduanaUI) con los microservicios

## Qué encontré y qué corregí

El HTML casi no hablaba con los microservicios: `vehiculos.html` y `menores.html`
solo guardaban todo en `localStorage`, y aunque `registro.html` sí tenía un
`fetch()`, había 3 bugs de infraestructura que iban a impedir que cualquier
página funcionara apenas se intentara conectar:

1. **Faltaba CORS en el api-gateway.** Sin esto el navegador bloquea cualquier
   `fetch()` desde el HTML hacia `http://localhost:8090`. Agregué
   `api-gateway/.../CorsConfig.java`.

2. **Choque de puertos.** `ms-reportes` tenía `server.port=8081`, el mismo
   puerto que `ms-auth`. Y `ms-tramites` estaba en `8083`, pero el gateway
   esperaba `tramites` en `8082`. Los dejé así (coinciden con `services.*` de
   `api-gateway/application.yml`):
   - `ms-auth` → 8081
   - `ms-tramites` → 8082
   - `ms-reportes` → 8083
   - `api-gateway` → 8090 (único puerto que debe usar el frontend)

3. **El proxy del gateway no reescribía las rutas.** `GatewayController`
   reenviaba la URL tal cual llegaba. Eso funcionaba de casualidad para
   `/auth/**` (porque `ms-auth` también usa el prefijo `/auth`), pero
   `ms-tramites` expone sus endpoints en `/api/v1/...` y `ms-reportes` en
   `/api/v1/reportes` (tiene `context-path: /api/v1`). Reescribí
   `GatewayController.java` para que:
   - `GET/POST http://localhost:8090/auth/...`        → `ms-auth:8081/auth/...`
   - `GET/POST http://localhost:8090/tramites/...`    → `ms-tramites:8082/api/v1/...`
   - `GET/POST http://localhost:8090/reportes/...`    → `ms-reportes:8083/api/v1/reportes/...`

## Páginas que ya quedaron conectadas de verdad

- **`aduana.html` (login)** → `POST /auth/login`. Guarda el `token`, `username`
  y `rol` reales que devuelve `ms-auth` en `localStorage`.
- **`registro.html`** → ya llamaba a `POST /auth/registro`; con el CORS
  arreglado ahora sí llega al backend.
- **`vehiculos.html`** → Le agregué los campos que el DTO real exige
  (marca, modelo, año, color, RUT/nombre del propietario, motivo de viaje) y
  ahora hace 2 llamadas: `POST /tramites/vehiculos` y luego
  `POST /tramites/salidas-temporales` con el `id` del vehículo creado. La
  tabla ahora se carga con `GET /tramites/vehiculos` +
  `GET /tramites/salidas-temporales/vehiculo/{id}`, en vez de leer
  `localStorage`.

## Páginas que NO alcancé a conectar (y por qué)

- **`menores.html`**: el backend sí tiene soporte para viajeros menores de
  edad (`POST /tramites/viajeros` con `tipoViajero`, datos del autorizante,
  etc.), pero requiere un `salidaTemporalId` que hoy no existe en el flujo de
  esa pantalla. Se puede conectar siguiendo el mismo patrón que usé en
  `vehiculos.html`, una vez que decidan desde qué salida temporal cuelga cada
  menor.
- **`sag.html`, `estado-pdi.html`, `dashboard(1).html`, `mis-tramites.html`**:
  no encontré ningún microservicio en el repo que exponga datos de SAG, PDI
  o un dashboard agregado — probablemente le corresponde a otro compañero de
  equipo construirlo, o falta ese microservicio.

## Cómo levantar todo

1. Tener MySQL corriendo en `localhost:3306` (usuario `root`).
2. Levantar, en este orden, cada microservicio (cada uno crea su propia BD):
   ```
   cd ms-auth/ms-auth      && ./mvnw spring-boot:run   # puerto 8081
   cd ms-tramites          && ./mvnw spring-boot:run   # puerto 8082
   cd ms-reportes          && ./mvnw spring-boot:run   # puerto 8083
   cd api-gateway/api-wateway && ./mvnw spring-boot:run # puerto 8090
   ```
3. Servir el HTML con un servidor local (NO abrirlo con doble clic /
   `file://`, porque con `file://` el navegador manda `Origin: null` y da
   más problemas de los necesarios). Por ejemplo, desde la carpeta `AduanaUI`:
   ```
   npx serve .
   # o
   python3 -m http.server 5500
   ```
   y abrir `http://localhost:5500/aduana.html`.
4. Todas las páginas apuntan a `http://localhost:8090` (el gateway) — si
   alguien corre el gateway en otro puerto, hay que actualizar la constante
   `API_GATEWAY` al inicio del `<script>` de cada página.
