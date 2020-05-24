package com.csoftz.reactive.playing.repository;

import org.springframework.data.repository.CrudRepository;

import com.csoftz.reactive.playing.domain.commerce.Item;

public interface BlockingItemRepository extends CrudRepository<Item, String> {
}
