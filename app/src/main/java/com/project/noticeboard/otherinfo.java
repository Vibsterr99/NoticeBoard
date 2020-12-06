package com.project.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.noticeboard.databinding.ActivityOtherinfoBinding;

import java.util.HashMap;
import java.util.Map;

public class otherinfo extends AppCompatActivity {
    ActivityOtherinfoBinding binding;
    private static final String TAG = "otherinfo";

    private static final String key_username = "username";
    private static final String key_college = "college name";
    private static final String key_hostel = "hostel";
    private static final String key_uid = "uid";
    private static final String key_name = "name";
    private static final String key_mail = "email";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void save(View view) {
        String username = binding.username.getText().toString();
        String college = binding.college.getText().toString();
        String hostel = binding.hostel.getText().toString();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(college) || TextUtils.isEmpty(hostel) || TextUtils.isEmpty(uid)) {
            Toast.makeText(this, "Please Fill All Details!", Toast.LENGTH_SHORT).show();
        } else{
            Map<String, Object> otherinfo = new HashMap<>();

            otherinfo.put(key_uid, uid);
            otherinfo.put(key_name, name);
            otherinfo.put(key_mail, mail);
            otherinfo.put(key_username, username);
            otherinfo.put(key_college, college);
            otherinfo.put(key_hostel, hostel);


            db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(otherinfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(otherinfo.this, "Information Saved", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onSuccess: otherinfo saved");
                            Intent intent = new Intent(getApplicationContext(), mainNoticeBoard.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(otherinfo.this, "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: otherinfo not saved");
                            recreate();
                        }
                    });

            }

        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOtherinfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        GoogleSignInAccount signInAccount= GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount!=null){
            binding.name.setText(signInAccount.getDisplayName());


        }




    }
}