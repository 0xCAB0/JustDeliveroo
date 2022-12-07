package com.alvaro.justdeliveroo.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//API client class setting up retrofit client.
public class FirebaseAPI {

    //URL de nuestra BBDD en firebase
    private static final String URL_BBDD = "https://just-deliveroo-default-rtdb.firebaseio.com/";
    private static Retrofit retrofit = null;

    //Obtenemos el Retrofit para llamar a la API usando nuestras credenciales
    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BBDD)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
