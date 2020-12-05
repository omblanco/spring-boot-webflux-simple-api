# spring-boot-webflux-simple-api
Spring Boot &amp; WebFlux Simple Api


## Índice
- [Descripción](#descripción)
- [Componentes](#componentes)
- [Características](#características)
- [Módulos](#módulos)
- [Uso](#uso)

## Descripción

Api rest de ejemplo para la creación de microservicios reactivos con Spring Boot y Spring WebFlux tanto con una base de datos MySQL como Mongo. Contiene ejemplos tanto de definición de controladores con Spring MVC como con Endpoints Funcionales ([Functional Endpoints](https://spring.getdocs.org/en-US/spring-framework-docs/docs/spring-web-reactive/webflux/webflux-fn.html)). Implementa características como la securización, trazabilidad de capas, bbdd en memoria, documentación del endpoint … todas ellas activables mediante perfiles de Spring.

## Componentes
- [Spring Boot 2.3.5](https://spring.io/projects/spring-boot)
- [Spring WebFlux](https://spring.io/projects/spring-framework)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Data Mongodb Reactive](https://spring.io/projects/spring-data-mongodb#overview)
- [Spring Fox (Swagger)](https://springfox.github.io/springfox/)
- [Spring AOP](https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/core.html#aop-api-pointcuts)
- [Lombok](https://projectlombok.org/)
- [Model Mapper](http://modelmapper.org/)
- [JUnit 5](https://junit.org/junit5/)

## Características

### Autenticación

Autenticación implementada con Spring Security y tokens de acceso jwt. La securización es activable mediante el perfil  “security”.

### Clases genéricas

Implementaciones de clases genéricas de Servicios y Controladores para los métodos CRUD.

### Controladores

Ejemplos de implementación de controladores con las anotaciones @Controller @RestController y Functional Endpoints.

### Trazabilidad y log

Mediante AOP se habilita el log para las capas de la aplicación mostrando los parámetros de entrada y salida. En la trazabilidad se muestra el tiempo de ejecución de un método en nanosegundos. Tanto el nivel del log como el deshabilitado son esta característica son configurables. En el caso de la activación/desactivación se realiza mediante el perfil “profiling”.

### Carga de configuración según entorno

La carga de ficheros de configuración por entorno se realiza usando Spring profiles:
- Sin perfil: carga el fichero por defecto para un entorno local.
- dev: el fichero para el entorno de dev.
- stage: fichero para stage.
- pro: profile para la carga de la configuración de producción.

### Base de datos en memoria

Tanto en la versión de MySQL como en la de mongo para el desarrollo y prueba en local se usan bases de datos en memoria con datos de prueba precargados.

### Documentación

Documentación de los métodos rest utilizando Spring Fox y Swagger. En el despliegue local se habilita en [Swagger](http://localhost:8080/swagger-ui/index.html).

## Módulos

## Uso
