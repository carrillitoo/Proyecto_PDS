### Manual (Manual de Usuario de la Aplicación)
El proyecto es una aplicación de gestión de proyectos al estilo Kanban compuesta por un servidor backend (Spring Boot) y un cliente de escritorio (JavaFX).
Para utilizar la aplicación:
1.  **Ejecutar Backend:** Inicie el proyecto `ProyectoPDS` usando Maven. Esto arrancará el servidor en el puerto por defecto (usualmente 8080) junto con la base de datos H2 en memoria/fichero.
2.  **Ejecutar Frontend:** Inicie la aplicación gráfica `ProyectoPDS-GUI` (`mvn javafx:run`).
3.  **Primeros Pasos:**
    *   Regístrese mediante la pantalla de Login (haga clic en crear cuenta si está disponible).
    *   Cree su primer **Tablero** desde el Dashboard.
    *   Añada **Listas** (ej. "Por hacer", "En progreso", "Hecho").
    *   Cree **Tarjetas** dentro de las listas para representar tareas.
    *   Haga clic en las tarjetas para editarlas, añadir checklists o asignar etiquetas de colores.
    *   Invite a compañeros usando sus correos electrónicos para colaborar en tiempo real.

