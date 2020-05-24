package com.csoftz.reactive.playing.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.csoftz.reactive.playing.domain.commerce.Cart;

public interface CartRepository extends ReactiveCrudRepository<Cart, String> {
}
