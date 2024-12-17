package com.examplestore.product;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/")
public class ProductResource {

    @GET
    @Operation(summary = "Gets all Products")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.ARRAY, implementation = Product.class)))
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        list.add(new Product(new Product.ProductId("1"), new Product.Price(200000)));

        return list;
    }
}
