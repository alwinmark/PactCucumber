package io.cucumber.skeleton;

import com.examplestore.pricing.PrizingController;
import com.examplestore.product.Product;
import com.examplestore.product.Product.ProductBuilder;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.internal.runners.statements.Fail;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ApplicationScoped
@AllArgsConstructor
public class StepDefinitions {

    @Inject
    private World world;

    @RestClient
    private PrizingController prizingController;


    @ParameterType("[0-9a-zA-Z-_]")
    public ScenarioContext.Variable variable(String variable) {
        return new ScenarioContext.Variable(variable);
    }


    @Given("A Product {variable}")
    public void a_product_a(ScenarioContext.Variable variable) {
        ProductBuilder currentProduct = Product.builder();
        world.getScenarioContext().put(variable, currentProduct);
    }
    @Given("Product {variable} has id {int}")
    public void product_a_has_id(ScenarioContext.Variable var, int id) {
        ProductBuilder currentProduct = world.getScenarioContext().get(var, ProductBuilder.class);
        currentProduct.productId(new Product.ProductId(""+id));
    }
    @Given("Product {variable} has price {long}€")
    public void product_a_has_price_€(ScenarioContext.Variable var, Long preis) {
        ProductBuilder currentProduct = world.getScenarioContext().get(var, ProductBuilder.class);
        currentProduct.price(new Product.Price(preis * 100000L));
    }

    @Given("Product {variable} is stored")
    public void product_a_is_stored(ScenarioContext.Variable var) {
        world.getProductServiceAdapter().addProduct(world.getScenarioContext().get(var, ProductBuilder.class).build());
        //TODO setup product in Pact as well
    }

    @When("Discount is changed to {int}%")
    public void discountIsChangedTo(Integer percentage) {
        prizingController.setDiscount(new PrizingController.Discount(percentage));
    }

    @Then("Product {variable} should have price {int}€")
    public void product_a_should_have_price_€(ScenarioContext.Variable var, Integer price) throws IOException {
        world.getProductServiceAdapter().assertPriceOfProduct(var, new Product.Price(price*100000));
    }

    @Then("Discount should be {int}%")
    public void discount_should_be(Integer expectedPercentage) {
        assertEquals(new PrizingController.Discount(expectedPercentage), prizingController.getDiscount().getEntity());
    }


}
