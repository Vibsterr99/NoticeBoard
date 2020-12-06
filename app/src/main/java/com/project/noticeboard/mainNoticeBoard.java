package com.project.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.project.noticeboard.databinding.ActivityMainNoticeBoardBinding;
import com.project.noticeboard.databinding.ActivityOtherinfoBinding;

public class mainNoticeBoard extends AppCompatActivity {
    ActivityMainNoticeBoardBinding binding;

    private DrawerLayout drawer;
    private NavigationView navigationview;
    private TextView username;
    private TextView mail;

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainNoticeBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        username=findViewById(R.id.header_username);
        mail=findViewById(R.id.header_mail);


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=binding.drawerLayout;
        navigationview=binding.navView;

        navigationview.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
}