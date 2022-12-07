package com.alvaro.justdeliveroo.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.alvaro.justdeliveroo.R;
import com.alvaro.justdeliveroo.model.ItemCarrito;
import com.alvaro.justdeliveroo.model.Comida;
import com.alvaro.justdeliveroo.viewmodel.FoodDetailViewModel;

import java.util.List;

public class ProductoActivity extends AppCompatActivity implements View.OnClickListener {

    private FoodDetailViewModel foodDetailViewModel;
    Observer<Comida> foodDetailObserver;
    private ImageView iFoodImage;
    private TextView tName,tCost,tQuantity,tTotalCost,tCartQuantity;
    private Toolbar cartView;
    Observer<List<ItemCarrito>> cartObserver;
    private Comida duplicateComida;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        String foodId = "";
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            if(bundle.containsKey("name")) {
                foodId = bundle.getString("name");
            }
        }
        if(foodId !=null && !foodId.isEmpty()) {
            iFoodImage = findViewById(R.id.i_food_image);
            tName = findViewById(R.id.t_name);
            tCost = findViewById(R.id.t_cost);
            tQuantity = findViewById(R.id.t_quantity);
            AppCompatImageView iPlus = findViewById(R.id.i_plus);
            AppCompatImageView iMinus = findViewById(R.id.i_minus);
            iPlus.setOnClickListener(this);
            iMinus.setOnClickListener(this);
            cartView = findViewById(R.id.cart_view);
            tTotalCost = findViewById(R.id.t_total_price);
            tCartQuantity = findViewById(R.id.t_cart_count);
            AppCompatButton bCart = findViewById(R.id.b_cart);
            bCart.setOnClickListener(this);

            foodDetailViewModel = ViewModelProviders.of(this).get(FoodDetailViewModel.class);
            foodDetailViewModel.subscribeForFoodDetails(foodId);
            foodDetailObserver = new Observer<Comida>() {
                @Override
                public void onChanged(@Nullable Comida comida) {
                    updateUI(comida);
                }
            };
            cartObserver = new Observer<List<ItemCarrito>>() {
                @Override
                public void onChanged(@Nullable List<ItemCarrito> itemCarritos) {
                    updateCartUI(itemCarritos);
                }
            };
            foodDetailViewModel.getFoodDetailsLiveData().observe(this, foodDetailObserver);
            foodDetailViewModel.getCartItemsLiveData().observe(this, cartObserver);
        }
    }


    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void updateUI(Comida comida) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_food);
        duplicateComida = comida;
        if(comida ==null){
            return;
        }
        tName.setText(comida.getName());
        tCost.setText(getString(R.string.euro) + comida.getPrice());
        Glide.with(this).load(comida.getImageUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions)
                .into(iFoodImage);
        tQuantity.setText(String.valueOf(comida.getQuantity()));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.i_minus:
                if(duplicateComida.getQuantity()!=0) {
                    duplicateComida.setQuantity(duplicateComida.getQuantity()-1);
                    tQuantity.setText(String.valueOf(duplicateComida.getQuantity()));
                }
                foodDetailViewModel.updateCart(duplicateComida);
                break;
            case R.id.i_plus:
                duplicateComida.setQuantity(duplicateComida.getQuantity()+1);
                tQuantity.setText(String.valueOf(duplicateComida.getQuantity()));
                foodDetailViewModel.updateCart(duplicateComida);
                break;
            case R.id.b_cart:
                startActivity(new Intent(this,CartActivity.class));
                break;

        }
    }

    @SuppressLint("SetTextI18n")
    private void updateCartUI(List<ItemCarrito> itemCarritos) {
        if(itemCarritos !=null && itemCarritos.size()>0){
            cartView.setVisibility(View.VISIBLE);
            double cost = 0.0;
            int quantity = 0;
            for(ItemCarrito itemCarrito : itemCarritos){
                cost = cost+(itemCarrito.getPrice()* itemCarrito.getQuantity());
                quantity = quantity+ itemCarrito.getQuantity();
            }
            tCartQuantity.setText(String.valueOf(quantity));
            tTotalCost.setText(getString(R.string.euro)+ cost);
        }else{
            cartView.setVisibility(View.GONE);
            tCartQuantity.setText("0");
            tTotalCost.setText(getString(R.string.euro)+"0");
        }
    }

    @Override
    protected void onDestroy() {
        foodDetailViewModel.getFoodDetailsLiveData().removeObserver(foodDetailObserver);
        foodDetailViewModel.getCartItemsLiveData().removeObserver(cartObserver);
        super.onDestroy();
    }
}
