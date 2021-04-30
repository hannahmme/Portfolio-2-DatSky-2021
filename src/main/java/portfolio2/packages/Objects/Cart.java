package portfolio2.packages.Objects;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String owner;
    private List<Product> productsInCart = new ArrayList<>();

    public Cart(String owner){
        this.owner = owner;
    }

    public String getOwner(){
        return this.owner;
    }

    public List<Product> getProductsInCart(){
        return productsInCart;
    }

    public String addProductToCart(Product product){
        if(product == null){
            return "Product is null";
        }
        productsInCart.add(product);
        return "Product added in customers cart.";
    }
}