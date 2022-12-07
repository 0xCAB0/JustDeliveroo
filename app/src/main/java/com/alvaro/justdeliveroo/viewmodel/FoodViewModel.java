package com.alvaro.justdeliveroo.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alvaro.justdeliveroo.db.AppDatabase;
import com.alvaro.justdeliveroo.model.ItemCarrito;
import com.alvaro.justdeliveroo.model.Comida;
import com.alvaro.justdeliveroo.API.datos.DatosComida;

import java.util.List;

import static com.alvaro.justdeliveroo.ui.HomeScreenActivity.ACTION_SORT_BY_PRICE;
import static com.alvaro.justdeliveroo.ui.HomeScreenActivity.ACTION_SORT_BY_RATING;


public class FoodViewModel extends AndroidViewModel {

    private AppDatabase db;
    private final MediatorLiveData<List<Comida>> foodDetailsMediatorLiveData = new MediatorLiveData<>();
    private LiveData<List<Comida>> foodDetailsLiveDataSortPrice;
    private LiveData<List<Comida>> foodDetailsLiveDataSortRating;
    private LiveData<List<ItemCarrito>> cartItemsLiveData;
    private MutableLiveData<Boolean> isFoodCallInProgress = new MutableLiveData<>();
    private static String DEFAULT_SORT = ACTION_SORT_BY_PRICE;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        db = AppDatabase.getDatabase(getApplication().getApplicationContext());
        updateFoodMenu();
        subscribeToFoodChanges();
        subscribeToCartChanges();
    }

    private void subscribeToCartChanges() {
        cartItemsLiveData = db.cartItemDao().getCartItems();
    }

    private void updateFoodMenu() {
        isFoodCallInProgress = DatosComida.getInstance().getFoodMenu(getApplication().getApplicationContext());
    }

    private void subscribeToFoodChanges() {
        if(DEFAULT_SORT.equals(ACTION_SORT_BY_PRICE)){
            foodDetailsLiveDataSortPrice = db.foodDetailsDao().getFoodsByPrice();
            foodDetailsMediatorLiveData.addSource(foodDetailsLiveDataSortPrice, new Observer<List<Comida>>() {
                @Override
                public void onChanged(@Nullable List<Comida> foodDetails) {
                    foodDetailsMediatorLiveData.setValue(foodDetails);
                }
            });
        }else if(DEFAULT_SORT.equals(ACTION_SORT_BY_RATING)){
            foodDetailsLiveDataSortRating = db.foodDetailsDao().getFoodsByRating();
            foodDetailsMediatorLiveData.addSource(foodDetailsLiveDataSortRating, new Observer<List<Comida>>() {
                @Override
                public void onChanged(@Nullable List<Comida> foodDetails) {
                    foodDetailsMediatorLiveData.setValue(foodDetails);
                }
            });
        }
    }

    public MediatorLiveData<List<Comida>> getFoodDetailsMutableLiveData() {
        return foodDetailsMediatorLiveData;
    }

    public void sortFood(String action){
        removeSource(DEFAULT_SORT);
        DEFAULT_SORT = action;
        subscribeToFoodChanges();
    }

    private void removeSource(String default_sort) {
        switch (default_sort){
            case ACTION_SORT_BY_PRICE:
                foodDetailsMediatorLiveData.removeSource(foodDetailsLiveDataSortPrice);
                break;
            case ACTION_SORT_BY_RATING:
                foodDetailsMediatorLiveData.removeSource(foodDetailsLiveDataSortRating);
                break;
        }
    }

    public LiveData<Boolean> isFoodUpdateInProgress(){
        return isFoodCallInProgress;
    }

    public LiveData<List<ItemCarrito>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }

    public void updateCart(Comida comida){
        DatosComida.getInstance().updateCart(db, comida);
    }

}
