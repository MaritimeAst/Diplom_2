package generators;

import models.Order;

import java.util.List;

public class OrderGenerator {

    public static Order getOrderDefault() {
      String ingredients = "61c0c5a71d1f82001bdaaa6d";

        return new Order(ingredients);
   }

    public static Order getOrderWithoutIngredients() {

        return new Order(" ");
    }

    public static Order getOrderWithWrongIngredients() {
        String ingredients = "1110c5a71d1f82001bdaaa6d";

        return new Order(ingredients);
    }

//    public static Order getOrderCheeseBurger() {
//
//        return new Order( List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa7a"));
//
//    }

//    String ingredients = "[\"61c0c5a71d1f82001bdaaa6d\", \"61c0c5a71d1f82001bdaaa6f\", \"61c0c5a71d1f82001bdaaa72\", \"61c0c5a71d1f82001bdaaa76\", \"61c0c5a71d1f82001bdaaa79\"]";

}