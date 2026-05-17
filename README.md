# Nexus Health — DSY1103 Desarrollo FullStack 1

## 📝 Descripción del Ecosistema
Nexus Health es un ecosistema de arquitectura distribuida compuesto por microservicios desacoplados, diseñado específicamente para mitigar el ausentismo de pacientes en centros médicos de tamaño pequeño y mediano. El sistema opera en segundo plano de manera asíncrona leyendo las agendas y estados de órdenes clínicas. Coordina de forma automatizada el despacho de alertas y recordatorios directamente a través de la API de WhatsApp Business.

### ⚖️ Cumplimiento Normativo y Privacidad (Ley 20.584)
Para dar estricto cumplimiento a la legislación chilena vigente sobre los derechos y deberes de los pacientes (Confidencialidad de la Ficha Clínica), el diseño de mensajería del ecosistema aplica un aislamiento estricto de la información sensible. Las tramas de datos (`NotificacionDTO`) y las plantillas de despacho omiten explícitamente diagnósticos médicos, nombres de exámenes específicos o datos clínicos críticos, garantizando que solo viajen textos genéricos de confirmación logística.

---

## 👥 Equipo de Desarrollo
| Nombre | GitHub |
|--------|--------|
| Alan Vidal | @MechaTeemo1412 |
| David Soto | @D1smos |

---

## 🏗️ Arquitectura y Estructura del Código (Patrón CSR)
Cada uno de los microservicios sigue estrictamente el patrón de diseño **Controller-Service-Repository (CSR)** con un flujo de información unidireccional para garantizar una separación real de responsabilidades en paquetes independientes:

* **Capa de Orquestación (Controller):** Actúa como la interfaz pública del servicio. Su única responsabilidad es exponer endpoints REST, validar la estructura inicial de las peticiones HTTP y delegar la ejecución a la lógica de negocio mediante objetos `ResponseEntity` (JSON).
* **Capa de Transferencia y Validación (DTO):** Objetos planos (POJOs / Java Records) aislados que transportan los datos de entrada sin exponer las entidades de la base de datos. Aplican de forma estricta las reglas de validación de datos a través de anotaciones de **Bean Validation (JSR 380)** (ej. `@NotNull`, `@Size`), rechazando peticiones incompletas a nivel de API.
* **Capa de Lógica de Negocio (Service):** Es el motor inteligente del microservicio. Procesa reglas funcionales específicas, maneja la inyección de logs con `@Slf4j` y coordina las excepciones del dominio.
* **Capa de Acceso a Datos (Repository y Entity):** Las entidades mapean fielmente las tablas de la base de datos mediante JPA/Hibernate. Los repositorios extienden de `JpaRepository` para ejecutar consultas e inserciones SQL automatizadas.
* **Escudo Centralizado de Errores (`@ControllerAdvice`):** La clase `GlobalExceptionHandler` captura de manera global cualquier error lanzado por la capa Service. Evita trazas de error descontroladas en el servidor transformándolas en respuestas estandarizadas con códigos HTTP adecuados (como `404 Not Found` o `400 Bad Request`).

---

## 🧩 Microservicios Implementados
El ecosistema ha sido estabilizado en un conjunto de 6 microservicios independientes que cooperan a través de la red local:

| # | Microservicio | Puerto | Descripción Funcional y Reglas de Negocio |
|---|---------------|--------|-------------------------------------------|
| 1 | `ms-pacientes` | `8081` | Gestión y extracción segura de datos demográficos clínicos. Valida que el teléfono de contacto sea obligatorio y cumpla con formatos de mensajería válidos. |
| 2 | `ms-agenda` | `8082` | Gestión de citas médicas. Utiliza una consulta optimizada para buscar citas en una ventana exacta de 24 horas y restringe las solicitudes para que solo permitan cambiar estado (Asistirá / No Asistirá) sin alterar la fecha original. |
| 3 | `ms-examenes` | `8083` | Mapeo y lógica transaccional para detectar de forma automatizada transiciones críticas de estado en órdenes de exámenes clínicos (de `PENDIENTE` a `LISTO`), disparando alertas inmediatas al ecosistema. |
| 4 | `ms-notificaciones` | `8084` | Capa central de comunicaciones encargada de interactuar con la API oficial de WhatsApp Business y gestionar las plantillas homologadas. Mantiene una base de datos propia para persistir de forma real el histórico de envíos y estados. |
| 5 | `ms-auditoria` | `8085` | Receptor centralizador de logs transaccionales (`Log`) de todo el ecosistema. Mediante tareas programadas (`@Scheduled`), compila de forma automatizada un reporte diario consolidado en formato CSV para la administración a las 18:00 hrs. |
| 6 | `ms-orquestador` | `8086` | El cerebro o director del flujo. Integra tareas programadas automáticas para evaluar alertas y expone adicionalmente un endpoint REST estratégico de ejecución manual para demostraciones en vivo. |

---

## 🛠️ Tecnologías y Dependencias Core
- **Lenguaje principal:** Java 21
- **Framework:** Spring Boot 4.0.6 (Spring Web, Spring Data JPA)
- **ORM:** Hibernate 7+
- **Gestor de Dependencias:** Maven
- **Interoperabilidad remota:** OpenFeign (Spring Cloud Cloud Routing Starter)
- **Trazabilidad:** SLF4J con Lombok (`@Slf4j`)
- **Conectores Cloud:** Oracle JDBC Driver

---

## Cómo Ejecutar el Proyecto
1. Clonar el repositorio: `git clone https://github.com/MechaTeemo1412/DSY1103-NexusHealth-Script.git`
2. Configurar credenciales de Base de Datos en los archivos `application.properties` de cada microservicio.
3. Ejecutar cada microservicio usando: `./mvnw spring-boot:run`

---

## Estado del Proyecto
🔄 En desarrollo — EP2 2026