package com.alvaro.justdeliveroo.extras;

import android.os.AsyncTask;

import com.alvaro.justdeliveroo.db.AppDatabase;
import com.alvaro.justdeliveroo.model.ItemCarrito;
import com.alvaro.justdeliveroo.model.Comida;

//Save food to the cart in database async task.
public class UpdateCart extends AsyncTask<Comida,Void,Void> {
    private final AppDatabase db;
    @SuppressWarnings("deprecation")
    public UpdateCart(AppDatabase db) {
        this.db = db;
    }

    @Override
    protected Void doInBackground(Comida... foodDetails) {
        if(db!=null){
            if(foodDetails[0]!=null) {
                if (foodDetails[0].getQuantity() == 0) {
                    db.cartItemDao().deleteCartItem(foodDetails[0].getName());
                    return null;
                }
                ItemCarrito itemCarrito = new ItemCarrito();
                itemCarrito.setName(foodDetails[0].getName());
                itemCarrito.setPrice(foodDetails[0].getPrice());
                itemCarrito.setQuantity(foodDetails[0].getQuantity());
                db.cartItemDao().add(itemCarrito);
            }
        }
        return null;
    }
}
