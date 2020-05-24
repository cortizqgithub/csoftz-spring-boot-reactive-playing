package com.csoftz.reactive.playing.controller;

import com.csoftz.reactive.playing.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

  private final CartService cartService;

  public HomeController(CartService cartService) {
    this.cartService = cartService;
  }

  @GetMapping
  public Mono<Rendering> home(Model model) {
    return Mono.just(
        Rendering.view("home.html")
            .modelAttribute("items", this.cartService.findAll().doOnNext(System.out::println))
            .modelAttribute("cart", this.cartService.findById("My Cart"))
            .build());
  }

  @PostMapping("/add/{id}")
  public Mono<String> addToCart(@PathVariable String id) {
    return this.cartService.addToCart("My Cart", id).thenReturn("redirect:/");
  }
}
