# LinguaPlus

Sistema de gestión académica para la academia de idiomas **LinguaPlus**, desarrollado en
Java 21 + JavaFX, siguiendo el patrón MVC y aplicando cinco patrones de diseño (Singleton,
Builder, Prototype, Factory Method y Abstract Factory) sobre la lógica de negocio descrita
en el documento de pensamiento computacional y el diagrama de clases del proyecto.

## Requisitos

- **JDK 21** (LTS)
- **Maven 3.9+**

No se requiere instalar JavaFX por separado: Maven descarga las dependencias
`javafx-controls` y `javafx-fxml` (versión 21.0.2) automáticamente desde Maven Central.

## Cómo ejecutar

### 1. Interfaz gráfica (JavaFX)

```bash
mvn javafx:run
```

### 2. Pruebas de consola (sin JavaFX)

Ejecuta los casos de prueba de los 5 patrones y de las reglas de negocio
(equivalentes a la Pregunta 6 y Pregunta 8 del documento de pensamiento computacional):

```bash
mvn exec:java
```

### 3. Compilar y empaquetar un JAR ejecutable

```bash
mvn clean package
java -jar target/linguaplus.jar
```

## Estructura del proyecto

```
src/main/java/linguaplus/
├── modelo/                 Entidades de dominio y patrones Singleton/Builder/Prototype
│   ├── comprobante/        Factory Method (Comprobante, GeneradorComprobante...)
│   └── entrega/            Abstract Factory (FabricaEntrega, FabricaPresencial...)
├── servicio/                Capa de servicios / reglas de negocio (sin JavaFX)
├── excepcion/                Jerarquía de excepciones de negocio
├── app/                      AppContext (estado compartido de la GUI), Main, MainConsola
├── vista/                    (reservado; las vistas viven como FXML en resources)
└── controlador/              Controladores JavaFX (uno por pestaña)

src/main/resources/linguaplus/
├── vista/                   Archivos FXML (uno por pantalla)
└── css/                     Hoja de estilos (styles.css)
```

## Patrones de diseño aplicados

| Patrón | Clases | Requisito / Regla que resuelve |
|---|---|---|
| **Singleton** | `ConsecutivoMatricula` | RF-05 / RN-04: numeración de matrícula única y consecutiva, sin importar sede o equipo |
| **Builder** | `Matricula` + `Matricula.Builder` | RF-04: datos obligatorios/opcionales y validación centralizada (RN-01, RN-02, RN-03) antes de crear el objeto |
| **Prototype** | `PeriodoAcademico`, `OfertaPrograma` | RF-03 / RN-05: clonar una oferta base hacia cada periodo con cupos totalmente independientes |
| **Factory Method** | `GeneradorComprobante` → `GeneradorPDF`, `GeneradorExcel` | RF-09: mismo proceso de emisión, formato de comprobante intercambiable (y extensible a un tercer formato) |
| **Abstract Factory** | `FabricaEntrega` → `FabricaPresencial`, `FabricaVirtual` | RN-06 / RN-07: material + identificación coherentes según modalidad, sin mezclar familias |

## Notas de diseño importantes

- **La interfaz gráfica no contiene reglas de negocio.** Los controladores JavaFX solo
  capturan datos, invocan la capa de `servicio`/`modelo`, y muestran el resultado o el
  mensaje de error de la excepción lanzada (`LinguaPlusException` y sus subclases).
- **Comprobantes PDF/Excel son simulados como texto plano** (decisión explícita del
  cliente, sin librerías externas como iText o Apache POI). Gracias al Factory Method,
  sustituir la simulación por una librería real en el futuro solo implica modificar
  `ComprobantePDF` / `ComprobanteExcel` (o añadir un tercer formato), sin tocar el resto
  del sistema.
- **Persistencia:** todo el estado vive en memoria (colecciones Java / `ObservableList`),
  según el alcance definido para esta entrega. `AppContext` centraliza dicho estado para
  toda la GUI.
- **Relación Matrícula–Periodo:** el diagrama de clases no define un campo explícito de
  periodo en `Matricula`; se asumió que una matrícula "pertenece" a un periodo cuando su
  `fechaInicio` cae dentro del rango `[fechaInicio, fechaFin]` de ese periodo
  (`PeriodoAcademico.contiene(fecha)`), usado por `CalculadorIngresos` (RF-13).
- **Asignación de tutor (RN-09):** se valida que el idioma de especialidad del docente
  coincida con el idioma del programa antes de permitir la asignación
  (`AsignacionTutor.validar()`).

## Paleta de diseño (psicología del color)

- **Azul profundo `#1F4E78`**: confianza y concentración — marca, encabezado, títulos.
- **Blanco / gris `#FFFFFF` / `#F7F9FB`**: limpieza y orden — fondos y tarjetas.
- **Coral `#FF6F59`**: acento cálido — botones de acción principal (CTA).
- **Verde `#27AE60`** / **Rojo `#C0392B`**: estados de éxito / error o disponibilidad.
