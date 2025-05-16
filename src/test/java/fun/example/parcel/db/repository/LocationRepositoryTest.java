package fun.example.parcel.db.repository;

import fun.example.parcel.db.entity.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = LocationRepositoryTest.Initializer.class)
class LocationRepositoryTest {

    final Logger log = LoggerFactory.getLogger(LocationRepositoryTest.class);

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("tests")
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

    private final LocationRepository locationRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    @Autowired
    LocationRepositoryTest(
            LocationRepository locationRepository,
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.productRepository = productRepository;
    }

    private LocationEntity location1;
    private LocationEntity location2;
    private LocationEntity location3;


    @BeforeEach
    void setUp() {
        UserEntity mockUser = UserEntity
                .builder()
                .email("tom@gmail.com")
                .name("tom")
                .build();

        userRepository.save(mockUser);

        location1 = LocationEntity
                .builder()
                .email("location1@gmail.com")
                .name("location1")
                .description("Location 1 here")
                .build();

        locationRepository.save(location1);


        location2 = LocationEntity
                .builder()
                .email("location2@gmail.com")
                .name("location2")
                .description("Location 2 here")
                .build();

        locationRepository.save(location2);


        location3 = LocationEntity
                .builder()
                .email("location3@gmail.com")
                .name("location3")
                .description("Location 3 here")
                .build();
        locationRepository.save(location3);


        ProductEntity product = ProductEntity
                .builder()
                .user(mockUser)
                .name("Mock Product 1")
                .description("This is mock product")
                .price(12.22)
                .transits(new ArrayList<>())
                .build();

        final List<TransitEntity> transits = Stream.of(location1, location2, location3).map(l -> {
                    TransitEntity transitEntity = TransitEntity
                            .builder()
                            .location(l)
                            .product(product)
                            .build();
                    product.getTransits().add(transitEntity);
                    return transitEntity;
                }
        ).toList();

        transits.get(0).setProductStatus(List.of(
                ProductStatusEntity
                        .builder()
                        .transit(transits.get(0))
                        .status(DeliveryStatus.Out_For_Delivery)
                        .build()
        ));

        transits.get(0).setProductStatus(List.of(
                ProductStatusEntity
                        .builder()
                        .transit(transits.get(0))
                        .status(DeliveryStatus.Picked_Up)
                        .build()
        ));

        transits.get(1).setProductStatus(List.of(
                ProductStatusEntity
                        .builder()
                        .transit(transits.get(1))
                        .status(DeliveryStatus.Out_For_Delivery)
                        .build()
        ));

        productRepository.save(product);
    }


    @Test
    void findProductStatusAtGivenLocationAndTestTheOutcome() {
        //when
        final List<ProductEntity> productsAtLocation1 = locationRepository.findProductsAtGivenLocation(location1.getId());
        final List<ProductEntity> productsAtLocation2 = locationRepository.findProductsAtGivenLocation(location2.getId());
        final List<ProductEntity> productsAtLocation3 = locationRepository.findProductsAtGivenLocation(location3.getId());

        log.error("The size is productsAtLocation1" + productsAtLocation1.size());
        log.error("The size is productsAtLocation1" + productsAtLocation2.size());
        log.error("The size is productsAtLocation1" + productsAtLocation3.size());

        //then
        assertEquals(0, productsAtLocation1.size());
        assertEquals(1, productsAtLocation2.size());
        assertEquals(0, productsAtLocation3.size());
    }
}
