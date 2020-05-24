package com.csoftz.reactive.playing.domain.commerce;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Cart {
  @Id private String id;
  private List<CartItem> cartItems;

  public Cart(String id) {
    this(id, new ArrayList<>());
  }
}
