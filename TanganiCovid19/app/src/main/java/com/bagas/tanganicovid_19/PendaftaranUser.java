package com.bagas.tanganicovid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PendaftaranUser extends AppCompatActivity {
    EditText etNama, etEmail, etPassword;
    RadioButton rbLaki, rbPerempuan;
    String gender ="";

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
    ProgressDialog progressDialog;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran_user);

        //casting view
        etNama = findViewById(R.id.et_regis_user);
        etEmail = findViewById(R.id.et_regis_email);
        etPassword = findViewById(R.id.et_regis_password);
        rbLaki = findViewById(R.id.laki);
        rbPerempuan = findViewById(R.id.perempuan);


        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void gotologin(View view) {
        Intent gotoLogin = new Intent(this, MainActivity.class);
        startActivity(gotoLogin);
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


    public void btn_mendaftar(View view) {

        awesomeValidation.addValidation(this, R.id.et_regis_user,
                RegexTemplate.NOT_EMPTY,R.string.invalid_name);

        awesomeValidation.addValidation(this, R.id.et_regis_email,
                Patterns.EMAIL_ADDRESS,R.string.invalid_email);

        awesomeValidation.addValidation(this, R.id.et_regis_password,
                ".{6,}",R.string.invalid_password);

        if(!rbLaki.isChecked() && !rbPerempuan.isChecked()) {
            Toast.makeText(this, "Maaf anda harus memilih jenis kelamin", Toast.LENGTH_SHORT).show();
        }



            if(awesomeValidation.validate()) {

                progressDialog = new ProgressDialog(this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                final String username = etNama.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                if (rbLaki.isChecked()) {
                    gender = "Laki-laki";
                }
                if (rbPerempuan.isChecked()) {
                    gender = "Perempuan";
                }


                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(PendaftaranUser.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
//                                    Users information = new Users(
//                                            username, email, gender, password
//                                    );
                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    String email = user.getEmail();
                                    String uid = user.getUid();

                                    HashMap<Object, String> hashMap = new HashMap<>();
                                    hashMap.put("email", email);
                                    hashMap.put("uid", uid);
                                    hashMap.put("username", username);
                                    hashMap.put("gender", gender);
                                    hashMap.put("password", password);
                                    hashMap.put("image", "");
                                    hashMap.put("phone", "");
                                    hashMap.put("cover", "");


                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("User");
                                    reference.child(uid).setValue(hashMap);




//                                   FirebaseDatabase.getInstance().getReference("User")
//                                           .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                           .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                       @Override
//                                       public void onComplete(@NonNull Task<Void> task) {
//                                           progressDialog.dismiss();
//                                            Toast.makeText(PendaftaranUser.this, "Anda berhasil mendaftar", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                       }
//                                   });

                                    progressDialog.dismiss();
                                    Toast.makeText(PendaftaranUser.this, "Anda berhasil mendaftar", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(PendaftaranUser.this, "Maaf, pendaftaran tidak berhasil", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
    }
}
