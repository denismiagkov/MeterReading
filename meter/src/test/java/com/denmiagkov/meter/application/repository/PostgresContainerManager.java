package com.denmiagkov.meter.application.repository;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import lombok.experimental.UtilityClass;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Класс поднимает общий тестконтейнер PostgreSQL для проведения интеграционных тестов для всех репозиториев
 */
//@Testcontainers
@UtilityClass
public final class PostgresContainerManager {
    private static final int CONTAINER_PORT = 5432;
    private static final int LOCAL_PORT = 5431;

    @Container
    private final static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.1")
            .withDatabaseName("meter")
            .withUsername("meter")
            .withPassword("123")
            .withExposedPorts(CONTAINER_PORT)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig()
                            .withPortBindings(new PortBinding(Ports.Binding.bindPort(LOCAL_PORT), new ExposedPort(CONTAINER_PORT)))
            ));

    public static void startContainer() {
        postgreSQLContainer.start();
    }

    public static void stopContainer() {
        postgreSQLContainer.stop();
    }
}
