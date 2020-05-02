package com.bagas.tanganicovid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;

    AwesomeValidation awesomeValidation;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
//        btnLogin = findViewById(R.id.btn_login);

//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
//                if(mFirebaseUser != null){
//                    Intent i = new Intent(MainActivity.this, MenuUtama.class);
//                    startActivity(i);
//                    Toast.makeText(MainActivity.this, "Selamat datang kembali", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//        };




    }

    public void lupapassword(View view) {
        Intent lupaPassword = new Intent(this, LupaPassword.class);
        startActivity(lupaPassword);
        finish();
    }

    public void mendaftar(View view) {
        Intent mendaftar  = new Intent(this, PendaftaranUser.class);
        startActivity(mendaftar);
        finish();
    }

    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi keluar aplikasi");
        builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        builder.setMessage("Anda yakin ingin keluar aplikasi ? ");
        builder.setCancelable(false);

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAndRemoveTask();
                finish();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void btnLogin(View view) {


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.et_username,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this, R.id.et_password,
                ".{6,}",R.string.invalid_password);

        if(awesomeValidation.validate()) {

            progressDialog = new ProgressDialog(this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Maaf, Email atau password anda salah, dan Pastikan Anda Terhubung dengan Internet", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Intent i = new Intent(MainActivity.this, MenuUtama.class);
                            startActivity(i);
                            finish();

                        }
                    }
                });

        }


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
//    }
}
