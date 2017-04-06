package com.example.estudiante.vistaspdam;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.Manifest;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

    }


    @Override
    public void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener( this );
    }

    @Override
    public void onStop()
    {
        super.onStop();
        firebaseAuth.removeAuthStateListener( this );
    }

    public void signUp(View v){
        Intent intent = new Intent(this, RegistrerActivity.class);
        startActivity(intent);
    }

    public void toMap(){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }


    public void signIn(View v){
        TextView email = (TextView) findViewById(R.id.editText6);
        TextView password = (TextView) findViewById(R.id.editText9);


        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    toMap();
                }

            }
        });


    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        /**
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if ( user != null )
        {
            startActivity( new Intent( this, MainActivity.class ) );
            finish();
        }
         **/

    }
}
