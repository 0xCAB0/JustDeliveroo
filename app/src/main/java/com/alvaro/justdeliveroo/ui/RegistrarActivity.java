package com.alvaro.justdeliveroo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.alvaro.justdeliveroo.R;
import com.alvaro.justdeliveroo.conexion.checkConexion;
import com.alvaro.justdeliveroo.viewmodel.LoginViewModel;

public class RegistrarActivity extends AppCompatActivity {
    TextView login_txt, welcome_txt;
    EditText usr, passw;
    Button login;

    //Firebase
    FirebaseAuth firebaseAuth;
    //Utilizamos la librería awesomeValidation para validar los campos del formulario
    AwesomeValidation awesomeValidation;
    LoginViewModel lvm;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        //Recogemos los elementos de la vista
        login_txt = findViewById(R.id.reg_txt);
        welcome_txt= findViewById(R.id.reg_txt_2);
        //Campos de meter texto
        usr= findViewById(R.id.reg_usr);
        passw= findViewById(R.id.reg_passw);
        //buttons
        login= findViewById(R.id.reg_btn);

        //Iniciamos los procesos
        firebaseAuth = FirebaseAuth.getInstance();

        lvm = new LoginViewModel(getApplication());
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.login_usr, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.login_passw,".{6,}",R.string.invalid_password);

        //Si el usuario intenta crear una cuenta:
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Recogemos su imput
                String usrname = usr.getText().toString();
                String pass = passw.getText().toString();
                
                //Lo pasamos por el validator
                if(awesomeValidation.validate()){
                    firebaseAuth.createUserWithEmailAndPassword(usrname,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegistrarActivity.this, "¡Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                String errorCode;
                                boolean networkAvailable = checkConexion.isConnected(RegistrarActivity.this);

                                if (!networkAvailable) {
                                    //  Toast.makeText(LoginActivity.this, "No estás conectado a Internet", Toast.LENGTH_SHORT).show();
                                    errorCode= ((FirebaseNetworkException) task.getException()).getMessage();
                                }
                                else {
                                    errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                    //Función auxiliar que devuelve un texto en función del código del error
                                }
                                lvm.getError(errorCode, RegistrarActivity.this, usr, passw);
                            }
                        }
                    });
                }else {
                    //Si no ha pasado por el validador, es que falta campos
                    Toast.makeText(RegistrarActivity.this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
