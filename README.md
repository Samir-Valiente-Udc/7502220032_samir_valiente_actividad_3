# 📋 Sistema de Gestión de Usuarios y Contratos

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-green.svg)
![Java](https://img.shields.io/badge/Java-17-blue.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange.svg)

## 📝 Descripción
Este proyecto es una aplicación web desarrollada con **Spring Boot** que implementa un sistema completo de gestión de usuarios y contratos laborales con autenticación segura y validación de datos.

---

## 🛠 Tecnologías Utilizadas

| Categoría       | Tecnologías                                                                 |
|-----------------|-----------------------------------------------------------------------------|
| **Backend**     | ![Java](https://img.shields.io/badge/Java-17-blue) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-green) ![Spring Security](https://img.shields.io/badge/Spring_Security-6.2.1-brightgreen) |
| **Persistencia**| ![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-3.2.5-yellowgreen) ![MySQL](https://img.shields.io/badge/MySQL-8.0-orange) ![Hibernate](https://img.shields.io/badge/Hibernate-6.4-blueviolet) |
| **Frontend**    | ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-green) ![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple) |
| **Herramientas**| ![Maven](https://img.shields.io/badge/Maven-3.6+-red) ![Lombok](https://img.shields.io/badge/Lombok-1.18-pink) ![BCrypt](https://img.shields.io/badge/BCrypt-4.1-lightgrey) |

---

## 🔥 Características Principales

### 🔒 Módulo de Seguridad
- 🔐 Autenticación basada en sesiones
- 🛡️ Encriptación BCrypt para contraseñas
- 🚦 Control de acceso por roles
- ❓ Recuperación mediante preguntas de seguridad

### 👥 Gestión de Usuarios
- 📝 Registro con validación
- 🔍 Búsqueda y filtrado avanzado
- ✏️ Edición de perfiles
- 🗑️ Eliminación segura
- 📊 Dashboard administrativo

### 📑 Gestión de Contratos
- 📅 Creación con validación de fechas
- 👨‍💼 Asignación de empleados
- 💰 Administración de montos
- 🔎 Búsqueda por múltiples criterios
- 📈 Visualización consolidada

---

## 🏗️ Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── ejemplo/
│   │           ├── config/        # Configuraciones
│   │           ├── controllers/   # Controladores MVC
│   │           ├── models/        # Entidades JPA
│   │           ├── repositories/  # Interfaces JPA
│   │           ├── services/      # Lógica de negocio
│   │           └── utils/         # Utilidades
│   └── resources/
│       ├── static/                # CSS, JS, imágenes
│       ├── templates/             # Vistas Thymeleaf
│       └── application.properties # Configuración
```

---

## ⚙️ Configuración

### 🔧 Base de Datos
```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/DB_EJERCICIO_16
spring.datasource.username=samir_valiente
spring.datasource.password=AbcdeUdeC
spring.jpa.hibernate.ddl-auto=update
```

### 🚀 Ejecución
1. Clonar repositorio: `git clone`
2. Crear base de datos MySQL
3. Configurar credenciales en `application.properties`
4. Ejecutar: `mvn spring-boot:run`
5. Acceder a: `http://localhost:8080/index`

---

## 👥 Usuarios Predefinidos

| Rol       | Usuario   | Contraseña |
|-----------|-----------|------------|
| Admin     | admin     | admin123   |
| Usuario   | Carlos123 | [hasheada] |

---

## 📌 Requisitos

- ![Java](https://img.shields.io/badge/Java-17+-blue) JDK 17 o superior
- ![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange) MySQL 8.0+
- ![Maven](https://img.shields.io/badge/Maven-3.6+-red) Maven 3.6+

---

## 📜 Licencia
Este proyecto está bajo licencia MIT. Ver archivo [LICENSE](LICENSE) para más detalles.