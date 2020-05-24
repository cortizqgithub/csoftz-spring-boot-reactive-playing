package com.csoftz.reactive.playing.service;

import com.csoftz.reactive.playing.domain.Dish;
import reactor.core.publisher.Flux;

public interface KitchenService {
  Flux<Dish> getDishes();
}
