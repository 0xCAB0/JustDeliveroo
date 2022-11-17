package com.javier.justdeliveroo.utility;

import java.util.Observable;

public class ObservableObject extends Observable {

    /*Clase que permite operar con las instancias que herendan de clases Observable*/
    private static final ObservableObject instance = new ObservableObject();

    public static ObservableObject getInstance() {
        return instance;
    }

    private ObservableObject() {
    }

    public void updateValue(Object data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }
}
