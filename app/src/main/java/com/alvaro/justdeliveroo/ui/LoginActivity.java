package com.alvaro.justdeliveroo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alvaro.justdeliveroo.R;
import com.alvaro.justdeliveroo.conexion.checkConexion;
import com.alvaro.justdeliveroo.viewmodel.UserViewModel;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class LoginActivity extends AppCompatActivity{
    public final String TAG="LoginActivity";
    TextView login_txt, welcome_txt;
    EditText usr, passw;
    Button login, reg;
    UserViewModel userViewModel;

    //Firebase
    FirebaseAuth firebaseAuth;
    //Utilizamos la librería awesomeValidation para validar los campos del formulario
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Base);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //Recogemos los elementos de la vista
        login_txt = findViewById(R.id.login_txt);
        welcome_txt= findViewById(R.id.login_txt_2);
        //Campos de meter texto
        usr= findViewById(R.id.login_usr);
        passw= findViewById(R.id.login_passw);
        //buttons
        login= findViewById(R.id.login_btn);
        reg= findViewById(R.id.passw_btn);

        //Iniciamos los procesos
        firebaseAuth = FirebaseAuth.getInstance();

        userViewModel = new UserViewModel(getApplication());
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.login_usr, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.login_passw,".{6,}",R.string.invalid_password);

        //Para los botones creamos listeners
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(awesomeValidation.validate()) {
                    //Recogemos user inpuit
                    String usrname = usr.getText().toString();
                    String pass = passw.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(usrname, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        userLogged(firebaseAuth.getCurrentUser());
                                        //task.getResult().getCredential().getSignInMethod();
                                    }
                                    else{
                                        //Control de errores
                                        String errorCode;
                                        boolean networkAvailable = checkConexion.isConnected(LoginActivity.this);
                                        if (!networkAvailable) {
                                            errorCode= ((FirebaseNetworkException) task.getException()).getMessage();
                                        }
                                        else {
                                            errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                        }
                                        userViewModel.getError(errorCode, LoginActivity.this, usr, passw);
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

    protected void onStart() {
        super.onStart();
        //Comprobamos si el usuario está logueado
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null){
            userLogged(currentUser);
        }
    }

    private void userLogged(FirebaseUser currentUser){
         currentUser.getIdToken(true).addOnCompleteListener(this, new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                Log.d(TAG,"My token: " + task.getResult().getToken());

            }
        });

        //Vamos a home y pasamos las credenciales por intent
        Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
        intent.putExtra("user", currentUser);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
