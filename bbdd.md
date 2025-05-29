# Estructura de la Base de Datos (`BBDD.md`)

## Descripción General

La base de datos está diseñada para gestionar la información relacionada con camas hospitalarias, sus estados y su localización dentro de hospitales y dependencias. Se han definido tres entidades principales: `Hospital`, `Dependencia` y `Cama`, cada una mapeada como una tabla en la base de datos relacional utilizando JPA (Jakarta Persistence API).

---

## Tablas y Estructura

### Tabla: `hospital`

| Campo     | Tipo           | Descripción                     |
|-----------|----------------|---------------------------------|
| id        | Long (PK)      | Identificador único del hospital |
| nombre    | String         | Nombre del hospital              |

---

### Tabla: `dependencia`

| Campo       | Tipo           | Descripción                                  |
|-------------|----------------|----------------------------------------------|
| id          | Long (PK)      | Identificador único de la dependencia        |
| nombre      | String         | Nombre de la dependencia                     |
| hospital_id | Long (FK)      | Relación con el hospital al que pertenece    |

---

### Tabla: `cama`

| Campo          | Tipo                  | Descripción                                  |
|----------------|-----------------------|----------------------------------------------|
| id             | Long (PK)             | Identificador único de la cama               |
| estado         | Enum (`EstadoCama`)   | Estado actual de la cama (e.g., LIBRE, OCUPADA) |
| hospital_id    | Long (FK)             | Hospital donde se encuentra la cama          |
| dependencia_id | Long (FK)             | Dependencia donde se encuentra la cama       |

---

## Decisiones de Diseño

- Se utilizó una **base de datos relacional** con JPA/Hibernate para facilitar el mapeo objeto-relacional en la aplicación Java.
- Se usó la estrategia `GenerationType.IDENTITY` para la generación automática de los `id`, permitiendo una gestión sencilla de claves primarias auto-incrementales.
- La entidad `Cama` se relaciona tanto con `Hospital` como con `Dependencia` para reflejar con precisión su ubicación física dentro de la institución.
- El estado de la cama se maneja como un enumerado`EstadoCama` con `@Enumerated(EnumType.STRING)` para guardar valores legibles como texto en la base de datos, facilitando su interpretación y mantenimiento.
- Se utiliza la anotación `@ManyToOne` para establecer relaciones muchos-a-uno entre:
  - `Cama` y `Hospital`
  - `Cama` y `Dependencia`
  - `Dependencia` y `Hospital`
- Estas relaciones reflejan una **jerarquía natural** del sistema hospitalario, donde múltiples camas y dependencias pertenecen a un hospital.

