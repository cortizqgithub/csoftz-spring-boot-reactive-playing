package com.csoftz.reactive.playing.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.csoftz.reactive.playing.domain.commerce.Cart;
import com.csoftz.reactive.playing.domain.commerce.Item;
import com.csoftz.reactive.playing.service.InventoryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(HomeController.class)
public class HomeControllerSliceTest {
    private static final String CART_ID = "My Cart";
    @Autowired
    private WebTestClient client;

    @MockBean
    InventoryService inventoryService;

    @Test
    void shouldLoadHomePage() {
        when(inventoryService.getInventory()).thenReturn(Flux.just(
            new Item("id1", "name1", "desc1", 1.99),
            new Item("id2", "name2", "desc2", 9.99)
        ));
        when(inventoryService.getCart(CART_ID)) //
            .thenReturn(Mono.just(new Cart(CART_ID)));

        client
            .get()
            .uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .consumeWith(exchangeResult -> {
                assertThat(exchangeResult.getResponseBody()).contains("action=\"/add/id1\"");
                assertThat(exchangeResult.getResponseBody()).contains("action=\"/add/id2\"");

                verify(inventoryService, times(1)).getCart(anyString());
            });
    }

}
