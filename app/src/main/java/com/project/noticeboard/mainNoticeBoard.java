package com.project.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.noticeboard.databinding.ActivityMainNoticeBoardBinding;
import com.project.noticeboard.databinding.ActivityOtherinfoBinding;

public class mainNoticeBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainNoticeBoardBinding binding;

    private DrawerLayout drawer;
    private NavigationView navigationview;
    private TextView username;
    private TextView email;
    private static final String TAG="mainNoticeBoard";
    private static final String key_username = "username";
    private static final String key_mail = "email";


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseFirestore db;

        db=FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            String name=documentSnapshot.getString(key_username);
                            String mail=documentSnapshot.getString(key_mail);
                            username.setText(name);
                            email.setText(mail);


                        }
                        else{
                            Toast.makeText(mainNoticeBoard.this, "Sorry, your information is not saved!", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),otherinfo.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mainNoticeBoard.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onFailure: "+e.toString());
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainNoticeBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        LinearLayout rl = findViewById(R.id.headerlayout);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.header_navbar, null);



        username=vi.findViewById(R.id.header_username);
        email=vi.findViewById(R.id.header_mail);



        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=binding.drawerLayout;
        navigationview=binding.navView;
        navigationview.bringToFront();

        navigationview.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null)
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new homefrag()).commit();
        navigationview.setCheckedItem(R.id.nav_home);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new homefrag()).commit();

                break;
            case R.id.nav_hostel:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new hostelfrag()).commit();

                break;
            case R.id.nav_post:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new postfrag()).commit();

                break;
            case R.id.nav_logout:

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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