package com.csoftz.reactive.playing.repository;

import com.csoftz.reactive.playing.domain.commerce.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CartRepository extends ReactiveCrudRepository<Cart, String> {}
