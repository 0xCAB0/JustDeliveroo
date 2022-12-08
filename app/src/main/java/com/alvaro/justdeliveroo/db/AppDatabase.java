package com.alvaro.justdeliveroo.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.alvaro.justdeliveroo.R;
import com.alvaro.justdeliveroo.model.Comida;
import com.alvaro.justdeliveroo.model.ItemCarrito;

/*Clase estandar de invocación a una bbdd implementada con room*/

@Database(entities = {Comida.class, ItemCarrito.class}, version = 5,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public static String DBNAME;

    public abstract ComidaDao foodDetailsDao();
    public abstract CartItemDao cartItemDao();

    public static AppDatabase getDatabase(final Context context) {
        DBNAME= context.getString(R.string.db_name);
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,DBNAME).
                            fallbackToDestructiveMigration().allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
