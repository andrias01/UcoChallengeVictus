package co.edu.uco.backendvictus.infrastructure.secondary.config;

import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import io.r2dbc.spi.ConnectionFactory;

@Configuration
public class R2dbcConfig {

    @Bean
    public ConnectionFactory connectionFactory(final R2dbcProperties properties) {
        return ConnectionFactoryBuilder.withUrl(properties.getUrl())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();
    }

    @Bean
    public DatabaseClient databaseClient(final ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }
}
