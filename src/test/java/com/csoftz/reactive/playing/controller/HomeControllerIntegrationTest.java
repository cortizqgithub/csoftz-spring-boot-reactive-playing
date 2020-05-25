package com.csoftz.reactive.playing.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.TEXT_HTML;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
class HomeControllerIntegrationTest {
    @Autowired
    WebTestClient client;

    @Test
    void shouldLoadHomePage() {
        client
            .get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(TEXT_HTML)
            .expectBody(String.class)
            .consumeWith(exchangeResult -> {
                assertThat(exchangeResult.getResponseBody())
                    .contains("<input type=\"submit\" value=\"Add to Cart\"/>");
            });
    }
}