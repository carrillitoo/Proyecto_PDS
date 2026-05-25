# Proyecto_PDS
Proyecto de la asignatura de Procesos de Desarrollo de Software, un organizador de tableros tipo Trello, con Drag and Drop (DnD) y gestión de tarjetas, multiusuario y persistencia de trazabilidad

## Integrantes
La practica la hemos realizado:
- Victor Carrillo Gil
- Javier Peñalver Gómez 
- Jaime Cegarra Martínez 

Para más información sobre el reparto de trabajo, se puede consultar el [CREDITOS.md](documentacion/CREDITOS.md)

## Estructura Documentación
La documentación la hemos dividido en los siguientes documentos:

Para manuales, hemos creado:
- Un [Manual de Usuario](documentacion/manual-usuario.md)
- Un [Manual de Desarrollador](documentacion/manual-dev.md)

Para la estructura interna hemos creado dos ficheros principales:
- Una explicación de los [modelos](documentacion/modelo.md), donde se muestra la estructura de la capa de dominio, y la relación entre clases
- Una reflexión sobre las diferentes [decisiones de diseño](documentacion/decisiones-de-diseño.md) que hemos tomado, en relación a la arquitectura hexagonal, DDD, etc.

Finalmente, hemos añadido, dos ficheros mostrando otras perspectivas:
- Una representación de las [pantallas](documentacion/pantallas.md) de la aplicación donde se describe de manera gráfica las diferentes vistas, perspectivas
- Una representación de las [historias de usuario](documentacion/historias-usuario.md) donde se representan algunas historias de usuario extraídas de las prácticas de la asignatura de Gestión de Proyectos de Desarrollo Software (GPDS)

## Lanzamiento y Despliegue
Para poder lanzar la aplicación hay que hacer lo siguiente:

Clonar el repositorio:
```bash
git clone https://github.com/carrillitoo/Proyecto_PDS.git
```

Para poder tener los ejecutables disponibles hay que:
1. Abre Eclipse.
2. Ve al menú superior y selecciona **File > Import...**
3. Despliega la carpeta **Maven**, selecciona **Existing Maven Projects** y haz clic en **Next**.
4. En **Root Directory**, haz clic en *Browse...* y busca la carpeta del proyecto que acabas de clonar.
5. Asegúrate de que el archivo `pom.xml` aparezca marcado en la lista y haz clic en **Finish**.


Para evitar conflictos con archivos residuales y asegurar que todo está sincronizado, realiza una limpieza del proyecto:
1. Ve al menú superior y selecciona **Project > Clean...**
2. Selecciona el proyecto de la lista, asegúrate de que la opción *"Clean projects selected below"* esté marcada y haz clic en **Clean**.
3. A continuación, haz clic derecho sobre el proyecto en el explorador de la izquierda, selecciona **Maven > Update Project...** (o pulsa `Alt + F5`).
4. En la ventana que aparece, asegúrate de marcar la casilla *"Force Update of Snapshots/Releases"* y pulsa **OK**.

Finalmente, para poder lanzar la aplicación de manera correcta:
1. En el explorador de proyectos en el proyecto `ProyectoPDS`, despliega la carpeta `src/main/java/umu/pds/api` y encontrarás la clase `ApiApplication`.
2. Haz clic derecho sobre este archivo.
3. Selecciona **Run**.
4. Abre la vista de la **Consola** en Eclipse para comprobar que la aplicación arranca correctamente, fijala para más comodidad.
5. En el explorador de proyectos en el proyecto `ProyectoPDS-GUI`, despliega la carpeta `src/main/java/umu/pds/gui` y encontrarás la clase `Main`.
6. Haz clic derecho sobre este archivo.
7. Selecciona **Run**.
8. Se desplegará la ventana que se conecta con la api, que otorga la vista de usuario

    
