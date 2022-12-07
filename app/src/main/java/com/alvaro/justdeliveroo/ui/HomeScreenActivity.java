package com.alvaro.justdeliveroo.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.alvaro.justdeliveroo.R;
import com.alvaro.justdeliveroo.model.ItemCarrito;
import com.alvaro.justdeliveroo.model.Comida;
import com.alvaro.justdeliveroo.conexion.checkConexion;
import com.alvaro.justdeliveroo.ui.Adaptadores.ComidaAdapter;
import com.alvaro.justdeliveroo.utility.ObservableObject;
import com.alvaro.justdeliveroo.viewmodel.FoodViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class HomeScreenActivity extends AppCompatActivity implements java.util.Observer, PopupMenu.OnMenuItemClickListener {

    FoodViewModel foodViewModel;
    Observer<List<Comida>> foodMenuObserver;
    Observer<List<ItemCarrito>> cartObserver;
    Observer<Boolean> isFoodUpdateInProgressObserver;
    RecyclerView foodList;
    ComidaAdapter comidaAdapter;
    AppCompatButton bCart;
    ImageView infoImage;
    TextView tInfo,tTotalCost,tCartQuantity;
    Toolbar cartView;
    FirebaseAuth firebaseAuth;
    public static final String INTENT_UPDATE_FOOD = "UPDATE_FOOD";
    public static final String INTENT_UPDATE_LIST = "UPDATE_LIST";
    public static final String ACTION_SORT_BY_PRICE = "SORT_PRICE";
    public static final String ACTION_SORT_BY_RATING = "SORT_RATING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Base);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //Obtenemos la sesión que hemos iniciado
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(user != null){
            toolbar.setTitle(user.getEmail());
        }
        else
            toolbar.setTitle(R.string.app_name + "Please LogIn");
        setSupportActionBar(toolbar);

        //Setting up the view model.
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);
        foodList = findViewById(R.id.food_list);
        tInfo = findViewById(R.id.t_loading);
        infoImage = findViewById(R.id.i_loading);
        cartView = findViewById(R.id.cart_view);
        tTotalCost = cartView.findViewById(R.id.t_total_price);
        tCartQuantity = cartView.findViewById(R.id.t_cart_count);
        bCart = cartView.findViewById(R.id.b_cart);
        bCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreenActivity.this,CartActivity.class));
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        foodList.setLayoutManager(mLayoutManager);
        comidaAdapter = new ComidaAdapter(new ArrayList<Comida>());
        foodList.setAdapter(comidaAdapter);
        foodList.scheduleLayoutAnimation();

        //Comprobamos conexión para obtener datos
        checkInternetConnectivity();
    }

    //Check for the network connections.
    private void checkInternetConnectivity() {
        boolean networkAvailable = checkConexion.isConnected(this);

        if (!networkAvailable) {
            tInfo.setText(R.string.no_internet_connection);
            tInfo.setTextSize(20f);
        } else {
            tInfo.setTextSize(14f);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        foodMenuObserver = new Observer<List<Comida>>() {
            @Override
            public void onChanged(@Nullable List<Comida> foodDetails) {
                if(foodDetails!=null){
                    comidaAdapter.setData(foodDetails);
                    comidaAdapter.notifyDataSetChanged();
                    runLayoutAnimation(foodList);
                }else{
                    Log.e("Food details","null");
                }
            }
        };
        isFoodUpdateInProgressObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean!=null && !aBoolean){
                    showProgress(false,true);
                    subscribeToFoodObserver();
                }else{
                    showProgress(true,false);
                }
            }
        };
        cartObserver = new Observer<List<ItemCarrito>>() {
            @Override
            public void onChanged(@Nullable List<ItemCarrito> itemCarritos) {
                updateCartUI(itemCarritos);
            }
        };
        foodViewModel.isFoodUpdateInProgress().observe(this,isFoodUpdateInProgressObserver);
        ObservableObject.getInstance().addObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_sort){
            showPopup(findViewById(R.id.action_sort));
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.action_sort_price){
            foodViewModel.sortFood(ACTION_SORT_BY_PRICE);
        }else if(menuItem.getItemId() == R.id.action_sort_rating){
            foodViewModel.sortFood(ACTION_SORT_BY_RATING);
        }
        return false;
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
            tTotalCost.setText(cost + " "+getString(R.string.euro));
        }else{
            cartView.setVisibility(View.GONE);
            tCartQuantity.setText("0");
            tTotalCost.setText("0 " +getString(R.string.euro));
        }
    }

    private void subscribeToFoodObserver() {
        if(!foodViewModel.getFoodDetailsMutableLiveData().hasObservers()) {
            foodViewModel.getFoodDetailsMutableLiveData().observe(HomeScreenActivity.this, foodMenuObserver);
        }
        if(!foodViewModel.getCartItemsLiveData().hasObservers()){
            foodViewModel.getCartItemsLiveData().observe(this,cartObserver);
        }
    }

    private void showProgress(boolean show, boolean showList) {
        foodList.setVisibility(showList?View.VISIBLE:View.GONE);
        tInfo.setVisibility(show?View.VISIBLE:View.GONE);
        infoImage.setVisibility(show?View.VISIBLE:View.GONE);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        if(recyclerView.getAdapter()!=null) {
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();
        }
    }

    @Override
    protected void onDestroy() {
        foodViewModel.getFoodDetailsMutableLiveData().removeObserver(foodMenuObserver);
        foodViewModel.isFoodUpdateInProgress().removeObserver(isFoodUpdateInProgressObserver);
        foodViewModel.getCartItemsLiveData().removeObserver(cartObserver);
        ObservableObject.getInstance().deleteObserver(this);
        Glide.get(this).clearMemory();
        super.onDestroy();
    }

    @Override
    public void update(Observable observable, Object o) {
        Intent intent = (Intent)o;
        if(intent!=null && intent.getAction() != null) {
            if (intent.getAction().equals(INTENT_UPDATE_FOOD)) {
                foodViewModel.updateCart(comidaAdapter.getItem(intent.getIntExtra("position",-1)));
            }else  if(intent.getAction().equals(INTENT_UPDATE_LIST)){
                comidaAdapter.notifyDataSetChanged();
            }
        }
    }
}
