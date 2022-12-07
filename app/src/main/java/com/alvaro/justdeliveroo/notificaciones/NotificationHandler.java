package com.alvaro.justdeliveroo.notificaciones;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class NotificationHandler extends ContextWrapper {

    NotificationManager manager;

    public static final String highChannelID = "1";
    public static final String highChannelName = "HIGH CHANNEL";

    public static final String lowChannelID = "2";
    public static final String lowChannelName = "LOW CHANNEL";

    // Grupo notification
    public static final String groupSummary = "GRUPO";
    public static final int groupID= 111;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHandler(Context base) {
        super(base);
        createChannels();
    }

    public NotificationManager getManager () {
        if (manager==null)
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        // Creamos canales
        NotificationChannel canalA = new NotificationChannel(highChannelID, highChannelName, NotificationManager.IMPORTANCE_HIGH);
        NotificationChannel canalB = new NotificationChannel(lowChannelID, lowChannelName, NotificationManager.IMPORTANCE_LOW);

        // Configuramos canales (opcional)
        canalA.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        canalA.setShowBadge(true);
        canalB.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        // Crear canal de notificaciones en el manager
        getManager().createNotificationChannel(canalA);
        getManager().createNotificationChannel(canalB);
    }

    public Notification.Builder createNotification (String title, String msg, boolean priority) {
        if (Build.VERSION.SDK_INT>=26) {
            if (priority) {
                return createNotificationChannels(title, msg, highChannelID);
            } else {
                return createNotificationChannels(title, msg, lowChannelID);
            }
        } else
            return createNotificationWithoutChannels(title, msg);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification.Builder createNotificationChannels (String title, String msg, String channel) {
        Bitmap image = BitmapFactory.decodeResource(getResources(), com.alvaro.justdeliveroo.R.drawable.logo_ok);

        // Crear Action
        Icon icon = Icon.createWithResource(this, com.alvaro.justdeliveroo.R.drawable.ic_launcher_foreground);
        //Notification.Action action = new Notification.Action.Builder (icon, "LANZAR", pit).build();

        return new Notification.Builder(getApplicationContext(), channel)
                .setSmallIcon(com.alvaro.justdeliveroo.R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setGroup(groupSummary)
                .setContentText(msg)
                //.setContentIntent(pit) // Añadimos el pendingIntent a la notificación
                //.setActions(action) // Añadimos la action creada
                .setLargeIcon(image) //Imagen de contacto (similar)
                //.setStyle(new Notification.BigPictureStyle().bigPicture(image).bigLargeIcon((Bitmap) null))// Estilo con imagen
                .setStyle(new Notification.BigTextStyle().bigText(msg))// Estilo con texto grande
                ;

    }

    private Notification.Builder createNotificationWithoutChannels (String title, String msg) {
        return new Notification.Builder(getApplicationContext())
                .setSmallIcon(com.alvaro.justdeliveroo.R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createGroup (boolean priority) {
        String canal = highChannelID;
        if (!priority)
            canal=lowChannelID;
        Notification grupo = new Notification.Builder(this,canal)
                .setGroupSummary(true)
                .setGroup(groupSummary)
                .setSmallIcon(com.alvaro.justdeliveroo.R.drawable.ic_launcher_foreground).build();
        getManager().notify(groupID,grupo);
    }




















}
