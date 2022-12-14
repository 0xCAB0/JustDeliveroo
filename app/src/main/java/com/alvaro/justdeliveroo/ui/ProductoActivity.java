package com.alvaro.justdeliveroo.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alvaro.justdeliveroo.R;
import com.alvaro.justdeliveroo.model.Comida;
import com.alvaro.justdeliveroo.model.ItemCarrito;
import com.alvaro.justdeliveroo.viewmodel.FoodDetailViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ProductoActivity extends AppCompatActivity implements View.OnClickListener {

    private FoodDetailViewModel foodDetailViewModel;
    Observer<Comida> foodDetailObserver;
    private ImageView iFoodImage;
    private TextView tName,tCost,tQuantity,tTotalCost,tCartQuantity;
    private Toolbar cartView;
    Observer<List<ItemCarrito>> cartObserver;
    private Comida duplicateComida;

    //AÑADIDO PARA COMPARTIR
    ImageButton share;
    private FileOutputStream fileOutputStream;
    private Intent intent;

    //---------------

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
            //BOTON AÑADIDO
            share = findViewById(R.id.btn_share);
            //---------------
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
        //AÑADIDO
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        //---------------
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
                Random r = new Random();
               int randomNumber = r.nextInt(2);
                // Si el número generado es 0, mostramos un mensaje de notificación
                // indicando que no hay disponibilidad de ese producto
                if (randomNumber == 0) {
                    Toast.makeText(getApplicationContext(), "Lo sentimos, este producto no está disponible en este momento.", Toast.LENGTH_LONG).show();
                }else {
                    duplicateComida.setQuantity(duplicateComida.getQuantity() + 1);
                    tQuantity.setText(String.valueOf(duplicateComida.getQuantity()));
                    foodDetailViewModel.updateCart(duplicateComida);
                }
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

    private void share() {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        iFoodImage.buildDrawingCache();
        Bitmap bitmap = iFoodImage.getDrawingCache();

        File file = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getResources().getString(R.string.app_name)+".jpg");

        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();

            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");

            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.putExtra(Intent.EXTRA_TEXT, "Elección: " + tName.getText() + "\r\n" + "Precio: " + tCost.getText() + "\r\n");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startActivity(Intent.createChooser(intent, "Share Image"));

    }
}
