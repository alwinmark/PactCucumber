package com.examplestore.pricing;


import com.examplestore.product.Product;
import com.examplestore.product.ProductServiceAdapter;
import jakarta.inject.Inject;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
@Path("/")
public class PrizingController {

    public static Discount discount = new Discount(0);
    private ProductServiceAdapter products;

    @Inject
    public PrizingController(ProductServiceAdapter products) {
        this.products = products;
    }

    @POST
    @Operation(summary = "Sets the global Discount on all products")
    @APIResponse(responseCode = "200", description = "Nothing")
    @Consumes(MediaType.APPLICATION_JSON)
    @Counted(name = "discountChanges", description = "How often the discount have been changed.")
    @Timed(name = "discountChange", description = "Measures, how long the discount change took time.", unit = MetricUnits.NANOSECONDS)
    public Response setDiscount(
            @RequestBody(description = "the new discount", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Discount.class))) Discount newDiscount
    ) {
        discount = newDiscount;

        products.getAll().stream().forEach(
                (p) -> {
                    Product.Price oldPrice = p.getPrice();
                    double pricePercentage = 1 - (discount.percentage / 100.0);
                    p.setPrice(new Product.Price((long) (oldPrice.toNumber() * pricePercentage)));
                    products.updateProduct(p);
                }
        );

        return Response.status(200).build();
    }

    @GET
    @Operation(summary = "Gets the current set Discount")
    @APIResponse(responseCode = "200", description = "Returns a Discount Object", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Discount.class)))
    @Counted(name = "discountChanges", description = "How often the discount have been changed.")
    @Timed(name = "discountChange", description = "Measures, how long the discount change took time.", unit = MetricUnits.NANOSECONDS)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscount() {
        return Response.status(200).entity(discount).build();
    }

    @Getter
    @Schema(name = "Discount", description = "Discount on all Products")
    @AllArgsConstructor(onConstructor_ = {@JsonbCreator})
    @EqualsAndHashCode
    public static class Discount {

        @Schema(name = "Percentage", description = "Actual Discount as percentage", required = true)
        private final int percentage;
    }

}
