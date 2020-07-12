package com.csoftz.reactive.playing.controller.api;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.csoftz.reactive.playing.domain.commerce.Item;
import com.csoftz.reactive.playing.repository.ItemRepository;
import com.csoftz.reactive.playing.service.InventoryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ApiItemController.class)
@AutoConfigureRestDocs
public class ApiItemControllerDocumentationTest {

    @Autowired
    private WebTestClient webTestClient;



    @MockBean
    ItemRepository repository;

    @Test
    void findingAllItems() {
        when(repository.findAll())
            .thenReturn(Flux.just(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99)));

        this.webTestClient
            .get()
            .uri("/api/v1/items")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(document("findAll", preprocessResponse(prettyPrint())));
    }

    @Test
    void postNewItem() {
        when(repository.save(any()))
            .thenReturn(Mono.just(new Item("1", "Alf alarm clock", "nothing important", 19.99)));

        this.webTestClient
            .post()
            .uri("/api/v1/items")
            .bodyValue(new Item("Alf alarm clock", "nothing important", 19.99))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody()
            .consumeWith(document("post-new-item", preprocessResponse(prettyPrint())));
    }

    @Test
    void findOneItem() {
        when(repository.findById("item-1"))
            .thenReturn(
                Mono.just(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99)));

        this.webTestClient
            .get()
            .uri("/api/v1/items/item-1")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(document("findOne", preprocessResponse(prettyPrint())));
    }
}
