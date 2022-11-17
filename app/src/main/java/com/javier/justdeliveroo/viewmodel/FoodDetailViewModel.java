package com.javier.justdeliveroo.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.javier.justdeliveroo.db.AppDatabase;
import com.javier.justdeliveroo.model.ItemCarrito;
import com.javier.justdeliveroo.model.Comida;
import com.javier.justdeliveroo.API.datos.DatosComida;

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
