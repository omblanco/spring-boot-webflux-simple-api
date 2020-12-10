# Spring Boot WebFlux Simple Api
Spring Boot &amp; WebFlux Simple Api


## Índice
- [Descripción](#descripción)
- [Componentes](#componentes)
- [Características](#características)
- [Módulos](#módulos)
- [Perfiles](#perfiles)
- [Uso](#uso)
	- [Requisitos](#requisitos)
	- [Generación de artefactos](#generación-de-artefactos)
	- [Lanzar la aplicación](#lanzar-la-aplicación)
	- [Utilización del client](#utilización-del-client)

## Descripción

Api rest de ejemplo para la creación de microservicios reactivos con Spring Boot y Spring WebFlux tanto con una base de datos MySQL como Mongo. Contiene ejemplos tanto de definición de controladores con Spring MVC como con Endpoints Funcionales ([Functional Endpoints](https://spring.getdocs.org/en-US/spring-framework-docs/docs/spring-web-reactive/webflux/webflux-fn.html)). Implementa características como la securización, trazabilidad de capas, bbdd en memoria, documentación del endpoint … todas ellas activables mediante perfiles de Spring.

## Componentes
- [Spring Boot 2.4.0](https://spring.io/projects/spring-boot)
- [Spring WebFlux](https://spring.io/projects/spring-framework)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Data Mongo DB Reactive](https://spring.io/projects/spring-data-mongodb#overview)
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

## Perfiles

La aplicación hace uso de los “Spring Profiles” con dos objetivos, la carga de la configuración para los distintos entornos y para la activación y desactivación de funcionalidades.

### Perfiles de entornos

- Sin perfil: carga el fichero application.properties para el desarrollo en local.
- dev: carga el fichero application-dev.properties para el despliegue en entorno de desarrollo.
- stage: carga el fichero application-stage.properties para el despliegue en entorno stage.
- pro: carga el fichero application-pro.properties para el despliegue en entorno producción

### Perfiles de funcionalidades

- profiling: activa el log para tracear las entradas y salidas de los métodos de las capas de la aplicación y su tiempo de ejecución.
- security: activa la securización de la api mediante token.
- swagger: habilita la página de documentación de la api usando swagger.
- openapi: habilita la página de documentación de la api usando Open Api.

## Uso

### Requisitos

- [Java JDK 11](https://www.oracle.com/es/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org/)

### Generación de artefactos

#### Alternativa Maven

En el directorio raíz del proyecto lanzamos:

```
mvn clean package
```

Resultado:
```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO]
[INFO] spring-boot-webflux-simple-api ..................... SUCCESS [  0.318 s]
[INFO] spring-boot-webflux-simple-api-commons ............. SUCCESS [  4.531 s]
[INFO] spring-boot-webflux-simple-api-app ................. SUCCESS [ 21.973 s]
[INFO] spring-boot-webflux-simple-api-mongo-app ........... SUCCESS [  5.121 s]
[INFO] spring-boot-webflux-simple-api-client .............. SUCCESS [  0.299 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 33.214 s
[INFO] Finished at: 2020-12-05T21:03:43+01:00
[INFO] Final Memory: 83M/284M
[INFO] ------------------------------------------------------------------------
```

Para omitir los tests:
```
mvn clean package -DskipTests=true
```

#### Alternativa sin Maven

En el directorio raíz del proyecto se hace uso del comando mvnw tanto para windows como para unix

```
mvnw clean package
```

Alternativa para no lanzar los tests
```
mvnw clean package -DskipTests=true

```

### Lanzar la aplicación

#### Alternativa Maven

Moverse al directorio:

```
spring-boot-webflux-simple-api-app\target
```

o 

```
spring-boot-webflux-simple-api-mongo-app\target
```

Y ejecutamos el fichero jar según la aplicación:

```
java -jar spring-boot-webflux-simple-api-app-x.x.x.jar
java -jar spring-boot-webflux-simple-api-mongo-app-x.x.x.jar
```

Para lanzar la aplicación con perfiles:
```
java -jar -Dspring.profiles.active=profiling,dev,security spring-boot-webflux-simple-api-mongo-app-x.x.x.jar
```

#### Alternativa sin Maven

Lanzar las aplicaciones desde el directorio raíz:

```
mvnw -pl spring-boot-webflux-simple-api-app -am spring-boot:run
```

o para mongo:

```
mvnw -pl spring-boot-webflux-simple-api-mongo-app -am spring-boot:run
```

Y añadiendo profiles:

```
mvnw -pl spring-boot-webflux-simple-api-app -am spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=profiling,security"
mvnw -pl spring-boot-webflux-simple-api-mongo-app -am spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=profiling,security"
```

### Utilización del client

Incluir la dependencia en el proyecto
```
<dependency>
	<groupId>com.omblanco.springboot.webflux.api</groupId>
	<artifactId>spring-boot-webflux-simple-api-client</artifactId>
	<version>1.6.0</version>
</dependency>
```

#### Ejemplos

Recuperar todos los usuarios con MySQL y MongoDB:

```java
String email = "john@mail.com";
String password = "1234";
String endpointMySQL = "http://localhost:8080";
String endpointMongoDB = "http://localhost:8081";

LOGGER.info("MySQL users");
ReactiveUsersClientBuilder.build(email, password, endpointMySQL).getAllUsers().subscribe(user -> LOGGER.info("{}", user));

LOGGER.info("Mongo users");
ReactiveUsersClientBuilder.build(email, password, endpointMongoDB).getAllUsers().subscribe(user -> LOGGER.info("{}", user));

LOGGER.info("MySQL user client");
ReactiveUsersClientBuilder<Long> builderWithLong = new ReactiveUsersClientBuilder<Long>();
builderWithLong.setUser(email);
builderWithLong.setPassword(password);
builderWithLong.setEndpoint(endpointMySQL);
ReactiveUsersClient<Long> userClientWithLong = builderWithLong.build();
Flux<UserDTO<Long>> userFluxWithLong = userClientWithLong.getAllUsers();
userFluxWithLong.subscribe(user -> LOGGER.info("{}", user));

LOGGER.info("Mongo user client");
ReactiveUsersClient<String> userClientWithString = ReactiveUsersClientBuilder.build(email, password, endpointMongoDB);
Flux<UserDTO<String>> userFluxWithString = userClientWithString.getAllUsers();
userFluxWithString.subscribe(user -> LOGGER.info("{}", user));
```

Filtrado de usuarios:
```java
String email = "john@mail.com";
String password = "1234";
String endpointMySQL = "http://localhost:8080";
String endpointMongoDB = "http://localhost:8081";

LOGGER.info("MySQL users");

UserFilterDTO filter = new UserFilterDTO();
filter.setName("oscar");
Sort sort = null;
ReactiveUsersClientBuilder.build(email, password, endpointMySQL).getUsers(filter, sort).subscribe(user -> LOGGER.info("{}", user));
```

Paginación y ordenación:
```java
String email = "john@mail.com";
String password = "1234";
String endpointMySQL = "http://localhost:8080";
String endpointMongoDB = "http://localhost:8081";

LOGGER.info("MongoDB users");

UserFilterDTO filter = new UserFilterDTO();
filter.setName("oscar");
List<Order> orders = new ArrayList<Order>();

orders.add(new Order(Direction.DESC, "name"));
Sort sort = Sort.by(orders);
Pageable pageable = PageRequest.of(0, 5, sort);
ReactiveUsersClientBuilder.build(email, password, endpointMongoDB).getUsers(filter, pageable).subscribe(page -> LOGGER.info("{}", page.getContent()));
```

Guardado de usuario:
```java
String email = "john@mail.com";
String password = "1234";
String endpointMySQL = "http://localhost:8080";
String endpointMongoDB = "http://localhost:8081";

UserDTO<Long> user = new UserDTO<Long>();
user.setName("fooo");
user.setSurname("surname");
user.setEmail("foooo@mail.com");
user.setPassword("12345678");
user.setBirthdate(new Date());

LOGGER.info("MySQL users");
ReactiveUsersClient<Long> userClient = ReactiveUsersClientBuilder.build(email, password, endpointMySQL);
userClient.save(user).subscribe(newUser -> LOGGER.info("New user: {}", newUser));
```

Guardado con errores:
```java
String email = "john@mail.com";
String password = "1234";
String endpointMySQL = "http://localhost:8080";
String endpointMongoDB = "http://localhost:8081";

LOGGER.info("MySQL users");

ReactiveUsersClient<Long> userClient = ReactiveUsersClientBuilder.build(email, password, endpointMySQL);

UserDTO<Long> userWithErrors = new UserDTO<Long>();
userWithErrors.setName("o");
userWithErrors.setEmail("eoa");
userClient.save(userWithErrors).subscribe(newUser -> LOGGER.info("New user: {}", newUser), e -> {
    if (e instanceof WebClientResponseException) {
        WebClientResponseException clientError = (WebClientResponseException)e;
        if (clientError.getRawStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY.value()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                ValidationErrorsResponse result = mapper.readValue(((WebClientResponseException) e).getResponseBodyAsString(),
                        ValidationErrorsResponse.class);
                LOGGER.error(result.getCode());
                LOGGER.error(result.getMessage());
                
                for (ValidationError validationError : result.getErrors()) {
                    LOGGER.error(validationError.getPath());
                    LOGGER.error(validationError.getCode());
                    LOGGER.error(validationError.getMessage());
                }
                
            } catch (Exception ex) {
                LOGGER.error("Error getting response", ex);
            }
        } else {
            LOGGER.error("Another type of error", clientError);
        }
    } else {
        LOGGER.error("Error on request", e);
    }
}); 
```
