package com.example.moneytree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pf=getSharedPreferences("LoginCheck",0);
        if(pf.getBoolean("isLoggedIn",false)) {
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }else {
            startActivity(new Intent(MainActivity.this, SigninActivity.class));
            finish();
        }
    }
}
