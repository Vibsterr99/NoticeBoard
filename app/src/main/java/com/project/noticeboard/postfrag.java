package com.project.noticeboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.noticeboard.databinding.FragmentPostfragBinding;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.Timestamp.now;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link postfrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class postfrag<Static> extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG="postfrag";

    public postfrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment postfrag.
     */
    // TODO: Rename and change types and number of parameters
    public static postfrag newInstance(String param1, String param2) {
        postfrag fragment = new postfrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private FragmentPostfragBinding binding;
    public Uri imageuri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Task<Uri> uriurl;
    private String url;
    private Spinner spinner;
    ArrayAdapter<CharSequence>  arrayAdapter;
    private String type;
    private static final String randomKey= UUID.randomUUID().toString();
    private static final String key_time = "time stamp";
    private static final String key_notice = "notice";
    private static final String key_image = "image name";
//    public static Timestamp timestamp= now();
    public static Integer imageflag=0;
    public static String college;
    public static String hostel;
    public static FirebaseFirestore db=FirebaseFirestore.getInstance();
    public String category;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentPostfragBinding.inflate(inflater,container,false);
        View view=binding.getRoot();

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        binding.savepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPicture();
                NoticeUpload();
            }
        });


        binding.imagepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosepicture();
            }
        });


        spinner=binding.spinner;
        arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.cllgtype,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        userview();
    }

    private void choosepicture() {

        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,1);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            imageuri=data.getData();
            binding.imagepost.setImageURI(imageuri);
            Log.i(TAG, "onActivityResult: "+imageuri);
            imageflag=1;

        }

    }

    private void uploadPicture() {

        if (imageflag == 1) {


            StorageReference Ref = storageReference.child("images/" + (FirebaseAuth.getInstance().getCurrentUser().getUid()) + "/" + randomKey);

            Ref.putFile(imageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uriurl = taskSnapshot.getStorage().getDownloadUrl();
                            if (uriurl.isSuccessful()) {
                                url = uriurl.getResult().toString();
                            }

                            
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            
                            Toast.makeText(getActivity(), "Failed To Upload", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void NoticeUpload(){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Posting!");
        pd.show();
        
        
        Map<String, Object> noticepost=new HashMap<>();
        Timestamp timestamp= now();
        noticepost.put(key_time,timestamp);
        String Notice=binding.notice.getText().toString();
        noticepost.put(key_notice,Notice);
        noticepost.put("Category",category);
        if(imageflag==1){
            noticepost.put(key_image,randomKey);
        }
        else if(imageflag==0){
            noticepost.put(key_image,null);
        }
        noticepost.put("College Name",college);
        noticepost.put("Hostel Name",hostel);
        noticepost.put("Key",randomKey);

        String coll="";
        Log.i(TAG, "NoticeUpload: "+category);
//        if(category=="College")
//            coll="College post";
//        if(category=="Hostel")
//            coll="Hostel post";



        db.collection(category).document(UUID.randomUUID().toString()).set(noticepost)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "posted", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: post saved");
                        pd.dismiss();
                        getFragmentManager()
                                .beginTransaction()
                                .detach(postfrag.this)
                                .attach(postfrag.this)
                                .commit();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: not posted");
                    }
                });
    }


    private void userview() {
        db=FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            String name=documentSnapshot.getString("username");
                            binding.user.setText(name);
                            Log.i(TAG, name);
                            college=documentSnapshot.getString("college name");
                            hostel=documentSnapshot.getString("hostel");

                        }
                        else{
                            Toast.makeText(getActivity(), "Sorry, your information is not saved!", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getActivity(),otherinfo.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onFailure: "+e.toString());
                    }
                });
    }


}