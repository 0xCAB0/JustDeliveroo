package com.javier.justdeliveroo.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.javier.justdeliveroo.model.Comida;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface ComidaDao {
    //Insertar
    @Insert(onConflict = REPLACE)
    void save(List<Comida> foodDetails);

    @Insert(onConflict = REPLACE)
    void save(Comida comida);

    @Query("DELETE FROM Comida WHERE name NOT IN (:nameList)")
    void deleteOtherFoods(List<String> nameList);

    @Query("DELETE FROM Comida")
    void deleteAll();

    @Query("SELECT * FROM Comida ORDER BY price ASC")
    LiveData<List<Comida>> getFoodsByPrice();

    @Query("SELECT * FROM Comida ORDER BY rating DESC")
    LiveData<List<Comida>> getFoodsByRating();

    @Query("SELECT * FROM Comida WHERE name = :name")
    LiveData<Comida> getFood(String name);
}
