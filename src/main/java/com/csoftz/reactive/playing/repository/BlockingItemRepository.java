package com.csoftz.reactive.playing.repository;

import com.csoftz.reactive.playing.domain.commerce.Item;
import org.springframework.data.repository.CrudRepository;

public interface BlockingItemRepository extends CrudRepository<Item, String> {}
