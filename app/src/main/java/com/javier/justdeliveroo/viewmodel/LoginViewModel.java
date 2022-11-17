package com.javier.justdeliveroo.viewmodel;

import android.app.Application;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.javier.justdeliveroo.ui.RegistrarActivity;

public class LoginViewModel extends AndroidViewModel {
    
    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void getError(String error, Context context,EditText usr, EditText passw) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(context, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(context, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(context, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(context, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                usr.setError("La dirección de correo electrónico está mal formateada.");
                usr.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(context, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                passw.setError("la contraseña es incorrecta ");
                passw.requestFocus();
                passw.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(context, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(context,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(context, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(context, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                usr.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                usr.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(context, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(context, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(context, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(context, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(context, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(context, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(context, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                passw.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                passw.requestFocus();
                break;
            case "A network error (such as timeout, interrupted connection or unreachable host) has occurred.":
                Toast.makeText(context, "No estás conectado a Internet, inténtalo más tarde", Toast.LENGTH_LONG).show();
                break;
        }

    }
}
