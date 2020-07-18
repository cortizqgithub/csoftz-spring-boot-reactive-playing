package com.csoftz.reactive.playing;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.csoftz.reactive.playing.domain.commerce.Item;
import com.csoftz.reactive.playing.repository.ItemRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
@ContextConfiguration
@Slf4j
public class RabbitTest {

    private static final String AMPQ_ITEMS_URI = "/api/v1/amqp/items";
    private static final String ITEM_ALF_ALARM_CLOCK = "Alf alarm clock";
    private static final String ITEM_SMURF_TV_TRAY = "Smurf TV tray";
    private static final String ITEM_DESC_NOTHING_IMPORTANT = "nothing important";

    private static final double ITEM_PRICE_19_99 = 19.99;
    private static final double ITEM_PRICE_29_99 = 29.99;

    @Container
    private static RabbitMQContainer container = new RabbitMQContainer();

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ItemRepository repository;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", container::getContainerIpAddress);
        registry.add("spring.rabbitmq.port", container::getAmqpPort);
    }

    @BeforeEach
    void setUp() {
        // This is necessary because project initializes a MongoDB instance.
        // Remember this is study project, this should never hapen in PROD.
        // We use here 'block()' method to force it gets executed immediately
        // so this tests pass.
        // See 'RepositoryDatabaseLoader.java' where MongoDB is initialized
        // to let other endpoints and common flow of application to work
        // seamlessly.
        this.repository.deleteAll().block();
    }

    @Test
    void verifyMessagingThroughAmqp() throws InterruptedException {
        this.webTestClient
            .post()
            .uri(AMPQ_ITEMS_URI)
            .bodyValue(new Item(ITEM_ALF_ALARM_CLOCK, ITEM_DESC_NOTHING_IMPORTANT, ITEM_PRICE_19_99))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody();

        Thread.sleep(1500L);

        this.webTestClient
            .post()
            .uri(AMPQ_ITEMS_URI)
            .bodyValue(new Item(ITEM_SMURF_TV_TRAY, ITEM_DESC_NOTHING_IMPORTANT, ITEM_PRICE_29_99))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody();

        Thread.sleep(2000L);

        this.repository
            .findAll()
            .as(StepVerifier::create)
            .expectNextMatches(
                item -> {
                    log.debug("ITEMTST={}", item);
                    assertThat(item.getName()).isEqualTo(ITEM_ALF_ALARM_CLOCK);
                    assertThat(item.getDescription()).isEqualTo(ITEM_DESC_NOTHING_IMPORTANT);
                    assertThat(item.getPrice()).isEqualTo(ITEM_PRICE_19_99);
                    return true;
                })
            .expectNextMatches(
                item -> {
                    log.debug("ITEMTST={}", item);
                    assertThat(item.getName()).isEqualTo(ITEM_SMURF_TV_TRAY);
                    assertThat(item.getDescription()).isEqualTo(ITEM_DESC_NOTHING_IMPORTANT);
                    assertThat(item.getPrice()).isEqualTo(ITEM_PRICE_29_99);
                    return true;
                })
            .verifyComplete();
    }
}
