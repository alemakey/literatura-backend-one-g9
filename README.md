# 📚 LiterAlura — Challenge Alura + Oracle ONE G9

[![Alura Latam](https://img.shields.io/badge/Alura%20Latam-Formación%20ONE-0070f3?style=for-the-badge)](https://www.aluracursos.com/) [![Oracle Next Education](https://img.shields.io/badge/Oracle%20Next%20Education-ONE%20G9-f80000?style=for-the-badge&logo=oracle&logoColor=white)](https://www.oracle.com/mx/education/oracle-next-education/) ![Proyecto educativo](https://img.shields.io/badge/Proyecto-educativo-blue?style=for-the-badge)

![Java 17](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white) ![Spring Boot 3.2.x](https://img.shields.io/badge/Spring%20Boot-3.2.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)

> Aplicación de consola desarrollada en **Java + Spring Boot** para explorar el mundo de los libros, conectándose a la API pública de [Gutendex](https://gutendex.com/) y almacenando los resultados en una base de datos.

---

## 🗂️ Funcionalidades

Al iniciar la aplicación, se muestra un menú interactivo en consola con las siguientes opciones:

| # | Opción |
|---|--------|
| 1 | 🔍 Buscar y guardar un libro desde la API Gutendex |
| 2 | 📖 Listar todos los libros registrados en la BD |
| 3 | 👤 Listar todos los autores registrados en la BD |
| 4 | 🌱 Buscar autores vivos en un año determinado |
| 5 | 🌐 Filtrar libros por idioma (en / es) |
| 6 | 🔎 Buscar un autor por nombre en la BD |
| 7 | 🏆 Top 10 libros más descargados (desde la API) |
| 8 | 📊 Top 10 libros más descargados (desde la BD) |
| 9 | 🎂 Autores nacidos después de un año específico |
| 10 | 🕊️ Autores fallecidos antes de un año específico |
| 0 | 🚪 Salir |

---

## 🛠️ Tecnologías utilizadas

- ☕ **Java 17**
- 🍃 **Spring Boot 3.2.5**
- 🗄️ **Spring Data JPA / Hibernate 6**
- 💾 **H2 Database** (base de datos en memoria)
- 📦 **Maven Wrapper** (no requiere Maven instalado)
- 🌐 **API Gutendex** — catálogo público de libros del Proyecto Gutenberg

---

## 🚀 Cómo ejecutar el proyecto localmente

### Requisitos previos

- **JDK 17 o superior** instalado ([descargar aquí](https://www.oracle.com/java/technologies/downloads/))
- **Git** instalado

### Pasos

**1. Clonar el repositorio**

```bash
git clone https://github.com/alemakey/literatura-backend-one-g9.git
cd literatura-backend-one-g9
```

**2. Configurar la variable de entorno de Java** *(solo si no está configurada globalmente)*

En Windows (PowerShell):

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
```

En macOS / Linux:

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
```

**3. Ejecutar la aplicación**

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

En macOS / Linux:

```bash
./mvnw spring-boot:run
```

**4. Interactuar con el menú**

La aplicación se inicia en la consola y muestra el menú de opciones. Escribe el número de la opción deseada y presiona Enter.

---

## 🗃️ Consola de base de datos H2

Mientras la app está corriendo, puedes explorar la BD en el navegador:

- URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL:** `jdbc:h2:mem:literaturadb`
- **Usuario:** `sa`
- **Contraseña:** *(dejar vacío)*

---

## 📁 Estructura del proyecto

```
src/
└── main/
    ├── java/com/alura/literatura/
    │   ├── controller/     # Endpoints REST (LibroController)
    │   ├── dto/            # Data Transfer Objects
    │   ├── model/          # Entidades JPA (Libro, Autor) y records de la API
    │   ├── principal/      # Lógica del menú de consola
    │   ├── repository/     # Repositorios Spring Data JPA
    │   └── service/        # Lógica de negocio y consumo de API
    └── resources/
        └── application.properties
```

---

## 👨‍💻 Autor

Desarrollado por **Victor** como parte del **Challenge LiterAlura** del programa Oracle Next Education (ONE) G9 con Alura Latam.
