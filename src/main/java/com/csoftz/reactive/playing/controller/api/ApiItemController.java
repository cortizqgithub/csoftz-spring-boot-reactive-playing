package com.csoftz.reactive.playing.controller.api;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csoftz.reactive.playing.domain.commerce.Item;
import com.csoftz.reactive.playing.repository.ItemRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ApiItemController {
    private final ItemRepository repository;

    @GetMapping()
    Flux<Item> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    Mono<Item> findOne(@PathVariable String id) {
        return this.repository.findById(id);
    }

    @PostMapping()
    public Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item) {
        return item.flatMap(this.repository::save)
            .map(
                savedItem ->
                    ResponseEntity.created(URI.create("/api/v1/items/" + savedItem.getId()))
                        .body(savedItem));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<?>> updateItem(@RequestBody Mono<Item> item, @PathVariable String id) {

        return item.map(
            content ->
                new Item(id, content.getName(), content.getDescription(), content.getPrice()))
            .flatMap(this.repository::save)
            .thenReturn(ResponseEntity.created(URI.create("/api/items/" + id)).build());
    }
}
