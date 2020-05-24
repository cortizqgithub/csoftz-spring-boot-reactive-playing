package com.csoftz.reactive.playing.controller.api;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import com.csoftz.reactive.playing.domain.Dish;
import com.csoftz.reactive.playing.service.KitchenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ServerController {
  private final KitchenService kitchen;

  @GetMapping(value = "/server", produces = TEXT_EVENT_STREAM_VALUE)
  public Flux<Dish> serveDishes() {
    return this.kitchen
        .getDishes()
        .doOnNext(dish -> System.out.println("Thank you for " + dish + "!"))
        .doOnError(error -> System.out.println("So sorry about " + error.getMessage()))
        .doOnComplete(() -> System.out.println("Thanks for all your hard work!"));
  }

  @GetMapping(value = "/served-dishes", produces = TEXT_EVENT_STREAM_VALUE)
  public Flux<Dish> deliverDishes() {
    return this.kitchen
        .getDishes()
        .map(this::deliver)
        .map(
            d -> {
              Dish newDish = new Dish("CHANGED PLUS ->" + d.getDescription());
              newDish.setDelivered(true);
              return newDish;
            })
        .doOnNext(dish -> System.out.println("Thank you for " + dish + "!"))
        .doOnError(error -> System.out.println("So sorry about " + error.getMessage()))
        .doOnComplete(() -> System.out.println("Thanks for all your hard work!"));
  }

  private Dish deliver(Dish dish) {
    dish.setDelivered(true);
    return dish;
  }
}
