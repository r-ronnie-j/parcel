package fun.example.parcel.service;

import fun.example.parcel.db.dto.UserDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = UserServiceIntegrationTest.Initializer.class)
public class UserServiceIntegrationTest {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testes")
            .withUsername("tester")
            .withPassword("test pass");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + mySQLContainer.getUsername(),
                    "spring.datasource.password=" + mySQLContainer.getPassword(),
                    "spring.jpa.hibernate.ddl-auto=update"
            ).applyTo(applicationContext);
        }
    }


    private final UserService userService;

    @Autowired
    UserServiceIntegrationTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void testAddTwoUsersAndGetAllUsers() {
        UserService.SignupRequest spot = new UserService.SignupRequest("Spot", "spot@gmail.com");

        userService.signup(spot);

        List<UserDto> allUsers = userService.getAllUsers();

        assertThat(allUsers).hasSize(1);
        assertThat(allUsers)
                .extracting(UserDto::getEmail)
                .contains("spot@gmail.com");
    }
}