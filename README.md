# Literatura Backend - ONE G9 (LiterAlura)

Proyecto del Challenge **LiterAlura** (Oracle Next Education - Grupo 9).
Aplicación backend en **Java 17 + Spring Boot** para consultar y gestionar información de **libros** y **autores**,
consumiendo datos desde una API externa y persistiendo en **PostgreSQL** con **JPA/Hibernate**.

## 🚀 Tecnologías
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Jackson (JSON)

## ✅ Funcionalidades (menú)
- Buscar libros por título
- Listar libros registrados
- Listar autores registrados
- Buscar autores por año (rango / vivos en cierto año)
- Guardar resultados en base de datos

## 🧩 Requisitos
- Java 17
- Maven 3+
- PostgreSQL (o cambiar configuración a H2 si lo deseas)

## ⚙️ Configuración
Crea una base de datos en PostgreSQL, por ejemplo:

```sql
CREATE DATABASE literatura;
Configura tus variables en application.properties (ejemplo):

properties
Copiar código
spring.datasource.url=jdbc:postgresql://localhost:5432/literatura
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
▶️ Ejecución
En la carpeta del proyecto:

bash
Copiar código
mvn clean install
mvn spring-boot:run
🧪 Tests
bash
Copiar código
mvn test
📌 Autor
Víctor Martínez Reyna
Challenge Backend ONE G9

yaml
Copiar código
