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

---

## Víctor Carrillo Gil (carrillitoo)
**Rol principal:** Infraestructura, CI/CD, modelado de dominio de tarjetas,etiquetas y adaptadores.

Se ha encargado de la integración continua mediante flujos de automatizados con SonarQube y GitHub Actions. En cuanto al código, se encargó del modelado de elementos del dominio, en particular de las tarjetas y las etiquetas, y también de la capa de adaptadores para separar correctamente la infraestructura.

**Commits destacados**
- `0f36415` y `20dc280` - *Añadida configuración de SonarQube en pom.xml y en application properties*: Configuración de las herramientas de análisis de código estático y CI/CD.
- `07291cb` - *estructurar modelo base de dominio para Tarjetas y Etiquetas*: Diseño e implementación de las entidades de dominio de tarjetas y etiquetas.
- `bbccb2f` y `742410f` - *Capa de Adaptadores/Infraestructura terminada*: Creación de los adaptadores de la capa de infraestructura.
- `f84769e` - *Tests primeras clases dominio*: Inicio y estructuración del testing en las clases bases del modelo.
- Múltiples merges (`051dd1e`, `53514e3`, etc.) y pruebas (`d9c9992` - *Prueba Postman realizada existosamente*): Gestión de ramas y validación de integraciones.
- `c4e58a2` - *workflows: añadido pipeline para SonarQube y tests*: Creación y automatización del workflow principal en `.github/workflows/sonar.yml`.
- `8b2a1f0` - *JPA Adapters*: Implementación de `TarjetaPersistenceAdapterImpl` y repositorios JPA para aislar la persistencia de datos.

---

## Javier Peñalver Gómez (javierpenalvergomez) 
**Rol principal:** Dominio de Identidad, Frontend (GUI), Integración de Servicios (Email) y Documentación.

Se ha encargado del módulo frontend, el proyecto ProyectoPDS-GUI, desarrollando las vistas iniciales, controladores de interfaz y estilos. En la parte del backend el diseño de dominio relacionado con los Usuarios y Roles y ademñas la implementación de adaptadores de salida específicos como el envío de emails. Finalmente, realizó grandes aportes a la refactorización y documentación del proyecto.

**Commits destacados**
- `d64f27c` - *dominio usuarios, usuario, email y rol*: creacion del modelo de identidad y acceso.
- `1cdccf2` - *modificacion en la clase usuario*: amplicación en email y usuario y creación del UsuarioRepositoryPort.
- `e0f49ff` - *dtos y usecases*: implementación de los DTOs, casos de uso y puertos de entrada y salida para la lógica de compartir tablero y verificación por email.
- `9ce315d` a `e82f036` - *primeras implementaciones front*, *front*, *interfaz*: Implementaciones iniciales de la interfaz gráfica.
- `a489f4b` y `82396a3` - *UsuarioController*, *ConsoleEmailAdapter*: implementación de la API de usuarios y del adaptador para el envío de correos.
- `4f3216f` y `9b41969` - *quitar mocks, mover dtos y otros cambios*, *pulir detalles interfaz*: Quitar datos simulados, orgnizar los DTOs en el frontend y refactorizaciones.
- `81f6191` y `a12ef37` - *documentación*: Contribuciones finales a los manuales y ficheros markdown del proyecto.

---

## Flujo de Trabajo y Pull Requests

El desarrollo del proyecto se ha gestionado mediante un flujo de trabajo basado en ramas, lo cual permite una clara división de tareas y una integración controlada:

- **Rama Principal (`main`):** Representa la línea base donde se consolida el proyecto.
- **Ramas de Dominio (`rama-sega` y `rama-carry`):** Se trabajaron en paralelo partiendo de `main`, desarrollando las clases y modelos del dominio de forma independiente para evitar conflictos.
- **Integración Base:** Posteriormente, el trabajo de ambas ramas se fusionó de nuevo en la rama principal (`main`), juntando así las funcionalidades base del dominio.
- **Rama de Funcionalidades (`rama-vico`):** Una vez integrados los modelos, la `rama-vico` hizo *pull* del repositorio consolidado (`main`) para trabajar sobre una base que ya tenía las clases resueltas y proceder a rematar las funcionalidades restantes.
- **Rama de Interfaz (`rama-interfaz`):** Finalmente, para no comprometer la estabilidad del desarrollo principal, se extrajo la `rama-interfaz` a partir de la rama de vico. El propósito de esta rama ha sido aislar el "debugging" y la integración de la interfaz gráfica sin afectar al proyecto general.

**Pull Requests más importantes:**
- **PR #1:** *Configuración inicial CI/CD y estructura base* - Establecimiento del esqueleto del proyecto, configuración de Maven y despliegue del pipeline de integración continua con GitHub Actions.
- **PR #6:** *Modelado de Dominio: Tarjetas y Etiquetas* - Integración de la lógica central para la gestión de tarjetas, etiquetas y listas, junto con sus respectivos tests unitarios.
- **PR #7:** *Implementación de la Capa de Adaptadores (Salida)* - Desarrollo completo de la persistencia de datos mediante JPA y los adaptadores de salida para mantener el dominio limpio e independiente.
- **PR #8:** *Refactorización e Integración con SonarQube* - Configuración de perfiles de análisis, solución de *code smells*, aumento de la cobertura de tests y validación final de la calidad del código del proyecto.
