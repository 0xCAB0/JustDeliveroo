package com.alvaro.justdeliveroo.db;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.alvaro.justdeliveroo.model.ItemCarrito;

import java.util.List;


@Dao
public interface CartItemDao {
    @Query("SELECT * FROM ItemCarrito")
    LiveData<List<ItemCarrito>> getCartItems();

    @Insert(onConflict = REPLACE)
    void add(ItemCarrito itemCarrito);

    @Query("DELETE FROM ItemCarrito WHERE item_name = :name")
    void deleteCartItem(String name);

    @Query("DELETE FROM ItemCarrito")
    void deleteCarrito();

    @Query("SELECT item_quantity_wanted FROM ItemCarrito WHERE item_name = :name")
    int getCartCount(String name);
}
