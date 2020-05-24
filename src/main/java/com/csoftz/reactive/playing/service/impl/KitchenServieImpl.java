package com.csoftz.reactive.playing.service.impl;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.csoftz.reactive.playing.domain.Dish;
import com.csoftz.reactive.playing.service.KitchenService;

import reactor.core.publisher.Flux;

@Service
public class KitchenServieImpl implements KitchenService {
    private Random picker = new Random();
    private List<Dish> menu =
        Arrays.asList(
            new Dish("Sesame chicken"),
            new Dish("Lo mein noodles, plain"),
            new Dish("Sweet & sour beef"));

    /**
     * Generates continuous stream of dishes.
     */
    @Override
    public Flux<Dish> getDishes() {
        return Flux.<Dish>generate(sink -> sink.next(randomDish()))
            .delayElements(Duration.ofMillis(250));
    }

    /**
     * Randomly pick the next dish.
     */
    private Dish randomDish() {
        return menu.get(picker.nextInt(menu.size()));
    }
}
