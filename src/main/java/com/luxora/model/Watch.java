package com.luxora.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "watches")
public class Watch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String brand;
    @NotBlank
    private String category;
    @NotBlank
    @Column(length = 1200)
    private String description;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal price;
    @DecimalMin("0.0")
    private BigDecimal oldPrice;
    @NotBlank
    private String imageUrl;
    private String movement = "Automatic";
    private String caseSize = "42 mm";
    private String strap = "Leather";
    private String waterResistance = "5 ATM";
    private boolean featured;
    private int stock = 10;

    public Watch() {
    }

    public Watch(String name, String brand, String category, String description, BigDecimal price,
            BigDecimal oldPrice, String imageUrl, String movement, String caseSize,
            String strap, String waterResistance, boolean featured, int stock) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.price = price;
        this.oldPrice = oldPrice;
        this.imageUrl = imageUrl;
        this.movement = movement;
        this.caseSize = caseSize;
        this.strap = strap;
        this.waterResistance = waterResistance;
        this.featured = featured;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public String getCaseSize() {
        return caseSize;
    }

    public void setCaseSize(String caseSize) {
        this.caseSize = caseSize;
    }

    public String getStrap() {
        return strap;
    }

    public void setStrap(String strap) {
        this.strap = strap;
    }

    public String getWaterResistance() {
        return waterResistance;
    }

    public void setWaterResistance(String waterResistance) {
        this.waterResistance = waterResistance;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
