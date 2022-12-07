package com.alvaro.justdeliveroo.API;

import com.alvaro.justdeliveroo.model.Comida;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface getJSON {
    //Request GET para obtener los datos de la bbdd en formato JSON
    @GET("/Comida.json")
    Call<List<Comida>> getFoodData();
}
