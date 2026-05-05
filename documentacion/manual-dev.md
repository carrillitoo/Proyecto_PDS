### Dev (Información para Desarrolladores)
El desarrollo está estructurado en dos repositorios/módulos Maven separados:
*   **ProyectoPDS (Backend):**
    *   **Framework:** Spring Boot 3.2.5.
    *   **Lenguaje:** Java 21.
    *   **Arquitectura:** Hexagonal / Clean Architecture. Las entidades de dominio se encuentran aisladas (paquete `umu.pds.api.domain`), y los adaptadores JPA y controladores REST actúan como puertos de entrada y salida (`umu.pds.api.adapters`).
    *   **Base de datos:** H2 Database (configuración en entorno de desarrollo), usando Spring Data JPA para la persistencia.
    *   **Testing:** JUnit 5, Mockito, y ArchUnit (para validación de arquitectura).
    *   **Otros:** Integración de correo electrónico (`spring-boot-starter-mail`) para envío de invitaciones/verificaciones.
*   **ProyectoPDS-GUI (Frontend):**
    *   **Framework:** JavaFX 21.
    *   **Lenguaje:** Java 21.
    *   **Patrón de Diseño UI:** MVC (Model-View-Controller). Los archivos FXML definen las vistas, y las clases en `umu.pds.gui.controllers` manejan la lógica de interfaz.
    *   **Comunicación:** Utiliza la librería Jackson (`jackson-databind`) para serializar y deserializar los payloads JSON que se comunican con la API REST del backend.
Para colaborar, asegúrese de mantener la cobertura de tests en el backend y de no romper las reglas de dependencia definidas por ArchUnit en el núcleo del dominio.
