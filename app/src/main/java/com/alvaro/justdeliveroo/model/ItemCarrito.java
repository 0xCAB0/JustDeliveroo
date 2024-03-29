package com.alvaro.justdeliveroo.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class ItemCarrito {
   /**
   El modelo de un item del carrito es esencialmente un subset de un elemento
   comida, por lo que obtenemos lo necesario usando su PK (name)

    ItemCarrito:
    * TEXT item_name (FK refereced from Comida)
    * FLOAT price (FK refereced from Comida)
    * INT quantity
    * */

    @ForeignKey(entity = Comida.class,
            parentColumns = "name",
            childColumns = "item_name",onDelete = ForeignKey.CASCADE)
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "item_name")
    private String name="";

    @ColumnInfo(name = "item_price")
    private Double price;

    //Esta cantidad es distinta a la del objeto en comida
    @ColumnInfo(name = "item_quantity_wanted")
    //Por default añade 1 al carrito
    private Integer quantity = 1;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
