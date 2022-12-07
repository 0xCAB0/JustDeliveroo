package com.alvaro.justdeliveroo.API.datos;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.alvaro.justdeliveroo.db.AppDatabase;
import com.alvaro.justdeliveroo.model.Comida;
import com.alvaro.justdeliveroo.API.FirebaseAPI;
import com.alvaro.justdeliveroo.API.getJSON;
import com.alvaro.justdeliveroo.extras.insertComida;
import com.alvaro.justdeliveroo.extras.UpdateCart;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*Esta clase proporciona los métodos necesarios para obtener los datos de la BBDD
* ubicada en firebase, descargando el archivo JSON y guardando los objetos en una lista*/
public class DatosComida {

    private static DatosComida instance; //DB instance
    private static final String TAG = "RepositorioComida";

    //LLamada a la API -> Obtenemos objeto JSON
    private final getJSON comida = FirebaseAPI.getClient().create(getJSON.class);

    //Method to get network response.
    public MutableLiveData<Boolean> getFoodMenu(final Context context){

        final MutableLiveData<Boolean> isFoodCallOngoing = new MutableLiveData<>();
        isFoodCallOngoing.setValue(true);

        comida.getFoodData().enqueue(new Callback<List<Comida>>() {
            @Override
            public void onResponse(Call<List<Comida>> call, Response<List<Comida>> response) {
                if(response.isSuccessful()) {
                    new insertComida(AppDatabase.getDatabase(context), response.body()).execute();
                    isFoodCallOngoing.setValue(false);
                }else{
                    Log.e("Error:" +TAG,"response not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Comida>> call, Throwable t) {
                Log.e(TAG,t.toString());
            }
        });
        return isFoodCallOngoing;
    }

    //Singleton implementation getInstance method.
    public static DatosComida getInstance() {
        if(instance == null){
            synchronized (DatosComida.class){
                if(instance == null){
                    instance = new DatosComida();
                }
            }
        }
        return instance;
    }

    //Populate the database.
    public void updateCart(final AppDatabase db, Comida comida) {
        new UpdateCart(db).execute(comida);
    }
}
