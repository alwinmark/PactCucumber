package com.examplestore.product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Default
public class ProductServiceAdapterImpl implements ProductServiceAdapter {
    @Override
    public Product.ProductId addProduct(Product product) {
        return null;
    }

    @Override
    public Optional<Product> getProduct(Product.ProductId productId) {
        return Optional.empty();
    }

    @Override
    public void updateProduct(Product product) {

    }

    @Override
    public List<Product> getAll() {
        return null;
    }
}
