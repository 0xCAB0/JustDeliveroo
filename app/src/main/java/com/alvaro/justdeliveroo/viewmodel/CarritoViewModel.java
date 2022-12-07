package com.alvaro.justdeliveroo.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alvaro.justdeliveroo.db.AppDatabase;
import com.alvaro.justdeliveroo.model.ItemCarrito;
import com.alvaro.justdeliveroo.utility.ObservableObject;

import java.util.List;

import static com.alvaro.justdeliveroo.ui.HomeScreenActivity.INTENT_UPDATE_LIST;

public class CarritoViewModel extends AndroidViewModel {

    private AppDatabase db;
    private Double totalComida=0.0,descuento=0.0,gastos=0.0;
    private final MutableLiveData<Double> Total = new MutableLiveData<>();
    private final MediatorLiveData<List<ItemCarrito>> mediatorLiveData = new MediatorLiveData<>();
    private String couponApplied="";
    private final MutableLiveData<String> errorString = new MutableLiveData<>();

    public CarritoViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        db = AppDatabase.getDatabase(getApplication().getApplicationContext());
        subscribeToCartChanges();
        gastos=calculaGastos();
    }

    private void subscribeToCartChanges() {
        LiveData<List<ItemCarrito>> cartItemsLiveData = db.cartItemDao().getCartItems();
        mediatorLiveData.addSource(cartItemsLiveData, new Observer<List<ItemCarrito>>() {
            @Override
            public void onChanged(@Nullable List<ItemCarrito> itemCarritos) {
                mediatorLiveData.setValue(itemCarritos);
                calculaTotal();
            }
        });
    }

    private void calculaTotal() {
        List<ItemCarrito> itemCarritoList = mediatorLiveData.getValue();
        totalComida = 0.0;
        if(itemCarritoList !=null) {
            for (ItemCarrito itemCarrito : itemCarritoList) {
                totalComida = totalComida+(itemCarrito.getPrice()* itemCarrito.getQuantity());
            }
            descuento = calculaDescuento(couponApplied);
            Total.setValue(Math.round((totalComida - descuento + gastos) * 100d) / 100d);
        }
    }

    private Double calculaGastos() {
        Double num = 3 + Math.random()*5;
        num = Math.round(num * 100d) / 100d; //2 cifras
        return num;
    }

    private Double calculaDescuento(String couponApplied) {
        if(couponApplied.equals("PIZZA24H")){
            errorString.setValue("");
            //20% de descuento sobre el totalComida
            return (totalComida*20)/100;
        }
        else if(couponApplied.equals("POLLOFRITO")){
            errorString.setValue("");
            //10%
            return (totalComida*10)/100;
        }

        return 0.0;
    }
    /*Métodos getters para variables privadas*/
    public Double getDescuento(){
        return descuento;
    }

    public Double getGastos(){
        return gastos;
    }

    public Double getTotalComida(){
        return totalComida;
    }

    public MutableLiveData<Double> getTotal(){
        return Total;
    }


    public void applyCoupon(String coupon) {
        couponApplied = coupon;
        calculaTotal();
    }

    public MediatorLiveData<List<ItemCarrito>> getCartItemsLiveData() {
        return mediatorLiveData;
    }

    public void removeItem(String name){
        db.cartItemDao().deleteCartItem(name);
        ObservableObject.getInstance().updateValue(new Intent(INTENT_UPDATE_LIST));
    }

    public MutableLiveData<String> getErrorString(){
        return errorString;
    }
    public String getErrorStringString(){
        return errorString.getValue();
    }
}
