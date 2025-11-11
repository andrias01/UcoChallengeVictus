package co.edu.uco.backendvictus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co.edu.uco.backendvictus.crosscutting.helpers.LoggerHelper;

@SpringBootApplication(scanBasePackages = "co.edu.uco.backendvictus")
public class BackendVictusApplication {

    public static void main(final String[] args) {
        SpringApplication.run(BackendVictusApplication.class, args);
        LoggerHelper.info(BackendVictusApplication.class, "Backend Victus backend reactivo iniciado correctamente");
    }
}
