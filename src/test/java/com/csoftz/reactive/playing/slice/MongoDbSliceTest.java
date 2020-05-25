package com.csoftz.reactive.playing.slice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.csoftz.reactive.playing.domain.commerce.Item;
import com.csoftz.reactive.playing.repository.ItemRepository;

import reactor.test.StepVerifier;

@DataMongoTest
public class MongoDbSliceTest {
    private static final String ITEM_NAME = "name";
    private static final String ITEM_DESCRIPTION = "description";
    private static final double ITEM_PRICE = 1.99;
    @Autowired
    ItemRepository repository;

    @Test
    void itemRepositorySavesItems() {
        Item sampleItem = new Item(ITEM_NAME, ITEM_DESCRIPTION, ITEM_PRICE);

        repository.save(sampleItem)
            .as(StepVerifier::create)
            .expectNextMatches(item -> {
                assertThat(item.getId()).isNotNull();
                assertThat(item.getName()).isEqualTo(ITEM_NAME);
                assertThat(item.getDescription()).isEqualTo(ITEM_DESCRIPTION);
                assertThat(item.getPrice()).isEqualTo(ITEM_PRICE);

                return true;
            })
            .verifyComplete();
    }
}
