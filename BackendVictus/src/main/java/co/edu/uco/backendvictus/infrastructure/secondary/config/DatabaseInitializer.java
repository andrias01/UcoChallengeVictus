package co.edu.uco.backendvictus.infrastructure.secondary.config;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.r2dbc.core.DatabaseClient;

import co.edu.uco.backendvictus.crosscutting.helpers.LoggerHelper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final org.slf4j.Logger LOGGER = LoggerHelper.getLogger(DatabaseInitializer.class);

    private static final List<String> SCHEMA_STATEMENTS = List.of(
            """
                    CREATE TABLE IF NOT EXISTS pais (
                        id UUID PRIMARY KEY,
                        nombre VARCHAR(150) NOT NULL,
                        activo BOOLEAN NOT NULL,
                        CONSTRAINT uq_pais_nombre UNIQUE (nombre)
                    )
                    """,
            """
                    CREATE TABLE IF NOT EXISTS departamento (
                        id UUID PRIMARY KEY,
                        pais_id UUID NOT NULL,
                        nombre VARCHAR(150) NOT NULL,
                        activo BOOLEAN NOT NULL,
                        CONSTRAINT fk_departamento_pais FOREIGN KEY (pais_id) REFERENCES pais(id),
                        CONSTRAINT uq_departamento_nombre UNIQUE (nombre)
                    )
                    """,
            """
                    CREATE TABLE IF NOT EXISTS ciudad (
                        id UUID PRIMARY KEY,
                        departamento_id UUID NOT NULL,
                        nombre VARCHAR(150) NOT NULL,
                        activo BOOLEAN NOT NULL,
                        CONSTRAINT fk_ciudad_departamento FOREIGN KEY (departamento_id) REFERENCES departamento(id),
                        CONSTRAINT uq_ciudad_nombre UNIQUE (nombre)
                    )
                    """,
            """
                    CREATE TABLE IF NOT EXISTS administrador (
                        id UUID PRIMARY KEY,
                        primer_nombre VARCHAR(150) NOT NULL,
                        segundo_nombres VARCHAR(150),
                        primer_apellido VARCHAR(150) NOT NULL,
                        segundo_apellido VARCHAR(150),
                        email VARCHAR(255) NOT NULL,
                        telefono VARCHAR(50),
                        activo BOOLEAN NOT NULL,
                        CONSTRAINT uq_administrador_email UNIQUE (email),
                        CONSTRAINT uq_administrador_telefono UNIQUE (telefono)
                    )
                    """,
            """
                    CREATE TABLE IF NOT EXISTS conjunto_residencial (
                        id UUID PRIMARY KEY,
                        nombre VARCHAR(200) NOT NULL,
                        direccion VARCHAR(255) NOT NULL,
                        ciudad_id UUID NOT NULL,
                        administrador_id UUID NOT NULL,
                        activo BOOLEAN NOT NULL,
                        CONSTRAINT fk_conjunto_ciudad FOREIGN KEY (ciudad_id) REFERENCES ciudad(id),
                        CONSTRAINT fk_conjunto_administrador FOREIGN KEY (administrador_id) REFERENCES administrador(id),
                        CONSTRAINT uq_conjunto_residencial_nombre UNIQUE (nombre)
                    )
                    """);

    private final DatabaseClient databaseClient;

    public DatabaseInitializer(final DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        Flux.fromIterable(SCHEMA_STATEMENTS)
                .concatMap(this::executeStatement)
                .then()
                .doOnSuccess(unused -> LOGGER.info("Esquema de base de datos verificado correctamente"))
                .doOnError(error -> LOGGER.error("Error inicializando la base de datos", error))
                .block();
    }

    private Mono<Long> executeStatement(final String sql) {
        return databaseClient.sql(sql)
                .fetch()
                .rowsUpdated()
                .doOnSubscribe(subscription -> LOGGER.debug("Ejecutando DDL: {}", sql))
                .doOnSuccess(rows -> LOGGER.debug("DDL ejecutado correctamente: {}", sql))
                .onErrorResume(error -> {
                    LOGGER.error("Fallo ejecutando DDL: {}", sql, error);
                    return Mono.error(new IllegalStateException("No fue posible inicializar la base de datos", error));
                });
    }
}
