package com.example.harshit.vidyadrive;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadsActivity extends AppCompatActivity {
    FloatingActionButton uploadfab;
    RecyclerView uploadrecycler;
    Uri pdfUri;
    FirebaseRecyclerAdapter adapter;
    String chaptertitle;
    ProgressDialog progressDialog;
    String downloadUrl;
            String filetitle;
            TextView nouploads;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        chaptertitle = getIntent().getExtras().get("chaptername").toString();

        uploadrecycler = findViewById(R.id.uploadsrecyclerid);
        uploadfab = findViewById(R.id.adduploadid);
        storage = FirebaseStorage.getInstance();
            nouploads = findViewById(R.id.nouploadstextwarnid);



        uploadrecycler.setHasFixedSize(true);
        uploadrecycler.setLayoutManager(new LinearLayoutManager(this));

        Query query = FirebaseDatabase.getInstance().getReference().child("Uploads").child(chaptertitle);
        query.keepSynced(true);
        FirebaseDatabase.getInstance().getReference().child("Uploads").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(chaptertitle)){
                    nouploads.setVisibility(View.VISIBLE);
                }else{
                    nouploads.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Uploads>().setQuery(query,Uploads.class).build();

        adapter = new FirebaseRecyclerAdapter<Uploads, UploadsHolder>(options) {
            @NonNull
            @Override
            public UploadsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.uploadcard, parent, false);

                return new UploadsHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final UploadsHolder holder, final int position, @NonNull Uploads model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.uploadstitled.setText(model.getUploadstitle());
                    holder.uploadstitled.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String post_key = getRef(position).getKey();
                            FirebaseDatabase.getInstance().getReference().child("Uploads").child(chaptertitle).child(post_key).child("downloadUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String url = dataSnapshot.getValue().toString();
                                    Intent pdfintent = new Intent(UploadsActivity.this,pdfActivity.class);
                                    pdfintent.putExtra("urls",url);
                                    startActivity(pdfintent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    });
            }
        };

        uploadrecycler.setAdapter(adapter);
        uploadfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(UploadsActivity.this,android.Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
                    selectpdf();
                }else{
                    ActivityCompat.requestPermissions(UploadsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }


            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectpdf();
        }else{
            Toast.makeText(UploadsActivity.this,"Please provide permission ",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void uploadpdf(Uri pdfUri) {

        progressDialog = new ProgressDialog(UploadsActivity.this);


        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setTitle("Uploading....");

        progressDialog.show();
        final String fileName = chaptertitle;

        final StorageReference storageReference = storage.getReference();
        storageReference.child("Uploads").child(filetitle).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                final String key = FirebaseDatabase.getInstance().getReference().child("Uploads").child(chaptertitle).push().getKey();
                storageReference.child("Uploads").child(filetitle).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downuri = uri;
                        downloadUrl = uri.toString();
                        FirebaseDatabase.getInstance().getReference().child("Uploads").child(chaptertitle).child(key).child("uploadstitle").setValue(filetitle);
                        FirebaseDatabase.getInstance().getReference().child("Uploads").child(chaptertitle).child(key).child("downloadUrl").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(UploadsActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.hide();
                                }
                            }
                        });
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                int currentprogress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentprogress);
            }
        });


    }
    private void selectpdf(){






        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==86 && resultCode==RESULT_OK && data!=null ){
            filetitle = data.getData().getLastPathSegment();
            pdfUri = data.getData();
            uploadpdf(pdfUri);
        }else{

            Toast.makeText(UploadsActivity.this,"Please select pdf file", Toast.LENGTH_LONG).show();
            }
    }

    private static class UploadsHolder extends RecyclerView.ViewHolder {

        TextView uploadstitled;
        ImageButton uploaddownloadbutton;
        UploadsHolder(View itemView) {
            super(itemView);


            uploadstitled = itemView.findViewById(R.id.uploadstitleid);
            uploaddownloadbutton = itemView.findViewById(R.id.downloaduploadButtonid);
        }
    }
}
