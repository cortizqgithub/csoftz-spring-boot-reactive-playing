package com.csoftz.reactive.playing.util.component;

import org.springframework.boot.CommandLineRunner;

import com.csoftz.reactive.playing.domain.commerce.Item;
import com.csoftz.reactive.playing.repository.BlockingItemRepository;

// @Component
public class RepositoryDatabaseLoader {
    // @Bean
    CommandLineRunner initialize(BlockingItemRepository repository) {
        // Don't ever execute this blocking code.
        return args -> {
            repository.save(new Item("Alf alarm clock", 19.99));
            repository.save(new Item("Smurf TV tray", 24.99));
        };
    }
}
