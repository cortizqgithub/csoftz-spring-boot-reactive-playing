package com.csoftz.reactive.playing.rsocketserver.service;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.csoftz.reactive.playing.rsocketserver.domain.Item;
import com.csoftz.reactive.playing.rsocketserver.repository.ItemRepository;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@Controller
public class RSocketService {

    private final ItemRepository repository;
    private final EmitterProcessor<Item> itemProcessor;
    private final FluxSink<Item> itemSink;

    public RSocketService(ItemRepository repository) {
        this.repository = repository;
        this.itemProcessor = EmitterProcessor.create();
        this.itemSink = this.itemProcessor.sink();
    }

    @MessageMapping("newItems.request-response")
    public Mono<Item> processNewItemsViaRSocketRequestResponse(Item item) {
        return this.repository.save(item)
            .doOnNext(this.itemSink::next);
    }

    @MessageMapping("newItems.fire-and-forget")
    public Mono<Void> processNewItemsViaRSocketFireAndForget(Item item) {
        return this.repository.save(item)
            .doOnNext(this.itemSink::next)
            .then();
    }

    @MessageMapping("newItems.monitor")
    public Flux<Item> monitorNewItems() {
        return this.itemProcessor;
    }
}
