package com.example.moneytree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.moneytree.Base.FireBaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    Button b1;
    FloatingActionButton floatingActionButton;
    BottomNavigationView bottomNavigationView;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        b1=findViewById(R.id.signoutbutton);
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signout();
//            }
//        });
        ProgressDialog progressDialog1=new ProgressDialog(HomeActivity.this);
        FireBaseHelper.getFireBaseHelper().getUserDocument(HomeActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid());




        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressDialog=new ProgressDialog(HomeActivity.this);
//                progressDialog.show();
//                FireBaseHelper.getFireBaseHelper().getUserDocument(HomeActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(new Intent(HomeActivity.this,Profile.class));


            }
        });
        bottomNavigationView=findViewById(R.id.navigation);

        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(R.id.navigation_join,R.id.navigation_home,R.id.navigation_create).build();
        NavController navController= Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(progressDialog!=null) {
            progressDialog.dismiss();
            progressDialog=null;
        }
    }

    public void signout(){
        FirebaseAuth.getInstance().signOut();

        SharedPreferences.Editor editor=getSharedPreferences("LoginCheck",0).edit();
//        editor.putBoolean("isLoggedIn",false);
//        editor.apply();
//        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        startActivity(new Intent(HomeActivity.this, SigninActivity.class));
    }
}
