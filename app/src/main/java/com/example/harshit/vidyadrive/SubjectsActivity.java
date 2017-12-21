package com.example.harshit.vidyadrive;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubjectsActivity extends AppCompatActivity {
    TextView titletext;
    private String coursetitle;
    RecyclerView subjectRecycler;
    DatabaseReference subjectDatabase;
    FirebaseRecyclerAdapter adapter;
    Animation uptodowna;
    String yeartitle;
    TextView warnSub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        warnSub = findViewById(R.id.subwarnid);
        yeartitle = getIntent().getExtras().get("yeartitle").toString();
        coursetitle = getIntent().getExtras().get("coursetitle").toString();
        titletext = findViewById(R.id.titleid);
        titletext.setText(coursetitle);
        subjectRecycler = findViewById(R.id.subjectrecyclerid);
        subjectRecycler.setHasFixedSize(true);
        subjectRecycler.setLayoutManager(new LinearLayoutManager(this));

        subjectDatabase = FirebaseDatabase.getInstance().getReference();
        uptodowna = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        FirebaseDatabase.getInstance().getReference().child("Year").child(yeartitle).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(coursetitle)){
                    warnSub.setVisibility(View.VISIBLE);
                }else{
                    warnSub.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = FirebaseDatabase.getInstance().getReference().child("Year").child(yeartitle).child(coursetitle).child("Subjects");
        query.keepSynced(true);
final FirebaseRecyclerOptions<Subjects> options =
        new FirebaseRecyclerOptions.Builder<Subjects>()
        .setQuery(query, Subjects.class)
        .build();

        adapter = new FirebaseRecyclerAdapter<Subjects, SubjectsHolder>(options) {
@Override
public SubjectsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new instance of the ViewHolder, in this case we are using a custom
        // layout called R.layout.message for each item
        View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.subjectcard, parent, false);

        return new SubjectsHolder(view);
        }

@Override
protected void onBindViewHolder(final SubjectsHolder holder, int position, final Subjects model) {
        // Bind the Chat object to the ChatHolder
        // ...
        RequestOptions options1 = new RequestOptions();
        options1.centerCrop();
        holder.subjectstitle.setText(model.getSubject());
        holder.subjectstitle.setAnimation(uptodowna);
        holder.subjectsbutton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        holder.subjectsbutton.animate().rotation(360).start();
        Intent gosubjectsintent = new Intent(SubjectsActivity.this,ChaptersActivity.class);
        gosubjectsintent.putExtra("subjecttitle",model.getSubject());
        startActivity(gosubjectsintent);

        }
        });



        }
        };
        subjectRecycler.setAdapter(adapter);



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

public static class SubjectsHolder extends RecyclerView.ViewHolder{
    TextView subjectstitle;

    ImageButton subjectsbutton;

    SubjectsHolder(View itemView) {
        super(itemView);

        subjectstitle = itemView.findViewById(R.id.subjecttextid);

        subjectsbutton = itemView.findViewById(R.id.subjectsimagebuttonid);



    }
}

}

