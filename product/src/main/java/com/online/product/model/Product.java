package com.online.product.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@ToString
public class Product {
    @NotNull
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String category;
    @NotNull
    private double price;
    @NotNull
    private int quantity;

}
