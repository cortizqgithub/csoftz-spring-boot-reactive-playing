package com.csoftz.reactive.playing.domain.commerce;

import org.springframework.data.annotation.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Item {

    @Id
    private String id;
    private String name;
    private String description;
    private double price;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
