package com.online.product;

import com.online.product.model.Product;
import com.online.product.model.Products;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Comparator;

@Path("/product")
public class ProductResource {

    private static ArrayList<Product> demoProducts;
    private static ArrayList<Product> dbProducts;
    private int maxPrdId=0;
    @Autowired
    private ApplicationContext appContext;

    static {
        demoProducts = new ArrayList();
        dbProducts = new ArrayList();

        demoProducts.add(Product.builder()
                .id(1)
                .name("Oranges")
                .category("Produce")
                .quantity(10)
                .price(3.99)
                .build());
        demoProducts.add(Product.builder()
                .id(2)
                .name("Apples")
                .category("Produce")
                .quantity(30)
                .price(1.99)
                .build());
        demoProducts.add(Product.builder()
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
    @Path("/setupdemo/v1")
    public Response setupDemoProducts() {
        Response resp;
        if (dbProducts == null || dbProducts.isEmpty()){
            dbProducts = new ArrayList();
            dbProducts.addAll(demoProducts);
            resp = Response.status(Response.Status.OK).build();
        }
        else
            resp = Response.status(Response.Status.BAD_REQUEST).entity("Demo products can only be setup when there are no products in system").build();
        return resp;

    }


    @SneakyThrows
    @GET
    @Produces("application/json")
    @Path("/list/v1")
    public Response getAllProducts(@QueryParam("order_by") String orderByAttribute) {

        Response resp;
        if (dbProducts == null || dbProducts.isEmpty())
            resp = Response.status(Response.Status.NO_CONTENT).build();
        else
            if (orderByAttribute == null || orderByAttribute.isEmpty())
                resp = Response.status(Response.Status.OK).entity(dbProducts).build();
            else
            {
                Comparator<Product> productComparator;
                if (orderByAttribute.equalsIgnoreCase("price"))
                    productComparator = Comparator.comparing(Product::getPrice);
                else
                    productComparator = Comparator.comparing(Product::getName);
                Products respProducts = new Products();
                respProducts.getProducts().addAll(dbProducts);
                respProducts.getProducts().sort(productComparator);
                resp = Response.status(Response.Status.OK).entity(respProducts).build();
            }
        return resp;

    }

    @SneakyThrows
    @GET
    @Produces("application/json")
    @Path("/find/v1/{productId}")
    public Response findProductById(@PathParam("productId") int productId) {
        Response resp;
        Product respProduct = dbProducts.stream().filter(prd -> prd.getId() == productId).findFirst().orElse(null);
        if (respProduct == null)
            resp = Response.status(Response.Status.NOT_FOUND).build();
        else
            resp = Response.status(Response.Status.OK).entity(respProduct).build();
        return resp;

    }

    @SneakyThrows
    @DELETE
    @Produces("application/json")
    @Path("/delete/v1/{productId}")
    public Response deleteProductById(@PathParam("productId") int productId) {
        Response resp;
        final ArrayList<String> errors = new ArrayList();
        int foundInPosition = -1;
        for (int i = 0; i < dbProducts.size(); i++) {
            if (dbProducts.get(i).getId() == productId)
                foundInPosition = i;
        }
        if (foundInPosition < 0)
            errors.add(String.format("Product to Modify not found. Use add the product instead"));

        if (errors.isEmpty()) {
            dbProducts.remove(foundInPosition);
            resp = Response.status(Response.Status.OK).build();
        } else
            resp = Response.status(Response.Status.NOT_FOUND).entity(errors).build();

        return resp;

    }

    @SneakyThrows
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/add/v1")
    public Response addProduct(@Valid Product product) {
        Response resp;
        final ArrayList<String> errors = new ArrayList();
        dbProducts.forEach(prd -> {
            if (prd.getId() == product.getId())
                errors.add(String.format("Duplicate Product Id. The id is used by product {%s} already", prd.getName()));
            if (prd.getName().equalsIgnoreCase(product.getName()))
                errors.add(String.format("Duplicate Product Name. This name is already used by product {%d}", prd.getId()));
            if (product.getQuantity() <= 0)
                errors.add(String.format("Initial quantity cannot be zero for product {%s}", product.getName()));
            if (product.getId() > maxPrdId)
                maxPrdId=product.getId();
        });
        if (product.getId() == 0)
            product.setId(maxPrdId+1);
        if (errors.isEmpty()) {
            dbProducts.add(product);
            resp = Response.status(Response.Status.CREATED).build();
        } else
            resp = Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        return resp;

    }

    @SneakyThrows
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/modify/v1")
    public Response modifyProduct(@Valid Product product) {
        Response resp;
        final ArrayList<String> errors = new ArrayList();
        int foundInPosition = -1;
        for (int i = 0; i < dbProducts.size(); i++) {
            if (dbProducts.get(i).getId() == product.getId())
                foundInPosition = i;
        }
        if (foundInPosition < 0)
            errors.add(String.format("Product to Modify not found. Use add the product instead"));

        if (errors.isEmpty()) {
            dbProducts.set(foundInPosition, product);
            resp = Response.status(Response.Status.OK).build();
        } else
            resp = Response.status(Response.Status.BAD_REQUEST).entity(errors).build();

        return resp;

    }
}