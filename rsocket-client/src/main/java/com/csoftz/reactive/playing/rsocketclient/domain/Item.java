package com.csoftz.reactive.playing.rsocketclient.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Item {
    private String id;
    private String name;
    private String description;
    private double price;

    public Item(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
