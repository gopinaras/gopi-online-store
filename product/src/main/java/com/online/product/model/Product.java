package com.online.product.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;

}
