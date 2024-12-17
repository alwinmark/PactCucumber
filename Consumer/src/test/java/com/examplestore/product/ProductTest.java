package com.examplestore.product;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    public void testCurrencyEuroCentConstructor() {
        Product.Price price = new Product.Price(12, 50);
        assertEquals("12,50 €" , price.toString());
    }

    @Test
    public void testCurrency() {
        Product.Price price = new Product.Price(120500);
        assertEquals("1,21 €" , price.toString());
    }


    @Test
    public void testEqualsOnProductId() {
        Product.ProductId id1 = new Product.ProductId("1");
        Product.ProductId id2 = new Product.ProductId("1");

        assertEquals(id1, id2);
    }

    @Test
    public void testHashOnProductId() {
        Product.ProductId id1 = new Product.ProductId("1");
        Product.ProductId id2 = new Product.ProductId("1");

        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void testHashMapOnProductId() {
        Product.ProductId id1 = new Product.ProductId("1");
        HashMap<Product.ProductId, String> map = new HashMap<>();

        map.put(id1, "something");

        assertTrue(map.containsKey(id1));
        assertEquals(map.get(id1), "something");
    }

}