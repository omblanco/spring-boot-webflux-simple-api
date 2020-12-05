# Spring Boot WebFlux Simple Api
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

```java
2020-12-05 20:26:27.766 DEBUG 9292 --- [oundedElastic-1] c.o.s.webflux.api.app.aop.LoggingAspect  : Request uuid: 4d4bec76-430e-4125-b2db-b0b28c100583 -> Enter: com.omblanco.springboot.webflux.api.commons.web.controllers.CommonController.get() with argument[s] = [1]
2020-12-05 20:26:27.767 DEBUG 9292 --- [oundedElastic-1] c.o.s.webflux.api.app.aop.LoggingAspect  : Request uuid: 841b8d37-5484-4e5a-80f7-710bbe58d690 -> Enter: com.omblanco.springboot.webflux.api.commons.services.CommonServiceImpl.findById() with argument[s] = [1]
2020-12-05 20:26:27.769  INFO 9292 --- [oundedElastic-1] c.o.s.w.api.app.aop.ProfilingAspect      : StopWatch 'Profiling': running time = 1073100 ns
---------------------------------------------
ns         %     Task name
---------------------------------------------
001073100  100 %  execution(CommonServiceImpl.findById(..))

2020-12-05 20:26:27.771 DEBUG 9292 --- [oundedElastic-2] c.o.s.webflux.api.app.aop.LoggingAspect  : Request uuid: f0391e74-44b1-4a59-aa33-4db18944b7a2 -> Enter: org.springframework.data.repository.CrudRepository.findById() with argument[s] = [1]
Hibernate: 
    select
        user0_.id as id1_0_0_,
        user0_.birthdate as birthdat2_0_0_,
        user0_.email as email3_0_0_,
        user0_.name as name4_0_0_,
        user0_.password as password5_0_0_,
        user0_.surname as surname6_0_0_ 
    from
        users user0_ 
    where
        user0_.id=?
2020-12-05 20:26:27.785 DEBUG 9292 --- [oundedElastic-2] c.o.s.webflux.api.app.aop.LoggingAspect  : Request uuid: f0391e74-44b1-4a59-aa33-4db18944b7a2 -> Exit: org.springframework.data.repository.CrudRepository.findById() with result = Optional[User(id=1, name=John, surname=Doe, email=john@mail.com, birthdate=2011-12-18 13:17:17.0, password=$2a$10$vUE9JNc3ZflWL6u4HFH2kOEHWgNIahyAxoUoaZ1g0rsHJ3y9kzhwy)]
2020-12-05 20:26:27.785 DEBUG 9292 --- [oundedElastic-2] c.o.s.webflux.api.app.aop.LoggingAspect  : Request uuid: 841b8d37-5484-4e5a-80f7-710bbe58d690 -> Exit: com.omblanco.springboot.webflux.api.commons.services.CommonServiceImpl.findById() with result = UserDTO(name=John, surname=Doe, email=john@mail.com, birthdate=2011-12-18 13:17:17.0, password=$2a$10$vUE9JNc3ZflWL6u4HFH2kOEHWgNIahyAxoUoaZ1g0rsHJ3y9kzhwy)
2020-12-05 20:26:27.786 DEBUG 9292 --- [oundedElastic-2] c.o.s.webflux.api.app.aop.LoggingAspect  : Request uuid: 4d4bec76-430e-4125-b2db-b0b28c100583 -> Exit: com.omblanco.springboot.webflux.api.commons.web.controllers.CommonController.get() with result = <200 OK OK,UserDTO(name=John, surname=Doe, email=john@mail.com, birthdate=2011-12-18 13:17:17.0, password=$2a$10$vUE9JNc3ZflWL6u4HFH2kOEHWgNIahyAxoUoaZ1g0rsHJ3y9kzhwy),[Content-Type:"application/json"]>
```

### Carga de configuración según entorno

La carga de ficheros de configuración por entorno se realiza usando Spring profiles:
- Sin perfil: carga el fichero por defecto para un entorno local.
- dev: el fichero para el entorno de dev.
- stage: fichero para stage.
- pro: profile para la carga de la configuración de producción.

### Base de datos en memoria

Tanto en la versión de MySQL como en la de mongo para el desarrollo y prueba en local se usan bases de datos en memoria con datos de prueba precargados.

### Conversión de modelos

Conversión de los modelos de las distintas capas de la aplicación utilizando Model Mapper.

### Lombok

Utilización de Lombok para generar los getters, setters y constructores y así evitar el código duplicado.

### Documentación

Documentación de los métodos rest utilizando Spring Fox y Swagger. En el despliegue local se habilita en [Swagger](http://localhost:8080/swagger-ui/index.html).

## Módulos
- spring-boot-webflux-simple-api-app: Módulo con la aplicación para MySql. Se divide en los paquetes:
	- aop: definiciones de los pointcut a tracear.
	- configuration: clases de configuración de la aplicación
	- model: paquete con las entidades y repositorios de acceso a datos.
	- service: capa con los servicios para la lógica de negocio.
	- web: paquete con las clases que implementan los endpoints REST (controllers, handlers, dtos, ...).
- spring-boot-webflux-simple-api-mongo-app: réplica del módulo anterior pero versión para mongodb
- spring-boot-webflux-simple-api-commons: módulo con clases comunes para desarrollar microservicios.
- spring-boot-webflux-simple-api-client: cliente reactivo para consumir el api de usuarios expuesta.

## Uso
