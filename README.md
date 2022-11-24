# IUD Library App:

### _Elaborado por: Carlos Cardona, Estefanía Jiménez, Mateo Valencia_

---

La biblioteca de una institución educativa, requiere gestionar su material bibliográfico (libros) y su respectivo préstamo para la comunidad académica. Todos los libros, poseen un título, un código ISBN, el número de páginas, la editorial y pueden estar en formato CD/DVD o impreso. Cada libro, pertenece a una categoría específica (matemática, ingeniería, ciencias sociales, entre otras). De cada libro, pueden existir varios ejemplares, cada ejemplar, tiene una identificación y un número de edición. La biblioteca, desea realizar búsquedas de libros por categoría, formato, autor o editorial, teniendo en cuenta que un libro puede ser escrito por varios autores, tratar varios temas y solo pertenecer a una editorial.

La comunidad académica, puede prestar ejemplares, considerando que, un profesor, puede solicitar una cantidad ilimitada de libros, pero, un estudiante, solo puede prestar, como máximo, cinco (5) libros. En todo caso, al momento de realizar el préstamo, se debe registrar la fecha del préstamo y la fecha de retorno del libro (el sistema debe calcular, automáticamente, esta fecha, que por defecto, corresponde a ocho (8) días y no se permiten renovaciones de préstamo) y una nota sobre el estado físico del libro. Se debe tener en cuenta que si un usuario (profesor o estudiante) no retorna un libro prestado antes de la fecha de retorno, tendrá inhabilitada la posibilidad de préstamo durante ocho (8) días por cada libro sin retornar.

La biblioteca, necesita conocer información general sobre los usuarios que prestan los libros, de los cuales, se requiere conocer el nombre completo, la dirección, el número de cédula, los teléfonos de contacto, el tipo de usuario (profesor o estudiante), si está habilitado o no para realizar préstamos y visualizar el histórico de préstamos por mes.

Adicionalmente, se debe contar con un usuario administrador que permite agregar, actualizar o eliminar tanto libros como usuarios.

## Pasos a seguir

- Clonar este repositorio
- Abrir el proyecto en su IDE de preferencia.
- Asignar el usuario y la contraseña de tu base de datos en el archivo _application.properties_
  Como se muestra a continuación:
```sh
spring.datasource.username=usuario
spring.datasource.password= ${contraseña}
```
- Crear un schema en MySQL Workbench o cualquier otro gestor de bases de datos llamado _**library**_
- Correr el proyecto

Debes ver en la consola lo siguiente:
```sh
2022-07-29 12:12:09.756  INFO 12524 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
```

## Documentación API

```sh
http://localhost:8080/swagger-ui/index.html
```
