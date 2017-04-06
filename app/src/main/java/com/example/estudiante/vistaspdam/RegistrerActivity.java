package com.example.estudiante.vistaspdam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by estudiante on 4/5/17.
 */

public class RegistrerActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);



    }


    public void signUp(View v){
        TextView firstName = (TextView) findViewById(R.id.editText);
        TextView LastName = (TextView) findViewById(R.id.editText2);
        TextView email = (TextView) findViewById(R.id.editText3);
        TextView password = (TextView) findViewById(R.id.editText4);
        TextView verifyPassword = (TextView) findViewById(R.id.editText5);


        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

            }
        });




        onBackPressed();
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }
}





