package mk.ukim.finki.wp.blossomhouse.model;

import lombok.Data;

@Data
public
class ProductQuantity {

    Product product;
    Integer quantity;

    public ProductQuantity( Product product, Integer quantity){
        this.product=product;
        this.quantity=quantity;
    }
}
