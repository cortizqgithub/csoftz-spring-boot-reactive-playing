package com.csoftz.reactive.playing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;

import com.csoftz.reactive.playing.domain.commerce.Cart;
import com.csoftz.reactive.playing.service.InventoryService;

import reactor.core.publisher.Mono;

@Controller
public class HomeController {

    private static final String CART_ID = "My Cart";

    private final InventoryService inventoryService;

    public HomeController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public Mono<Rendering> home(Model model) {
        return Mono.just(
            Rendering.view("home.html")
                .modelAttribute("info", "THE INFO")
                .modelAttribute("items", this.inventoryService.getInventory())
                .modelAttribute("cart",
                    this.inventoryService.getCart(CART_ID)
                        .defaultIfEmpty(new Cart(CART_ID)))
                .build());
    }

    @PostMapping("/add/{id}")
    public Mono<String> addToCart(@PathVariable String id) {
        return this.inventoryService
            .addItemToCart(CART_ID, id)
            .thenReturn("redirect:/");
    }
}
