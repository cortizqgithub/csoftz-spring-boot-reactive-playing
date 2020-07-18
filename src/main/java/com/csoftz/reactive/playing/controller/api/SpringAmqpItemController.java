package com.csoftz.reactive.playing.controller.api;

import java.net.URI;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csoftz.reactive.playing.domain.commerce.Item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/amqp")
public class SpringAmqpItemController {

    private final AmqpTemplate template;

    @PostMapping("/items")
    public Mono<ResponseEntity<?>> addNewItemUsingSpringAmqp(@RequestBody Mono<Item> item) {
        return item.publishOn(Schedulers.boundedElastic())
            .flatMap(
                content ->
                    Mono.fromCallable(
                        () -> {
                            this.template.convertAndSend(
                                "hacking-spring-boot", "new-items-spring-amqp", content);
                            return ResponseEntity.created(URI.create("/api/v1/amqp/items")).build();
                        }));
    }
}
