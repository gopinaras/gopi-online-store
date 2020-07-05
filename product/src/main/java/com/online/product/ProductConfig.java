package com.online.product;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class ProductConfig extends ResourceConfig
{
    public ProductConfig()
    {
        register(ProductResource.class);
    }
}