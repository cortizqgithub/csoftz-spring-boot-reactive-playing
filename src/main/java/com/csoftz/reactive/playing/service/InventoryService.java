package com.csoftz.reactive.playing.service;

import org.springframework.stereotype.Service;

import com.csoftz.reactive.playing.domain.commerce.Cart;
import com.csoftz.reactive.playing.domain.commerce.CartItem;
import com.csoftz.reactive.playing.domain.commerce.Item;
import com.csoftz.reactive.playing.repository.CartRepository;
import com.csoftz.reactive.playing.repository.ItemRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InventoryService {
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public InventoryService(ItemRepository itemRepository, CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    public Mono<Cart> addItemToCart(String cartId, String itemId) {
        return this.cartRepository
            .findById(cartId)
            .defaultIfEmpty(new Cart(cartId))
            .flatMap(
                cart ->
                    cart.getCartItems()
                        .stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                        .findAny()
                        .map(
                            cartItem -> {
                                cartItem.increment();
                                return Mono.just(cart);
                            })
                        .orElseGet(
                            () ->
                                this.itemRepository
                                    .findById(itemId)
                                    .map(CartItem::new)
                                    .doOnNext(cartItem -> cart.getCartItems().add(cartItem))
                                    .map(cartItem -> cart)))
            .flatMap(this.cartRepository::save);
    }

    public Flux<Item> getInventory() {
        return this.itemRepository.findAll();
    }

    public Mono<Cart> getCart(String cartId) {
        return this.cartRepository.findById(cartId);
    }
}
