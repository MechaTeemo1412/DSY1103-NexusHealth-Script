# Nexus Health — DSY1103 Desarrollo FullStack 1

## Descripción
Nexus Health es un ecosistema de microservicios diseñado para reducir el ausentismo en centros médicos de tamaño pequeño/mediano. Opera en segundo plano como un motor asíncrono que lee agendas y estado de exámenes, enviando notificaciones y procesando confirmaciones directamente vía API de WhatsApp, respetando la normativa de privacidad de datos y contando con un sistema de Fallback automatizado.

## Equipo
| Nombre | GitHub |
|--------|--------|
| Alan Vidal | @MechaTeemo1412 |
| David Soto | @D1smos |

## Microservicios Implementados
| # | Microservicio | Puerto | Descripción |
|---|---------------|--------|-------------|
| 1 | `ms-pacientes`    | 8081   | Gestión y validación de la información de contacto. |
| 2 | `ms-agenda`       | 8082   | Gestión de citas médicas y estado de exámenes. |
| 3 | `ms-orquestador`  | 8083   | Motor temporizado que evalúa y dispara alertas. |
| 4 | `ms-comunicaciones`| 8084  | Cliente API para WhatsApp, Fallback SMS y Webhooks. |
| 5 | `ms-auditoria`    | 8085   | Trazabilidad de envíos y generación de reportes. |

## Tecnologías Utilizadas
- Maven
- Java 17 / Spring Boot 4.0.6
- Spring Web
- Spring Data JPA + Hibernate
- Oracle Driver
- Spring Reactive Web
- Lombok para reducción de código repetitivo

## Cómo Ejecutar el Proyecto
1. Clonar el repositorio: `git clone https://github.com/MechaTeemo1412/DSY1103-NexusHealth-Script.git`
2. Configurar credenciales de Base de Datos en los archivos `application.properties` de cada microservicio.
3. Ejecutar cada microservicio usando: `./mvnw spring-boot:run`

## Estado del Proyecto
🔄 En desarrollo — Semestre 1 Fullstack 2026