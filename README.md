# 🍽️ API REST para Control de Pedidos y Mesas de Restaurante

**Proyecto Final – DAM B | Alejandro Pau**  
**Asignatura:** Acceso a Datos  
**Framework:** Spring Boot  
**Base de datos:** `proyectofinal_damb` (MySQL)

---

## 📋 Descripción del proyecto

API REST desarrollada con Spring Boot para la gestión integral de un restaurante. Permite administrar mesas, clientes, empleados, pedidos y platos desde una arquitectura por capas limpia y escalable.

---

## 🏢 Problema de negocio

Un restaurante necesita digitalizar la gestión de su sala. Actualmente el control de mesas, pedidos y consumo de clientes se realiza de forma manual, lo que genera errores e ineficiencias.

La API resuelve este problema permitiendo:
- Registrar y gestionar clientes
- Controlar el estado de las mesas (libre/ocupada)
- Asignar empleados a mesas
- Crear y gestionar pedidos con sus platos
- Cerrar cuentas y generar tickets de consumo

---

## 🗂️ Modelo de datos

El sistema está compuesto por las siguientes entidades:

| Entidad | Descripción |
|---|---|
| `Cliente` | Persona que consume en el restaurante |
| `Mesa` | Mesa física del restaurante, con estado libre/ocupada |
| `Empleado` | Trabajador del restaurante, asignado a una mesa |
| `Plato` | Elemento del menú con nombre, descripción, precio e imagen |
| `Pedido` | Orden de consumo vinculada a cliente, mesa y empleado |
| `DetallePedido` | Línea de un pedido con plato, cantidad y precio unitario |

### Relaciones principales

- Un **Empleado** atiende como máximo una **Mesa** (`@OneToOne`)
- Un **Cliente** puede tener varios **Pedidos** a lo largo del tiempo (`@ManyToOne`)
- Una **Mesa** solo puede tener un pedido abierto a la vez
- Un **Pedido** contiene varios **DetallePedido** (`@OneToMany`)
- Cada **DetallePedido** referencia un **Plato** (`@ManyToOne`)

---

## ⚙️ Instrucciones de ejecución

### Requisitos previos

- Java 17 o superior
- Maven
- XAMPP con MySQL activo
- Postman o Insomnia para pruebas

### Pasos

1. Clona o descarga el proyecto
2. Abre **XAMPP** y arranca el servicio **MySQL**
3. Accede a `http://localhost/phpmyadmin` y crea la base de datos:
   ```sql
   CREATE DATABASE proyectofinal_damb CHARACTER SET utf8 COLLATE utf8_general_ci;
   ```
4. Abre el proyecto en **IntelliJ IDEA**
5. Verifica el fichero `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/proyectofinal_damb?useSSL=false&useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=
   ```
6. Ejecuta la aplicación. Hibernate creará las tablas automáticamente.
7. La API estará disponible en `http://localhost:8080`

---

## 🔌 Endpoints principales

### Clientes

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/cliente/list` | Lista todos los clientes |
| `GET` | `/cliente/detail/{id}` | Obtiene un cliente por ID |
| `POST` | `/cliente/create` | Registra un nuevo cliente |
| `DELETE` | `/cliente/delete/{id}` | Elimina un cliente |

**Ejemplo de body para crear cliente:**
```json
{
    "nombre": "Juan",
    "apellido": "García",
    "telefono": "600123456",
    "email": "juan@email.com"
}
```

### Endpoints automáticos (Spring Data REST)

Spring genera automáticamente endpoints CRUD bajo `/api` para:

| Endpoint | Entidad |
|---|---|
| `/api/clientes` | Cliente |
| `/api/mesas` | Mesa |
| `/api/empleados` | Empleado |
| `/api/platos` | Plato |
| `/api/pedidos` | Pedido |
| `/api/detallePedidos` | DetallePedido |

> ⚠️ Los endpoints manuales (controladores propios) **no** llevan el prefijo `/api`.  
> Los endpoints automáticos de repositorio **sí** llevan el prefijo `/api`.

---

## 🏗️ Arquitectura del proyecto

El proyecto sigue una arquitectura por capas:

```
controller/   → Recibe peticiones HTTP y devuelve respuestas
service/      → Contiene la lógica de negocio
dao/          → Repositorios de acceso a base de datos
entity/       → Entidades JPA (modelo de datos)
dto/          → Objetos de transferencia de datos
```

---

## 📦 Dependencias principales

| Dependencia | Uso |
|---|---|
| Spring Data JPA | Persistencia y acceso a datos |
| Spring Data REST | Endpoints automáticos para repositorios |
| MySQL Driver | Conexión con la base de datos |
| Lombok | Generación automática de getters/setters |
| Apache Commons Lang | Validación de campos con `StringUtils` |

---

## ✅ Casos de uso implementados

- [x] Registrar cliente
- [ ] Asignar mesa
- [ ] Crear pedido
- [ ] Agregar plato al pedido
- [ ] Cerrar cuenta
- [ ] Generar ticket
- [ ] Asignar empleado a cliente
