package models;

public class Order {
    public String firstIngredient;
    public String secondIngredient;

    public Order(String firstIngredient, String secondIngredient) {
        this.firstIngredient = firstIngredient;
        this.secondIngredient = secondIngredient;
    }

    public String getFirstIngredient() {
        return firstIngredient;
    }

    public void setFirstIngredient(String firstIngredient) {
        this.firstIngredient = firstIngredient;
    }

    public String getSecondIngredient() {
        return secondIngredient;
    }

    public void setSecondIngredient(String secondIngredient) {
        this.secondIngredient = secondIngredient;
    }
}
