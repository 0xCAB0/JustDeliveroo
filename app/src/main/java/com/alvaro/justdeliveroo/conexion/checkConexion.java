package com.alvaro.justdeliveroo.conexion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//Clase que permite conocer el estado de la conexión a internet
public class checkConexion {
   public static boolean isConnected(Context context) {
      ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo wifiCon = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
      NetworkInfo mobCon = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

      return (wifiCon != null && wifiCon.isConnected()) || (mobCon != null && mobCon.isConnected());
   }
}
