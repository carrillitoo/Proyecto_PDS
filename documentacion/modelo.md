### Modelo
El sistema está diseñado utilizando principios de Diseño Orientado al Dominio (DDD - Domain Driven Design), separando claramente las responsabilidades y modelando el negocio alrededor de entidades ricas.
*   **Bounded Context Principal (Gestión Kanban):** Agrupa todo lo referente al flujo de trabajo. El *Aggregate Root* principal es el **Tablero**, que contiene **ListaTareas**. A su vez, las listas contienen **Tarjetas**, que pueden ser de diferentes tipos (**TarjetaTarea** o **TarjetaChecklist** con **Checklists**). Se les puede asociar **Etiquetas**.
*   **Bounded Context de Identidad y Acceso:** Gestiona el **Usuario**, los **Roles** y los **Permisos**.
*   **Bounded Context de Colaboración:** Gestiona las **Invitaciones** y la **TrazaAccion** (historial de eventos para auditoría/actividad).
**Diagrama de Dominio (Mermaid):**
```mermaid
classDiagram
    class Usuario {
        <<Entity>>
        +String email
        +String password
        +String nombre
        +verificarCuenta()
    }
    class Tablero {
        <<Entity>>
        +String titulo
        +EstadoTablero estado
        +crearLista()
        +invitarUsuario()
    }
    class ListaTareas {
        <<Entity>>
        +String nombre
        +int posicion
        +añadirTarjeta()
    }
    class Tarjeta {
        <<Entity, Abstract>>
        +String titulo
        +String descripcion
        +int posicion
    }
    class TarjetaTarea {
        <<Entity>>
        +Tarea tareaAsociada
    }
    class TarjetaChecklist {
        <<Entity>>
        +List~Checklist~ checklists
    }
    class Checklist {
        <<Entity>>
        +String titulo
        +boolean completado
    }
    class Etiqueta {
        <<Value Object>>
        +String nombre
        +Color color
    }
    class Invitacion {
        <<Entity>>
        +Usuario remitente
        +Usuario destinatario
        +Tablero tablero
        +Rol rolAsignado
    }
    class TrazaAccion {
        <<Value Object>>
        +TipoAccion accion
        +Date fecha
        +Usuario autor
    }
    Usuario "1" --> "*" Tablero : Propietario/Miembro
    Tablero "1" *-- "*" ListaTareas : Contiene
    Tablero "1" *-- "*" Etiqueta : Define
    ListaTareas "1" *-- "*" Tarjeta : Contiene
    Tarjeta <|-- TarjetaTarea
    Tarjeta <|-- TarjetaChecklist
    TarjetaChecklist "1" *-- "*" Checklist : Contiene
    Tarjeta "*" --> "*" Etiqueta : Asignada
    Tablero "1" *-- "*" Invitacion : Gestiona
    Tablero "1" *-- "*" TrazaAccion : Registra
```
## Documentacion

