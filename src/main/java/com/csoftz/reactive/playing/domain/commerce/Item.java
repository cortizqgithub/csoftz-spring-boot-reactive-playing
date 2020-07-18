package com.csoftz.reactive.playing.domain.commerce;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Item implements Serializable {

    private static final long serialVersionUID = -2689503644260171344L;

    @Id
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
