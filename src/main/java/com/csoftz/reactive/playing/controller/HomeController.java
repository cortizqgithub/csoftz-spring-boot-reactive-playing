package com.csoftz.reactive.playing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;

import com.csoftz.reactive.playing.domain.commerce.Cart;
import com.csoftz.reactive.playing.service.InventoryService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class HomeController {

    private static final String CART_ID = "My Cart";

    private final InventoryService inventoryService;

    public HomeController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public Mono<Rendering> home(Model model) {
        log.trace("THIS IS HOME entry point!");
        return Mono.just(
            Rendering.view("home.html")
                .modelAttribute("info", "THE INFO")
                .modelAttribute("items", this.inventoryService.getInventory())
                .modelAttribute("cart",
                    this.inventoryService.getCart(CART_ID)
                        .defaultIfEmpty(new Cart(CART_ID)))
                .build())
            .log();
    }

    @PostMapping("/add/{id}")
    public Mono<String> addToCart(@PathVariable String id) {
        log.trace("THIS IS HOME entry point! Adding an item {}", id);
        return this.inventoryService
            .addItemToCart(CART_ID, id)
            .thenReturn("redirect:/");
    }
}
