package com.alvaro.justdeliveroo.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.StrictMode;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.alvaro.justdeliveroo.db.AppDatabase;
import com.alvaro.justdeliveroo.model.ItemCarrito;
import com.alvaro.justdeliveroo.model.Comida;
import com.alvaro.justdeliveroo.API.datos.DatosComida;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FoodDetailViewModel extends AndroidViewModel {

    private AppDatabase db;
    private LiveData<List<ItemCarrito>> cartItemsLiveData;
    private LiveData<Comida> foodDetailsLiveData;

    public FoodDetailViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        db = AppDatabase.getDatabase(getApplication().getApplicationContext());
        subscribeToCartChanges();
    }

    private void subscribeToCartChanges() {
        cartItemsLiveData = db.cartItemDao().getCartItems();
    }

    public void subscribeForFoodDetails(String name){
        foodDetailsLiveData = db.foodDetailsDao().getFood(name);
    }

    public LiveData<Comida> getFoodDetailsLiveData(){
        return foodDetailsLiveData;
    }

    public LiveData<List<ItemCarrito>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }

    public void updateCart(Comida comida){
        DatosComida.getInstance().updateCart(db, comida);
        db.foodDetailsDao().save(comida);
    }


}
