package com.alvaro.justdeliveroo.utility;

import java.util.Observable;

/**
 * Clase que permite actualizar un objeto de la clase Observable
 * @see java.util.Observable
 * */
public class ObservableObject extends Observable {

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
