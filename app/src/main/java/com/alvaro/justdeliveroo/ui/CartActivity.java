package com.alvaro.justdeliveroo.ui;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alvaro.justdeliveroo.R;
import com.alvaro.justdeliveroo.model.ItemCarrito;
import com.alvaro.justdeliveroo.notificaciones.NotificationHandler;
import com.alvaro.justdeliveroo.ui.Adaptadores.CartListAdapter;
import com.alvaro.justdeliveroo.viewmodel.CarritoViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView cartList;
    TextView tDiscount,hDiscount,tItemsCost,tDelivery,hDelivery,tGrandTotal;
    TextInputEditText eCoupon;
    TextInputLayout eCouponLayout;
    AppCompatButton bApply,iRemoveCoupon;
    CarritoViewModel carritoViewModel;
    Observer<List<ItemCarrito>> cartObserver;
    Observer<Double> costObserver;
    Observer<String> errorObserver;
    CartListAdapter cartListAdapter;
    NotificationHandler handler;
    String titleString = "Cupón Promocional";
    String msgString = "Gracias por usar el código promocional";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle(R.string.your_cart);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        cartList = findViewById(R.id.cart_list);
        tDiscount = findViewById(R.id.t_discount);
        tItemsCost = findViewById(R.id.t_total);
        hDiscount = findViewById(R.id.h_discount);
        tDelivery = findViewById(R.id.t_delivery);
        hDelivery = findViewById(R.id.h_delivery);
        iRemoveCoupon = findViewById(R.id.i_remove);
        tGrandTotal = findViewById(R.id.t_grand_total);
        eCouponLayout = findViewById(R.id.coupon_layout);
        eCoupon = findViewById(R.id.e_coupon);
        bApply = findViewById(R.id.b_apply);
        bApply.setOnClickListener(this);
        iRemoveCoupon.setOnClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        cartList.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        cartObserver = new Observer<List<ItemCarrito>>() {
            @Override
            public void onChanged(@Nullable List<ItemCarrito> itemCarritos) {
                cartListAdapter.setData(itemCarritos);
                cartListAdapter.notifyDataSetChanged();
                if(itemCarritos !=null && itemCarritos.size()==0){
                    finish();
                }
            }
        };
        costObserver = new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double aDouble) {
                updateUI(aDouble);
            }
        };
        errorObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                if(error!=null && error.isEmpty()){
                    eCouponLayout.setError(null);
                    eCouponLayout.setErrorEnabled(false);
                }else{
                    eCouponLayout.setError(error);
                    eCouponLayout.setErrorEnabled(true);
                }
            }
        };
        carritoViewModel = ViewModelProviders.of(this).get(CarritoViewModel.class);
        cartListAdapter = new CartListAdapter(new ArrayList<ItemCarrito>(), carritoViewModel);
        cartList.setAdapter(cartListAdapter);
        carritoViewModel.getCartItemsLiveData().observe(this,cartObserver);
        carritoViewModel.getTotal().observe(this,costObserver);
        carritoViewModel.getErrorString().observe(this,errorObserver);
        eCoupon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==EditorInfo.IME_ACTION_DONE){
                    applyCoupon();
                    return false;
                }
                return false;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(Double grandTotal) {
        tItemsCost.setText(" "+ carritoViewModel.getTotalComida() + getString(R.string.euro));
        tGrandTotal.setText(" "+ grandTotal + getString(R.string.euro));
        if(carritoViewModel.getDescuento()>0){
            hDiscount.setVisibility(View.VISIBLE);
            tDiscount.setVisibility(View.VISIBLE);
            hDiscount.setText(getString(R.string.discount)+" ( 20% )");
            tDiscount.setText(" - "+getString(R.string.euro)+" "+ carritoViewModel.getDescuento());
        }else{
            hDiscount.setVisibility(View.GONE);
            tDiscount.setVisibility(View.GONE);
        }
        if(carritoViewModel.getGastos()>0){
            hDelivery.setText(getString(R.string.delivery_charges));
            tDelivery.setText(" "+ carritoViewModel.getGastos()+getString(R.string.euro));
            tDelivery.setPaintFlags(0);
        }else{
            hDelivery.setText(getString(R.string.delivery_charges)+" ( Free )");
            tDelivery.setText("30.00 €");
            tDelivery.setPaintFlags(tDelivery.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.b_apply){
            applyCoupon();
        }else if(view.getId() == R.id.i_remove){
            eCoupon.setFocusable(true);
            eCoupon.setFocusableInTouchMode(true);
            eCoupon.setText("");
            eCoupon.setLongClickable(true);
            eCouponLayout.setErrorEnabled(false);
            eCouponLayout.setError(null);
            iRemoveCoupon.setVisibility(View.INVISIBLE);
            bApply.setVisibility(View.VISIBLE);
            carritoViewModel.applyCoupon("");
        }
    }

    private void applyCoupon() {
        if(eCoupon.getText()!=null) {
            String coupon = eCoupon.getText().toString().trim().toUpperCase();
            if (!coupon.isEmpty() && (coupon.equals("POLLOFRITO") || coupon.equals("PIZZA24H"))) {
                eCouponLayout.setErrorEnabled(false);
                eCouponLayout.setError(null);
                carritoViewModel.applyCoupon(coupon);
                eCoupon.setFocusable(false);
                eCoupon.setFocusableInTouchMode(false);
                eCoupon.setLongClickable(false);
                iRemoveCoupon.setVisibility(View.VISIBLE);
                bApply.setVisibility(View.INVISIBLE);
                /*
                if(comprobarPermisos()){
                    Notification.Builder nBuilder = handler.createNotification(titleString, msgString, true);
                    handler.getManager().notify(1,nBuilder.build());
                    }
                */
            } else {
                eCouponLayout.setErrorEnabled(true);
                eCouponLayout.setError("Cupón inválido/Caducado");
            }
        }
    }

    /*Implementar función auxiliar para chequear permiso notificaciones*/

    @Override
    protected void onDestroy() {
        carritoViewModel.getCartItemsLiveData().removeObserver(cartObserver);
        carritoViewModel.getTotal().removeObserver(costObserver);
        carritoViewModel.getErrorString().removeObserver(errorObserver);
        super.onDestroy();
    }
}
