package com.javier.justdeliveroo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.javier.justdeliveroo.R;
import com.javier.justdeliveroo.conexion.checkConexion;
import com.javier.justdeliveroo.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity{

    TextView login_txt, welcome_txt;
    EditText usr, passw;
    Button login, reg;
    LoginViewModel lvm;

    //Firebase
    FirebaseAuth firebaseAuth;
    //Utilizamos la librería awesomeValidation para validar los campos del formulario
    AwesomeValidation awesomeValidation;

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Base);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        //Recogemos los elementos de la vista
        login_txt = findViewById(R.id.login_txt);
        welcome_txt= findViewById(R.id.login_txt_2);
        //Campos de meter texto
        usr= findViewById(R.id.login_usr);
        passw= findViewById(R.id.login_passw);
        //buttons
        login= findViewById(R.id.login_btn);
        reg= findViewById(R.id.passw_btn);


        //COMENTAR ANTES DE ENTREGAR (SOLO PRUEBAS)
        //rellena(usr, passw);

        //Iniciamos los procesos
        firebaseAuth = FirebaseAuth.getInstance();

        lvm = new LoginViewModel(getApplication());
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.login_usr, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.login_passw,".{6,}",R.string.invalid_password);


        //Para los botones creamos listeners
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(awesomeValidation.validate()) {
                    //Recogemos su imput
                    String usrname = usr.getText().toString();
                    String pass = passw.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(usrname, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Control de errores
                            if(!(task.isSuccessful())){
                                String errorCode;
                                boolean networkAvailable = checkConexion.isConnected(LoginActivity.this);

                                if (!networkAvailable) {
                                  //  Toast.makeText(LoginActivity.this, "No estás conectado a Internet", Toast.LENGTH_SHORT).show();
                                    errorCode= ((FirebaseNetworkException) task.getException()).getMessage();
                                }
                                else {
                                    errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                    //Función auxiliar que devuelve un texto en función del código del error
                                }
                                    lvm.getError(errorCode, LoginActivity.this, usr, passw);

                            }
                            else{
                                //Vamos a home y pasamos las credenciales por intent
                                Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                                //intent.putExtra("user", usrname);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else {
                    //Si no ha pasado por el validador, es que falta campos
                    Toast.makeText(LoginActivity.this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Para registrarse
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
                startActivity(intent);
            }
        });
    }

    //Función que rellena los campos para hacer pruebas más rápido
    private void rellena(EditText e1, EditText e2){
        e1.setText("j@m.com");
        e2.setText("123456");
    }
}
