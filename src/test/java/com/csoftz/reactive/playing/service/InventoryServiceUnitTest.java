package com.csoftz.reactive.playing.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.csoftz.reactive.playing.domain.commerce.Cart;
import com.csoftz.reactive.playing.domain.commerce.CartItem;
import com.csoftz.reactive.playing.domain.commerce.Item;
import com.csoftz.reactive.playing.repository.CartRepository;
import com.csoftz.reactive.playing.repository.ItemRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class InventoryServiceUnitTest {
    private static final String CART_ID = "My Cart";
    private static final String ITEM_ID = "item1";
    private static final String ITEM_NAME = "TV tray";
    private static final String ITEM_DESCRIPTION = "Alf TV tray";
    private static final double ITEM_PRICE = 19.99;

    private InventoryService inventoryService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        // Define test data
        Item sampleItem = new Item(ITEM_ID, ITEM_NAME, ITEM_DESCRIPTION, ITEM_PRICE);

        CartItem sampleCartItem = new CartItem(sampleItem);
        Cart sampleCart = new Cart(CART_ID, Collections.singletonList(sampleCartItem));

        // Define mock interactions provided
        // by your collaborators
        when(cartRepository.findById(anyString())).thenReturn(Mono.empty());
        when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));

        inventoryService = new InventoryService(itemRepository, cartRepository);
    }

    @Test
    void givenItemSavedInEmptyCartShouldProduceOneCartItem() {
        inventoryService
            .addItemToCart(CART_ID, ITEM_ID)
            .as(StepVerifier::create)
            .expectNextMatches(
                cart -> {
                    assertThat(cart.getCartItems())
                        .extracting(CartItem::getQuantity)
                        .containsExactlyInAnyOrder(1);

                    assertThat(cart.getCartItems())
                        .extracting(CartItem::getItem)
                        .containsExactly(new Item(ITEM_ID, ITEM_NAME, ITEM_DESCRIPTION, ITEM_PRICE));

                    return true;
                })
            .verifyComplete();
    }
}