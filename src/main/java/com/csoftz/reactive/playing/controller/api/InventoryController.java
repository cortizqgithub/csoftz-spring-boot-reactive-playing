package com.csoftz.reactive.playing.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csoftz.reactive.playing.domain.commerce.Cart;
import com.csoftz.reactive.playing.domain.commerce.CartItem;
import com.csoftz.reactive.playing.service.InventoryService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private static final String CART_ID = "My Cart";

    private final InventoryService inventoryService;

    @GetMapping("/cart")
    public Mono<Cart> getCart() {
        return this.inventoryService.getCart(CART_ID)
            .defaultIfEmpty(new Cart(CART_ID));
    }

    @GetMapping("/cart/items")
    public Flux<CartItem> getAllCartItems() {
        return this.inventoryService
            .getCart(CART_ID)
            .flatMapMany(cart -> Flux.fromIterable(cart.getCartItems()));
    }

    @GetMapping("cart/items/fix/description")
    public Flux<CartItem> getAllCartItemsDescriptionFixed() {
        return this.inventoryService
            .getCart(CART_ID)
            .flatMapMany(cart -> Flux.fromIterable(cart.getCartItems()))
            .map(
                cartItem -> {
                    if (cartItem.getItem().getDescription() == null) {
                        cartItem.getItem().setDescription(" Set to diff");
                    }
                    return cartItem;
                    // At this point the cartItem is not saved.
                    // This is only to showcase on the fly modification.
                });
    }
}
