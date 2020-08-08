package com.csoftz.reactive.playing.rsocketserver.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.csoftz.reactive.playing.rsocketserver.domain.Item;

public interface ItemRepository extends ReactiveCrudRepository<Item, String> {
}
