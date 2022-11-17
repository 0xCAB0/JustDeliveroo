package com.javier.justdeliveroo.extras;

import android.os.AsyncTask;

import com.javier.justdeliveroo.db.AppDatabase;
import com.javier.justdeliveroo.model.Comida;

import java.util.ArrayList;
import java.util.List;

//Meter comida en la BBDD como tarea asíncrona
public class insertComida extends AsyncTask<Void,Void,Void> {
    private final AppDatabase db;
    private final List<Comida> foodDetails; //Lista que almacena elementos tipo comida
    @SuppressWarnings("deprecation")
    public insertComida(AppDatabase db, List<Comida> foodDetails) {
        this.db = db;
        this.foodDetails = foodDetails;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(db!=null){
            if(foodDetails!=null && foodDetails.size()>0) {
                List<String> nameList = new ArrayList<>();
                for(int i=0;i<foodDetails.size();i++){
                    nameList.add(foodDetails.get(i).getName());
                    foodDetails.get(i).setQuantity(db.cartItemDao().getCartCount(foodDetails.get(i).getName()));
                }
                db.foodDetailsDao().save(foodDetails);
                db.foodDetailsDao().deleteOtherFoods(nameList);
            }else{
                db.foodDetailsDao().deleteAll();
            }
        }
        return null;
    }
}
