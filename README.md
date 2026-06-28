# Nexus Health — DSY1103 Desarrollo FullStack 1

## 📝 Descripción del Ecosistema

Nexus Health es un ecosistema de arquitectura distribuida compuesto por microservicios desacoplados, diseñado específicamente para mitigar el ausentismo de pacientes en centros médicos de tamaño pequeño y mediano. El sistema opera en segundo plano de manera asíncrona leyendo las agendas y estados de órdenes clínicas, coordinando de forma automatizada el despacho de alertas y recordatorios.

### ⚖️ Cumplimiento Normativo y Privacidad (Ley 20.584)

Para dar estricto cumplimiento a la legislación chilena vigente sobre los derechos y deberes de los pacientes, el diseño de mensajería aplica un aislamiento estricto de la información sensible. Las tramas de datos (`NotificacionDTO`) y las plantillas de despacho omiten explícitamente diagnósticos médicos o datos clínicos críticos.

---

## 👥 Equipo de Desarrollo

| Nombre | GitHub |
| --- | --- |
| Alan Vidal | @MechaTeemo1412 |
| David Soto | @D1smos |

---

## 🏗️ Arquitectura y Topología de Red (Docker & Gateway)

El ecosistema ha evolucionado a una topología de red aislada y segura, orquestada mediante **Docker Compose**. Los servicios ya no dependen de IPs locales (`localhost`), sino que se comunican mediante resolución **DNS interna de Docker** dentro de la red virtual `nexus-network`.

### 🛡️ API Gateway (Punto de Entrada Único)

Todo el tráfico externo es interceptado y enrutado por `ms-gateway`. Los microservicios hijos no exponen sus puertos directamente al exterior, garantizando seguridad y control de acceso centralizado.

| N° | Microservicio | Puerto Interno | Rol Arquitectónico |
| --- | --- | --- | --- |
| 1 | `ms-gateway` | `8080` | Proxy reverso y enrutador principal (Gateway). Único puerto expuesto al cliente. |
| 2 | `ms-pacientes` | `8081` | Adaptador de lectura. Valida formatos mediante JSR 380. **Desplegado en la nube (Render)**. |
| 3 | `ms-agenda` | `8082` | Motor de búsqueda de citas programadas en ventanas de 24 horas. |
| 4 | `ms-examenes` | `8083` | Gestor de estados de laboratorio mediante actualizaciones parciales (`PATCH`). |
| 5 | `ms-notificaciones` | `8084` | Capa de mensajería (Simulación WhatsApp Business). |
| 6 | `ms-auditoria` | `8085` | Receptor central de trazabilidad (Logs del ecosistema). |
| 7 | `ms-orquestador` | `8086` | Director del flujo (Saga/Orquestación). Integra tareas `@Scheduled` y consumo síncrono vía Feign. |

---

## ☁️ Despliegue en la Nube (CI/CD) y Configuración Dinámica

Para demostrar prácticas de DevOps y Continuous Deployment, el microservicio `ms-pacientes` se encuentra **desplegado operativamente en Render**.

* **Configuración Externalizada (The Twelve-Factor App):** Las URLs de comunicación entre microservicios (Feign Clients) no están *hardcodeadas*. Se inyectan dinámicamente mediante variables de entorno (ej. `AUDITORIA_URL`), lo que permite que las imágenes Docker sean 100% inmutables y agnósticas al entorno (Local vs Cloud).

---

## 🛠️ Tecnologías y Dependencias Core

* **Lenguaje principal:** Java 21
* **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA)
* **Gestor de Dependencias:** Maven
* **Enrutamiento:** Spring Cloud Gateway
* **Interoperabilidad:** OpenFeign (Comunicación síncrona inter-servicios)
* **Documentación API:** SpringDoc OpenAPI 3 (Swagger UI)
* **Manejo de Errores:** `@ControllerAdvice` y Bean Validation (JSR-380)
* **Contenedores:** Docker & Docker Compose
* **Base de Datos:** Oracle Database (Autonomous Database Cloud)

---

## 📖 Documentación Interactiva (Swagger)

El proyecto cuenta con documentación autogenerada mediante OpenAPI 3. Para explorar e interactuar con los endpoints de cualquier microservicio en un entorno local, ingrese a la consola interactiva:
👉 `http://localhost:[PUERTO]/swagger-ui/index.html`

---

## 📋 Prerrequisitos y Ejecución del Entorno Local

1. **Clonar el repositorio:** `git clone https://github.com/MechaTeemo1412/DSY1103-NexusHealth-Script.git`
2. **Preparar Credenciales:** Asegúrese de contar con la Wallet de Oracle configurada y mapeada en sus variables de entorno locales.
3. **Compilar y Empaquetar:**
En la raíz del proyecto o dentro de cada carpeta, compilar los binarios ignorando tests:
`./mvnw clean package -DskipTests`
4. **Levantar Ecosistema Docker:**
`docker-compose up --build`
5. **Pruebas (Postman):** Las peticiones de prueba deben dirigirse al Gateway utilizando la IP de loopback o variables de entorno (ej. `http://127.0.0.1:8080/api/v1/...`).

---