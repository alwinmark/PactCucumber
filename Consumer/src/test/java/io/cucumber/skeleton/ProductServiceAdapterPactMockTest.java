package io.cucumber.skeleton;

import com.examplestore.product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;


@ExtendWith(MockitoExtension.class)
class ProductServiceAdapterPactMockTest {
    @Mock
    private ScenarioContext context;


    public static List<Arguments> productProvider() {
        String id = "1";
        int price = 20000;
        Product.ProductId sameProductId = new Product.ProductId(id);
        Product.Price sameProductprice = new Product.Price(price);
        Product sameProductWithSameProductIdAndPrice = new Product(sameProductId, sameProductprice);
        Product sameProductWithSameProductId = new Product(sameProductId, new Product.Price(price));
        Product sameProduct = new Product(new Product.ProductId(id), new Product.Price(price));

        return List.of(
                Arguments.of(sameProductWithSameProductIdAndPrice, sameProductWithSameProductIdAndPrice, sameProductId),
                Arguments.of(sameProductWithSameProductIdAndPrice, sameProductWithSameProductIdAndPrice, new Product.ProductId(id)),
                Arguments.of(sameProductWithSameProductIdAndPrice, sameProductWithSameProductId, new Product.ProductId(id)),
                Arguments.of(sameProductWithSameProductIdAndPrice, sameProduct, new Product.ProductId(id))
        );
    }

    @ParameterizedTest
    @MethodSource("productProvider")
    public void test_addAndRetrieveSameObject(Product in, Product expected, Product.ProductId id) throws NoSuchElementException {
        ProductServiceAdapterPactMock testee = new ProductServiceAdapterPactMock(context);
        testee.addProduct(in);
        Product out = testee.getProduct(id).get();

        Assertions.assertEquals(expected, out);
    }

}