package com.csoftz.reactive.playing.controller.api;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.csoftz.reactive.playing.domain.commerce.Item;
import com.csoftz.reactive.playing.repository.ItemRepository;
import com.csoftz.reactive.playing.service.InventoryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = AffordancesItemController.class)
@AutoConfigureRestDocs
public class AffordancesItemControllerDocumentationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private InventoryService service;

    @MockBean
    private ItemRepository repository;

    @Test
    void findSingleItemAffordances() {
        when(repository.findById("item-1"))
            .thenReturn(
                Mono.just(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99)));

        this.webTestClient
            .get()
            .uri("/affordances/items/item-1")
            .accept(MediaTypes.HAL_FORMS_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(document("single-item-affordances", preprocessResponse(prettyPrint())));
    }

    @Test
    void findAggregateRootItemAffordances() {
        when(repository.findAll())
            .thenReturn(Flux.just(new Item("Alf alarm clock", "nothing I really need", 19.99)));
        when(repository.findById((String) null))
            .thenReturn(
                Mono.just(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99)));

        this.webTestClient
            .get()
            .uri("/affordances/items")
            .accept(MediaTypes.HAL_FORMS_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(document("aggregate-root-affordances", preprocessResponse(prettyPrint())));
    }
}
