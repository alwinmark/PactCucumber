package io.cucumber.skeleton;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslRequestWithoutPath;
import au.com.dius.pact.consumer.dsl.PactDslWithState;
import au.com.dius.pact.core.model.DefaultPactWriter;
import au.com.dius.pact.core.model.Pact;
import au.com.dius.pact.core.model.PactSpecVersion;
import com.examplestore.product.Product;
import com.examplestore.product.ProductServiceAdapter;
import io.cucumber.java.Scenario;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.NotFoundException;
import lombok.Setter;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductServiceAdapterPactMock implements ProductServiceAdapter {

    @Setter
    private ScenarioContext scenarioContext;

    private static final String PRODUCT_SERVICE = "ProductService";

    private ConsumerPactBuilder pactBuilder;

    private Pact lastPact;

    private static final Jsonb jsonb = JsonbBuilder.create();

    private HashMap<Product.ProductId, Product> products = new HashMap<>();

    @Inject
    public ProductServiceAdapterPactMock(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;

    }

    public void initScenario(Scenario scenario) {
        this.pactBuilder = ConsumerPactBuilder.consumer(String.format("PricingService.%s", scenario.getName().toLowerCase().replaceAll("\\s+", "-")));
    }

    private PactDslWithState newInteractionWithState(String description, LinkedList<String> states) {
        PactDslWithState pactDslWithState = this.pactBuilder.hasPactWith(PRODUCT_SERVICE).addMetadataValue("featureID", scenarioContext.getScenario().getId()).given(Objects.requireNonNull(states.pollLast()));

        for (String state : states) {
            pactDslWithState.given(state);
        }

        return pactDslWithState;
    }

    private PactDslRequestWithoutPath newInteraction(String description) {
        return this.pactBuilder.hasPactWith(PRODUCT_SERVICE).addMetadataValue("featureID", scenarioContext.getScenario().getId()).uponReceiving(format("%s: %s", this.pactBuilder.getInteractions().size(), description));
    }

    @Override
    public Product.ProductId addProduct(Product product) {
        products.put(product.getProductId(), product);
        lastPact = newInteraction(format("POST Product %s", product.getProductId()))
                .path("/product")
                .method("POST")
                .body(jsonb.toJson(product))
                .willRespondWith()
                .status(201)
                .headers(Map.of("Content-Type", "application/json"))
                .toPact();
        return product.getProductId();
    }

    @Override
    public Optional<Product> getProduct(Product.ProductId productId) {
        return Optional.ofNullable(products.get(productId));
    }

    @Override
    public void updateProduct(Product product) {
        lastPact = newInteraction(format("PUT Product %s", product.getProductId()))
                .path("/product")
                .method("PUT")
                .body(jsonb.toJson(product))
                .willRespondWith()
                .status(204)
                .headers(Map.of("Content-Type", "application/json"))
                .toPact();

        this.products.put(product.getProductId(), product);
    }

    public void assertPriceOfProduct(ScenarioContext.Variable var, Product.Price expectedPrice) throws IOException {
        Product p = getProduct(var).orElseThrow(NotFoundException::new);


        lastPact = newInteraction(format("GET Product %s", var))
                .path("/product")
                .method("GET")
                .query(format("id=%s", p.getProductId()))
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .integerType("id", 1)
                        .numberValue("price", expectedPrice.toNumber())
                ).toPact();

        assertEquals(expectedPrice, p.getPrice());
    }


    /**
     * Usage for Given/Then purposes only
     *
     * @param variable
     * @return the corresponding Product for the Cucumber Variable as contracted Request/Response
     */
    public Optional<Product> getProduct(ScenarioContext.Variable variable) {
        return getProduct(getProductIdForVariable(variable));
    }

    private Product.ProductId getProductIdForVariable(ScenarioContext.Variable var) {
        return scenarioContext.get(var, Product.ProductBuilder.class).build().getProductId();
    }

    @Override
    public List<Product> getAll() {
        List<Product> productList = new ArrayList<>(products.values());
        lastPact = newInteraction("GET all products")
                .path("/product")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(jsonb.toJson(productList)).toPact();
        return productList;
    }

    public void writePactFile(String featureFile) {
        File pactFile = new File(format("target/pacts/%s/%s.json", featureFile, this.scenarioContext.getScenario().getName().toLowerCase().replaceAll("\\s+", "-")));
        DefaultPactWriter.INSTANCE.writePact(pactFile, lastPact, PactSpecVersion.V4);


        System.out.println(pactFile.toPath().toAbsolutePath());
    }


}
