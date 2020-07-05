package com.online.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.product.model.Product;
import com.online.product.model.Products;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Path("/products")
public class ProductResource {
    private static ArrayList<Product> products;
    private static Products productsFromJson;
    private static ObjectMapper objectMapper;
    private Resource resource;
    @Autowired
    private ApplicationContext appContext;
    static {

        objectMapper = new ObjectMapper();
        products = new ArrayList();
        products.add(Product.builder()
                .id(1)
                .name("Oranges")
                .category("Produce")
                .quantity(10)
                .price(3.99)
                .build());
        products.add(Product.builder()
                .id(2)
                .name("Apples")
                .category("Produce")
                .quantity(30)
                .price(1.99)
                .build());
        products.add(Product.builder()
                .id(3)
                .name("Bananas")
                .category("Produce")
                .quantity(24)
                .price(0.47)
                .build());

    }


    @SneakyThrows
    @GET
    @Produces("application/json")
    @Path("/v1")
    public ArrayList<Product> getAllProducts() {
        resource = appContext.getResource("classpath:productListStarter.json");
        String json = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8.name());
        productsFromJson = objectMapper.readValue(json, Products.class);
        productsFromJson.getProducts().forEach(prd->products.add(prd));
        return products;

    }
}