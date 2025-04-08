# 🎨 Museo del Trapo - Portal Web Oficial

> _“Detrás de cada pincelada, una historia. Detrás del trapo, un arte que respira.”_

Bienvenidos al **Museo del Trapo**, un espacio cultural que recoge la obra de artistas emergentes y consagrados en el arte pictórico. Desde lienzos modernos hasta técnicas clásicas, este sitio web permite descubrir exposiciones únicas, adquirir obras originales y formar parte de una comunidad que vibra con el arte.

---

## 🖼️ ¿Qué puedes hacer en este museo digital?

🔍 **Explorar exposiciones de arte**  
🖌️ **Descubrir artistas y sus obras** 
🧾 **Comentar y dar me gusta en tus obras preferidas**
👤 **Crear una cuenta de visitante o acceder como administrador**

---

## 🧶 Arquitectura del Proyecto

El sistema se basa en una arquitectura REST bien estructurada. Las entidades clave del sistema son:

| Entidad        | Descripción                                                                 |
|----------------|-----------------------------------------------------------------------------|
| `User`         | Visitantes o administradores del museo digital.                             |
| `Artist`       | Creador o autora de las obras, con su propia ficha informativa.             |
| `Picture`      | Cuadros únicos que pueden visualizarse.                                     |
| `Comment`      | Deja tu comentario en los cuadros que más te gusten.                        |


### 🕸️ Relaciones entre entidades

- Un `User` puede:
  - Comentar varios `Picture`
  - Dar like a varios `Picture`
- Un `Artist` puede agrupar ninguna o muchas `Picture`
- Cada `Picture` pertenece a un `Artist`
- Un `Picture` puede ser comentado por múltiples `User`
- Un `Picture` puede tener me gusta de varios `User`

---

## 🛡️ Gestión de Acceso

| Rol              | Permisos                                                                  |
|------------------|--------------------------------------------------------------------------|
| `Administrador`  | Gestión total de usuarios, obras, artistas, comentarios y likes.         |
| `Usuario`        | Visualizar cuadros, comentarlos y dar like.                              |

---

## 🌍 Un paseo visual por el museo

📸 Los artistas y cuadros incluyen imágenes de alta calidad para enriquecer la experiencia virtual.  
🖼️ ![Diagrama de Entidades](src/main/resources/static/diagrama.png)

---

## 🛠️ Herramientas y Tecnologías

- **Spring Boot** – Backend robusto con APIs REST.
- **HTML/CSS** – Interfaz inspirada en galerías de arte contemporáneo.
- **MySQL** – Base de datos relacional.
- **DTOs y Mappers** – Flujo limpio de datos entre cliente y servidor.

---

## 👩‍🎨 Equipo de Desarrollo

Desarrollado por el grupo **15**:

👨‍💻 **Alejandro Sanchez Díaz**  
Encargado de:
  - Controladores Rest
  - Conversión a DTO y Mappers
  - Postman sampleData
  - Cambio a base de datos MySQL
  - Arreglos menores
