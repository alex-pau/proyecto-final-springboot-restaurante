# API REST — Control de Pedidos y Mesas de Restaurante

API REST desarrollada con **Spring Boot** para la gestión integral de un restaurante: mesas, clientes, empleados, pedidos y platos.

---

## Descripción del proyecto

Este proyecto implementa una API REST que permite administrar el flujo completo de un restaurante en sala, desde el registro de clientes hasta la generación del ticket final.

La arquitectura sigue un diseño por capas:

```
Controller → Service → Repository → Entity
```

Cubre todos los casos de uso del negocio: registrar clientes, asignar mesas, gestionar pedidos, agregar platos, cerrar cuenta y generar ticket.

---

## Problema de negocio

Un restaurante necesita una solución digital para gestionar sus operaciones en sala de forma eficiente. Los problemas concretos que resuelve esta API son:

- Controlar qué mesas están **ocupadas o libres** en tiempo real.
- **Registrar clientes** y asignarles un empleado responsable de su atención.
- Gestionar **pedidos abiertos y cerrados**, vinculados a una mesa y un cliente.
- Permitir **añadir platos** a un pedido activo, registrando el precio en el momento exacto del pedido para evitar inconsistencias si el precio cambia después.
- **Cerrar la cuenta** calculando el total automáticamente y liberando la mesa.
- **Generar el ticket final** con todos los detalles del pedido.

---

## Instrucciones de ejecución

### Requisitos previos

- Java 17 o superior
- Maven
- MySQL 8.x

### Configuración de la base de datos

Crear la base de datos en MySQL:

```sql
CREATE DATABASE proyectofinal_damb;
```

Ajustar credenciales en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/proyectofinal_damb
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> Las tablas se crean automáticamente al arrancar gracias a `ddl-auto=update`.

### Arrancar la aplicación

```bash
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:8080`

### Orden recomendado para probar

```
1. POST /empleado/create       → Crear empleados
2. POST /mesa/create           → Crear mesas
3. POST /plato/create          → Crear platos
4. POST /cliente/create        → Registrar cliente
5. POST /pedido/create         → Crear pedido (asigna mesa + empleado)
6. POST /detalle/create        → Añadir platos al pedido
7. PUT  /pedido/cerrar/{id}    → Cerrar cuenta
8. GET  /pedido/ticket/{id}    → Generar ticket
```

---

## Arquitectura del proyecto

```
src/main/java/com/proyectofinal/restaurante/
├── controller/     → Controladores REST (un fichero por entidad)
├── service/        → Lógica de negocio y validaciones
├── dao/            → Repositorios JPA (interfaces)
├── entity/         → Entidades JPA mapeadas a tablas MySQL
└── dto/            → Objetos de transferencia (entrada y salida)
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

## Casos de uso

### CU-01 · Registrar cliente

Da de alta un nuevo cliente validando que nombre, apellido y email estén presentes y que el email no esté ya registrado.

- **Endpoint:** `POST /cliente/create`
- **Validaciones:** campos obligatorios, email único

### CU-02 · Asignar mesa

Una mesa se asigna automáticamente al crear el pedido (pasa a `ocupada = true`). También existe un endpoint manual para asignar o liberar mesas.

- **Endpoint principal:** `PUT /mesa/asignar/{id}`
- **Asignación automática en:** `POST /pedido/create`
- **Validación clave:** no se puede asignar una mesa ya ocupada

### CU-03 · Crear pedido

Crea un pedido abierto vinculando cliente, mesa y empleado en un solo paso. La fecha se asigna automáticamente y la mesa queda marcada como ocupada.

- **Endpoint:** `POST /pedido/create`
- **Body:** `clienteId`, `mesaId`, `empleadoId`
- **Validaciones:** los tres IDs deben existir, la mesa debe estar libre

### CU-04 · Agregar plato al pedido

Añade una línea de detalle al pedido indicando el plato y la cantidad. El precio unitario se captura del plato en ese momento, protegiendo el pedido ante futuros cambios de precio.

- **Endpoint:** `POST /detalle/create`
- **Body:** `pedidoId`, `platoId`, `cantidad`
- **Validaciones:** cantidad > 0, pedido debe estar abierto

### CU-05 · Cerrar cuenta

Cierra el pedido calculando el total como suma de `cantidad × precioUnitario` de cada línea. Libera automáticamente la mesa asociada.

- **Endpoint:** `PUT /pedido/cerrar/{id}`
- **Validación:** el pedido no puede estar ya cerrado
- **Efecto secundario:** la mesa queda libre automáticamente

### CU-06 · Generar ticket

Devuelve un resumen completo del pedido cerrado: cliente, empleado, mesa, fecha, líneas de detalle con subtotales y total final.

- **Endpoint:** `GET /pedido/ticket/{id}`
- **Validación:** el pedido debe estar cerrado
- **Respuesta:** objeto `TicketDto` con lista de `TicketLineaDto`

### CU-07 · Asignar un empleado a un cliente

La asignación se realiza al crear el pedido: el campo `empleadoId` vincula directamente al empleado (camarero) con el cliente a través del pedido. La entidad `Pedido` tiene una relación `@ManyToOne` con `Empleado`, garantizando la trazabilidad del servicio.

- **Mecanismo:** campo `empleado` en la entidad `Pedido`
- **Endpoint:** `POST /pedido/create` (incluye `empleadoId` en el body)
- **Consulta:** `GET /pedido/cliente/{clienteId}` devuelve todos los pedidos con su empleado asignado

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

```json
// Body crear/actualizar
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

```json
// Body crear/actualizar
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

```json
// Body crear/actualizar
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

```json
// Body crear/actualizar
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

```json
// Body crear
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

```json
// Body agregar plato
{
  "pedidoId": 1,
  "platoId": 4,
  "cantidad": 2
}
```

---

## Gestión de errores

La API controla y responde correctamente ante las siguientes situaciones:

| Situación | Código HTTP |
|---|---|
| Recurso no encontrado | `404 Not Found` |
| Campo obligatorio vacío | `400 Bad Request` |
| Email o número de mesa duplicado | `400 Bad Request` |
| Mesa ya ocupada al crear pedido | `400 Bad Request` |
| Añadir plato a pedido cerrado | `400 Bad Request` |
| Pedir ticket sin cerrar la cuenta | `400 Bad Request` |

Todos los errores devuelven un JSON descriptivo:

```json
{
  "mensaje": "la mesa ya está ocupada"
}
```

---

## Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA** — persistencia y repositorios
- **Lombok** — reducción de código boilerplate
- **MySQL** — base de datos relacional
- **Apache Commons Lang** — validación de strings con `StringUtils`
- **Spring Data REST** — exposición automática de repositorios

---

## Autor

**Alejandro Pau** — DAM B
