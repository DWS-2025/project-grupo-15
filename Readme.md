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
🖼️ ![Diagrama de Entidades](entidades.png)

---

## 🛠️ Herramientas y Tecnologías

- **Spring Boot** – Backend robusto con APIs REST.
- **HTML/CSS** – Interfaz inspirada en galerías de arte contemporáneo.
- **MySQL** – Base de datos relacional.
- **DTOs y Mappers** – Flujo limpio de datos entre cliente y servidor.

---

## 👩‍🎨 Equipo de Desarrollo

Desarrollado por el grupo **15**:

👨‍💻 **Alejandro Sanchez**  
Encargado de la integración de DTOs, lógica REST y pruebas funcionales.  
🔗 [@usuarioGit](https://github.com/<idDeGithub>)

👨‍🎨 **Samuel López**  
Responsable del rediseño visual, estructura de base de datos y mejoras de experiencia.  
🔗 [@usuarioGit](https://github.com/<idDeGithub>)

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


# Museo del Trapo

Grupo 15
Por hacer:
- Paginacion AJAX
- Comentarios en el codigo

En cuanto a la migración a MySQL:
- Descargar mysqlserver
- credenciales y demás
- Meterlo al pom.xml (Cuidado con lo que digeron en el foro sobre la version)
- Cambiarlo en application properties