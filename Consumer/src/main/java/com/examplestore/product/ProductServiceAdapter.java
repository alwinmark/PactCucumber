package com.examplestore.product;

import java.util.List;
import java.util.Optional;

public interface ProductServiceAdapter {


    Product.ProductId addProduct(Product product);

    Optional<Product> getProduct(Product.ProductId productId);

    void updateProduct(Product product);

    List<Product> getAll();

}
