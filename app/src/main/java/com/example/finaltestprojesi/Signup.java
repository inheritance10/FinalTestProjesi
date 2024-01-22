package com.example.finaltestprojesi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText editTextFirstname, editTextLastname, editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        editTextFirstname = findViewById(R.id.editTextFirstname);
        editTextLastname = findViewById(R.id.editTextLastname);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button login_btn = findViewById(R.id.loginButton);
        Button signup_btn = findViewById(R.id.signupButton);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kullanıcının girdiği bilgileri al
                String firstname = editTextFirstname.getText().toString().trim();
                String lastname = editTextLastname.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Firebase Authentication kullanarak kullanıcıyı kaydet
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Kayıt başarılı ise kullanıcıyı giriş ekranına yönlendir
                                    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                                    Map<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("firstname", firstname);
                                    userInfo.put("lastname", lastname);
                                    fireStore.collection("users").document(auth.getUid()).set(userInfo)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(Signup.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Signup.this, Login.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Hata durumunda konsola yazdır
                                                    e.printStackTrace();
                                                }
                                            });

                                } else {
                                    // Kayıt başarısız ise hata mesajını konsola yazdır
                                    Exception error = task.getException();
                                    if (error != null) {
                                        error.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        });
    }
}
