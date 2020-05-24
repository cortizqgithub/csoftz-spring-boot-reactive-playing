package com.csoftz.reactive.playing.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.csoftz.reactive.playing.domain.commerce.Item;

import reactor.core.publisher.Flux;

public interface ItemRepository extends ReactiveCrudRepository<Item, String> {
    Flux<Item> findByNameContaining(String partialName);
}
