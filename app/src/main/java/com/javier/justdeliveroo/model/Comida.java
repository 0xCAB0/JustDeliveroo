package com.javier.justdeliveroo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Comida {

    @PrimaryKey
    @SerializedName("name")
    @Expose
    @NonNull
    private String name;

    @SerializedName("price")
    @Expose
    private Double price;

    @SerializedName("rating")
    @Expose
    private Double rating;

    @SerializedName("quantity")
    @Expose
    private Integer quantity = 0;

    @SerializedName("url")
    @Expose
    private String imageUrl;


    public Comida(@NonNull String name, Double price, Double rating, Integer quantity,String imageUrl) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    //Getters y Setters
    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
