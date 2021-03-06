package com.csoftz.reactive.playing.service;

import org.springframework.stereotype.Service;

import com.csoftz.reactive.playing.domain.commerce.Cart;
import com.csoftz.reactive.playing.domain.commerce.CartItem;
import com.csoftz.reactive.playing.domain.commerce.Item;
import com.csoftz.reactive.playing.repository.CartRepository;
import com.csoftz.reactive.playing.repository.ItemRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * aba
 *
 * @author Carlos Adolfo Ortiz Q.
 */
@Service
public class CartService {
    private static final int JOSHUA_AGE = 10;

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    CartService(ItemRepository itemRepository, CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    public Flux<Item> findAll() {
        return this.itemRepository.findAll();
    }

    public Mono<Cart> findById(String id) {
        return this.cartRepository.findById(id).defaultIfEmpty(new Cart(id));
    }

    /**
     * This is the cart.
     *
     * @param cartId Identifier of the Cart to add the item to.
     * @param id     Item identifier to use.
     * @return A cart with items.
     */
    public Mono<Cart> addToCart(String cartId, String id) {
        return this.cartRepository
            .findById(cartId)
            .defaultIfEmpty(new Cart(cartId))
            .flatMap(
                cart ->
                    cart.getCartItems()
                        .stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(id))
                        .findAny()
                        .map(
                            cartItem -> {
                                cartItem.increment();
                                return Mono.just(cart);
                            })
                        .orElseGet(
                            () ->
                                this.itemRepository
                                    .findById(id)
                                    .map(CartItem::new)
                                    .doOnNext(cartItem -> cart.getCartItems().add(cartItem))
                                    .map(cartItem -> cart)))
            .flatMap(this.cartRepository::save);
    }
}
