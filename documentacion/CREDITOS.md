# Créditos y Participación del Equipo

A continuación se exponen las áreas de responsabilidad de cada miembro.

---

## Jaime Cegarra Martínez (seega-s)
**Rol principal:** Integración total del dominio de tablero y lista de tarjetas, desde el modelo hasta la persistencia  

**Commits destacados:**
- `dda4c10` - *Base de jerarquía de directorios y proyecto*: Configuración inicial del esqueleto del proyecto.
- `69ccfb2` a `16bffde` - *Tablero declarado*, *logica de transiciones obligatorias*: Implementación central de la lógica de negocio y reglas del `Tablero`.
- `36407ed` - *testing de tablero y lista de tareas completo;;; cobertura total 88%*: Gran contribución al aseguramiento de la calidad en la capa de dominio.
- `2496063` y `6cbfbd2` - *TableroController REST*, *ListaTareasController REST + GlobalHandler*: Exposición del dominio a través de la API REST y manejo de excepciones.
- `d7e6779` - *repository adapter + debug postman + todo funcionando bien :D*: Integración exitosa de los adaptadores de repositorios.
- `4327f77` y `d0f4ac0` - *Interfaz avanzada para conectar con DTOs*, *Todos los detalles de interfaz solucionados*: Trabajo en la conexión fluida entre el frontend JavaFX y el backend REST.


## Victor Carrillo Gil (carrillito)
**Rol principal:** Infraestructura, CI/CD, Modelado de Dominio (Tarjetas y Etiquetas) y Adaptadores.

Victor lideró la integración continua, configurando flujos automatizados con SonarQube y GitHub Actions. En el código, su participación se centró fuertemente en el modelado de elementos base del dominio (como las tarjetas y las etiquetas) y en consolidar la capa de adaptadores para separar correctamente la infraestructura.

**Evidencias destacadas (Commits):**
- `0f36415` y `20dc280` - *Añadida configuración de SonarQube en pom.xml y workflow*: Configuración de herramientas de análisis de código estático y CI/CD.
- `07291cb` - *feat: estructurar modelo base de dominio para Tarjetas y Etiquetas*: Diseño e implementación de las entidades de dominio secundarias.
- `bbccb2f` y `742410f` - *Capa de Adaptadores/Infraestructura terminada*, *Nuevas clases para la capa de Adaptadores*: Creación de los adaptadores secundarios y aislamiento de la infraestructura.
- `f84769e` - *Tests primeras clases dominio*: Inicio y estructuración del testing en las clases bases del modelo.
- Múltiples merges (`051dd1e`, `53514e3`, etc.) y pruebas (`d9c9992` - *Prueba Postman realizada existosamente*): Gestión de ramas y validación de integraciones.

---

## Javier Peñalver Gómez
**Rol principal:** Dominio de Identidad, Frontend (GUI), Integración de Servicios (Email) y Documentación.

Javier se enfocó intensamente en el módulo frontend (`ProyectoPDS-GUI`), desarrollando las vistas iniciales, controladores de interfaz y estilos. En el backend, diseñó el modelo de dominio relacionado con los Usuarios y Roles, así como la implementación de adaptadores de salida específicos como el envío de emails. Finalmente, realizó grandes aportes a la refactorización y documentación del proyecto.

**Evidencias destacadas (Commits):**
- `d64f27c` - *dominio usuarios, usuario, email y rol*: Modelado del Bounded Context de Identidad y Acceso.
- `9ce315d` a `e82f036` - *primeras implementaciones front*, *front*, *interfaz*: Construcción inicial de la interfaz gráfica en JavaFX.
- `a489f4b` y `82396a3` - *UsuarioController*, *ConsoleEmailAdapter*: Exposición de la API de usuarios y creación del adaptador para la simulación/envío de correos.
- `4f3216f` y `9b41969` - *quitar mocks, mover dtos y otros cambios*, *pulir detalles interfaz*: Refactorización profunda, limpieza de datos simulados y organización de los DTOs en el frontend.
- `81f6191` y `a12ef37` - *documentación*: Contribuciones finales a los manuales y ficheros markdown del proyecto.
