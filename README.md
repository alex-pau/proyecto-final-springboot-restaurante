# API REST para Control de Pedidos y Mesas de Restaurante

API REST desarrollada con Spring Boot para la gestión integral de un restaurante: mesas, clientes, empleados, pedidos y platos.

---

## Descripción del proyecto

Este proyecto implementa una API REST que permite administrar el flujo completo de un restaurante en sala, desde el registro de clientes hasta la generación del ticket final. La arquitectura sigue un diseño por capas (Controladores, Servicios, Repositorios, Entidades) y cubre todos los casos de uso del negocio.

---

## Problema de negocio

Un restaurante necesita una solución digital para gestionar sus operaciones en sala de forma eficiente. Los problemas concretos que resuelve esta API son:

- Controlar qué mesas están ocupadas o libres en tiempo real.
- Registrar clientes y asignarles un empleado responsable de su atención.
- Gestionar pedidos abiertos y cerrados, vinculados a una mesa y un cliente.
- Permitir añadir platos a un pedido activo, registrando el precio en el momento exacto del pedido para evitar inconsistencias si el precio cambia después.
- Cerrar la cuenta calculando el total automáticamente y liberando la mesa.
- Generar el ticket final con todos los detalles del pedido.

---

## Instrucciones de ejecución

### Requisitos previos

- Java 17 o superior
- Maven
- MySQL

### Configuración de la base de datos

Crea la base de datos en MySQL:

```sql
CREATE DATABASE proyectofinal_damb;
```

Configura el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/proyectofinal_damb
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Ejecución

```bash
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080`.

---

## Arquitectura del proyecto

```
src/main/java/com/proyectofinal/restaurante/
├── controller/     # Controladores REST (endpoints)
├── service/        # Lógica de negocio
├── dao/            # Repositorios JPA
├── entity/         # Entidades de base de datos
└── dto/            # Objetos de transferencia de datos
```

### Entidades del sistema

| Entidad | Descripción |
|---|---|
| `Cliente` | Persona que realiza el pedido |
| `Mesa` | Mesa del restaurante, puede estar ocupada o libre |
| `Empleado` | Personal del restaurante asignado a atender al cliente |
| `Pedido` | Orden abierta o cerrada, vincula cliente, mesa y empleado |
| `Plato` | Ítem del menú con nombre, descripción y precio |
| `DetallePedido` | Línea de un pedido: plato, cantidad y precio unitario en el momento del pedido |

---

## Flujo principal de uso

```
1. POST /cliente/create          → Registrar cliente
2. POST /pedido/create           → Crear pedido (asigna mesa + empleado automáticamente)
3. POST /detalle/create          → Agregar platos al pedido
4. PUT  /pedido/cerrar/{id}      → Cerrar cuenta (calcula total + libera mesa)
5. GET  /pedido/ticket/{id}      → Obtener ticket del pedido cerrado
```

---

## Endpoints principales

### Clientes — `/cliente`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/cliente/lista` | Listar todos los clientes |
| GET | `/cliente/detail/{id}` | Obtener cliente por ID |
| POST | `/cliente/create` | Registrar nuevo cliente |
| PUT | `/cliente/update/{id}` | Actualizar datos de un cliente |
| DELETE | `/cliente/delete/{id}` | Eliminar cliente |

**Body para crear/actualizar:**
```json
{
  "nombre": "Juan",
  "apellido": "García",
  "telefono": "612345678",
  "email": "juan@email.com"
}
```

---

### Mesas — `/mesa`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/mesa/lista` | Listar todas las mesas |
| GET | `/mesa/detail/{id}` | Obtener mesa por ID |
| POST | `/mesa/create` | Crear nueva mesa |
| PUT | `/mesa/update/{id}` | Actualizar mesa |
| PUT | `/mesa/asignar/{id}` | Marcar mesa como ocupada |
| PUT | `/mesa/liberar/{id}` | Marcar mesa como libre |
| DELETE | `/mesa/delete/{id}` | Eliminar mesa |

**Body para crear/actualizar:**
```json
{
  "numero": 5,
  "capacidad": 4
}
```

---

### Empleados — `/empleado`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/empleado/lista` | Listar todos los empleados |
| GET | `/empleado/detail/{id}` | Obtener empleado por ID |
| POST | `/empleado/create` | Crear nuevo empleado |
| PUT | `/empleado/update/{id}` | Actualizar empleado |
| DELETE | `/empleado/delete/{id}` | Eliminar empleado |

**Body para crear/actualizar:**
```json
{
  "nombre": "María",
  "apellido": "López",
  "cargo": "Camarera"
}
```

---

### Platos — `/plato`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/plato/lista` | Listar todos los platos |
| GET | `/plato/detail/{id}` | Obtener plato por ID |
| POST | `/plato/create` | Crear nuevo plato |
| PUT | `/plato/update/{id}` | Actualizar plato |
| DELETE | `/plato/delete/{id}` | Eliminar plato |

**Body para crear/actualizar:**
```json
{
  "nombre": "Paella Valenciana",
  "descripcion": "Paella tradicional con pollo y verduras",
  "precio": 14.50,
  "imagenUrl": "https://ejemplo.com/paella.jpg"
}
```

---

### Pedidos — `/pedido`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/pedido/lista` | Listar todos los pedidos |
| GET | `/pedido/detail/{id}` | Obtener pedido por ID |
| GET | `/pedido/cliente/{clienteId}` | Pedidos de un cliente |
| POST | `/pedido/create` | Crear pedido (asigna mesa y empleado) |
| PUT | `/pedido/cerrar/{id}` | Cerrar cuenta (calcula total y libera mesa) |
| GET | `/pedido/ticket/{id}` | Generar ticket (solo pedidos cerrados) |
| DELETE | `/pedido/delete/{id}` | Eliminar pedido |

**Body para crear:**
```json
{
  "clienteId": 1,
  "mesaId": 3,
  "empleadoId": 2
}
```

---

### Detalles de pedido — `/detalle`

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/detalle/lista` | Listar todos los detalles |
| GET | `/detalle/detail/{id}` | Obtener detalle por ID |
| GET | `/detalle/porPedido/{pedidoId}` | Detalles de un pedido concreto |
| POST | `/detalle/create` | Agregar plato al pedido |
| PUT | `/detalle/update/{id}` | Actualizar cantidad de un detalle |
| DELETE | `/detalle/delete/{id}` | Eliminar detalle |

**Body para agregar plato:**
```json
{
  "pedidoId": 1,
  "platoId": 4,
  "cantidad": 2
}
```

---

## Gestión de errores

La API controla y responde correctamente ante situaciones como:

- Recursos inexistentes → `404 Not Found` con mensaje descriptivo
- Datos incorrectos o incompletos → `400 Bad Request` con mensaje descriptivo
- Operaciones no permitidas (añadir plato a pedido cerrado, asignar mesa ocupada, etc.) → `400 Bad Request`

---

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Data JPA — persistencia y repositorios
- Lombok — reducción de código boilerplate
- MySQL — base de datos relacional
- Apache Commons Lang — utilidades de validación de strings
- Spring Data REST — exposición automática de repositorios

---

## Autor

Alejandro Pau — DAM B
