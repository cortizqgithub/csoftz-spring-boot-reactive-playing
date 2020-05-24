package com.csoftz.reactive.playing.domain.commerce;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Item {

  @Id private String id;
  private String name;
  private String description;
  private double price;

  public Item(String name, double price) {
    this.name = name;
    this.price = price;
  }
}
