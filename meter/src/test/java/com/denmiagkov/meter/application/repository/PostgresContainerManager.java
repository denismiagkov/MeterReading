package com.denmiagkov.meter.application.repository;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Класс поднимает общий тестконтейнер PostgreSQL для проведения интеграционных тестов для всех репозиториев
 */
@Testcontainers
//@SpringBootTest
public final class PostgresContainerManager {
    private static final int CONTAINER_PORT = 5432;
    private static final int LOCAL_PORT = 5431;

    @Container
    //  @ServiceConnection
    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1")
            .withDatabaseName("meter")
            .withUsername("meter")
            .withPassword("123")
            .withExposedPorts(CONTAINER_PORT)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig()
                            .withPortBindings(new PortBinding(Ports.Binding.bindPort(LOCAL_PORT), new ExposedPort(CONTAINER_PORT)))
            ));


//    @DynamicPropertySource
//    static void registerPgProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url",
//                () -> String.format("jdbc:postgresql://localhost:%d/meter", postgres.getFirstMappedPort()));
//        registry.add("spring.datasource.username", () -> "meter");
//        registry.add("spring.datasource.password", () -> "123");
//    }


    public static void startContainer() {
        postgres.start();
    }

    public static void stopContainer() {
        postgres.stop();
    }
}
