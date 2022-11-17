package com.javier.justdeliveroo.db;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.javier.justdeliveroo.R;
import com.javier.justdeliveroo.model.ItemCarrito;
import com.javier.justdeliveroo.model.Comida;

/*Clase estandar de invocación a una bbdd implementada con room*/

@Database(entities = {Comida.class, ItemCarrito.class}, version = 5,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract ComidaDao foodDetailsDao();
    public abstract CartItemDao cartItemDao();

    public static AppDatabase getDatabase(final Context context) {
        String dbname = context.getString(R.string.db_name);
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,dbname).fallbackToDestructiveMigration().allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