🔗 [@asaanchezz10](https://github.com/asaanchezz10)

#### Listado de los 5 commits más significativos

1. **[Commit: 3b19352 -> Migración a MySQL y extras a la API](https://github.com/DWS-2025/project-grupo-15/commit/3b1935250db1bb88bbd90601d5ce39092d894a87)**  
   Descripción: Migración de la base de datos a MySQL y adición de nuevas funcionalidades a la API.

2. **[Commit: 53fd23a -> Funcionamiento completo de la API](https://github.com/DWS-2025/project-grupo-15/commit/53fd23a31d0da626274f5afb534aefd9ff1a3c4b)**  
   Descripción: Implementación completa de la API, asegurando que todos los endpoints funcionen correctamente.

3. **[Commit: 9f05623 -> Finalización de la creación de DTOS](https://github.com/DWS-2025/project-grupo-15/commit/9f0562349eb7acb3b27743a7f3935f0442551b10)**
   Descripción: Terminación de la creación de los DTOs para la correcta interacción entre capas de la aplicación.

4. **[Commit: 265b6a5 -> Funcionalidad completa del controlador web](https://github.com/DWS-2025/project-grupo-15/commit/265b6a5125974819aac2d276acb0d10efe119b9c)**  
   Descripción: Implementación final del controlador web, asegurando la correcta interacción con el frontend.

5. **[Commit: 7592700 -> Arreglo de referencias circulares](https://github.com/DWS-2025/project-grupo-15/commit/7592700a46242fd02f596bd1e5cd3de44c8e9753)**  
   Descripción: Solución de las referencias circulares que causaban errores en la serialización de objetos.

#### Listado de los 5 ficheros en los que más ha participado el miembro

1. **[ `src/main/java/es/museotrapo/trapo/controller/rest/PictureControllerREST`](https://github.com/DWS-2025/project-grupo-15/blob/main/src/main/java/es/museotrapo/trapo/controller/rest/PictureControllerREST.java)**
2. **[`src/main/java/es/museotrapo/trapo/service/PictureService.java`](https://github.com/DWS-2025/project-grupo-15/blame/main/src/main/java/es/museotrapo/trapo/service/PictureService.java)**  

3. **[`src/main/java/es/museotrapo/trapo/controller/rest/ArtistControllerREST`](https://github.com/DWS-2025/project-grupo-15/blame/main/src/main/java/es/museotrapo/trapo/controller/rest/ArtistControllerREST.java)**  

4. **[`src/main/java/es/museotrapo/trapo/dto/PictureDTO.java`](https://github.com/DWS-2025/project-grupo-15/blob/main/src/main/java/es/museotrapo/trapo/dto/PictureDTO.java)**  
   Se pone este fichero para remarcar la contribución en los DTOs
5. **[`src/main/java/es/museotrapo/trapo/service/CommentService.java`](https://github.com/DWS-2025/project-grupo-15/blame/main/src/main/java/es/museotrapo/trapo/service/CommentService.java)**

👨‍🎨 **Samuel López Cabezudo**  
Responsable de:
- Paginación
- Consulta dinámica
- Controladores Rest
- Arreglos menores
🔗 [@P3rseo](https://github.com/P3rseo)

#### Listado de los 5 commits más significativos

1. **[Commit: 02263d8 -> CommentService con DTOs](https://github.com/DWS-2025/project-grupo-15/commit/02263d8c8b0b8fa450e0c124fa8bc5d64cf36eeb)**  
   Descripción: Implementación de CommentService utilizando DTOs para una mejor interacción con la base de datos y la API.

2. **[Commit: bea9326 -> Paginación](https://github.com/DWS-2025/project-grupo-15/commit/bea9326bb35db42f29714dad66b1f667412b9f5a)**  
   Descripción: Implementación de la paginación en los resultados de la API para una mejor gestión de datos y eficiencia.

3. **[Commit: bb20be9 -> Picture REST](https://github.com/DWS-2025/project-grupo-15/commit/bb20be97e0eee55ef50dd92acdae325c3c141f5c)**  
   Descripción: Creación de los endpoints REST para manejar las operaciones relacionadas con las imágenes.

4. **[Commit: 6aaf08 -> Más implementación de la paginación](https://github.com/DWS-2025/project-grupo-15/commit/6aaf0898ef3711e2deee1ef64b48fd888306d815)**  
   Descripción: Continuación de la implementación de la paginación en varios servicios y controladores de la API.

5. **[Commit: 9f683a5 -> Creación del README](https://github.com/DWS-2025/project-grupo-15/commit/9f683a56706ac90d4c73d1994a71df0e7b947a27)**  
   Descripción: Creación del archivo README.

#### Listado de los 5 ficheros en los que más ha participado el miembro

1. **[ `src/main/java/es/museotrapo/trapo/controller/rest/ArtistControllerREST.java`](https://github.com/DWS-2025/project-grupo-15/blame/main/src/main/java/es/museotrapo/trapo/controller/rest/ArtistControllerREST.java)**
2. **[`src/main/java/es/museotrapo/trapo/controller/web/ArtistController.java`](https://github.com/DWS-2025/project-grupo-15/blame/main/src/main/java/es/museotrapo/trapo/controller/web/ArtistController.java)**

3. **[`src/main/java/es/museotrapo/trapo/service/ArtistService.java`](https://github.com/DWS-2025/project-grupo-15/blame/main/src/main/java/es/museotrapo/trapo/service/ArtistService.java)**

4. **[`src/main/resources/templates/artists.html`](https://github.com/DWS-2025/project-grupo-15/blob/main/src/main/resources/templates/artists.html)**  

5. **[`src/main/java/es/museotrapo/trapo/service/CommentService.java`](https://github.com/DWS-2025/project-grupo-15/blame/main/src/main/java/es/museotrapo/trapo/service/CommentService.java)**  
---

## 🔗 Rutas Principales

- `/index` – Página principal de acceso a cuadros, artistas y usuarios
- `/artists` – Lista de artistas con sus fichas y obras
- `/pictures` – Catálogo de cuadros disponibles
- `/users` – Registro de nuevos usuarios
- `/api/**` – Endpoints REST para integraciones y pruebas

---

## 🌟 Características Destacadas

🎨 Galería virtual con navegación fluida  
🧵 Diseño elegante que combina lo clásico y lo moderno  
📦 Imágenes de obras almacenadas y gestionadas eficientemente  
🔐 Roles y permisos para un acceso controlado (todavía no)

---

## 🗂️ ¿Qué viene después?

- ✅ Paginación, filtros y búsquedas por artista o técnica
- 🚧 Gestión de visitas guiadas y eventos especiales
- 📊 Panel con métricas para artistas y administradores
- 🌐 Versión multilingüe para visitantes internacionales

---

> Hecho con arte, propósito y código.  
> _Museo del Trapo – Donde la pintura cobra vida._
