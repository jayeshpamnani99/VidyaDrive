package com.example.harshit.vidyadrive;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChaptersActivity extends AppCompatActivity {
    RecyclerView chapterrecycler;
    FirebaseRecyclerAdapter adapter;
    TextView chapwarnid;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        final String chaptitle = getIntent().getExtras().get("subjecttitle").toString();
        chapwarnid = findViewById(R.id.chapwarnid);
        FirebaseDatabase.getInstance().getReference().child("Chapters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(chaptitle)){
                    chapwarnid.setVisibility(View.VISIBLE);
                }else{
                    chapwarnid.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodown);

        Toast.makeText(ChaptersActivity.this,chaptitle,Toast.LENGTH_SHORT).show();
        chapterrecycler = findViewById(R.id.chapterrecyclerid);
        chapterrecycler.setHasFixedSize(true);
        chapterrecycler.setLayoutManager(new LinearLayoutManager(this));
        Query query = FirebaseDatabase.getInstance().getReference().child("Chapters").child(chaptitle);
        FirebaseRecyclerOptions<Chapters> options =
                new FirebaseRecyclerOptions.Builder<Chapters>()
                        .setQuery(query, Chapters.class)
                        .build();
            query.keepSynced(true);
        adapter = new FirebaseRecyclerAdapter<Chapters, ChaptersHolder>(options) {
            @Override
            public ChaptersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chapterscard, parent, false);

                return new ChaptersHolder(view);
            }

            @Override
            protected void onBindViewHolder(final ChaptersHolder holder, int position, Chapters model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.chatpterstitle.setText(model.getChaptertitle());
                holder.chaptercardview.setAnimation(animation);

                holder.chatpterstitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent uploadsIntent = new Intent(ChaptersActivity.this,UploadsActivity.class);
                        uploadsIntent.putExtra("chaptername",holder.chatpterstitle.getText());

                        startActivity(uploadsIntent);

                    }
                });
            }
        };
        chapterrecycler.setAdapter(adapter);
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

    private static class ChaptersHolder extends RecyclerView.ViewHolder {


        CardView chaptercardview;

        TextView chatpterstitle;
        ChaptersHolder(View itemView) {
            super(itemView);

            chaptercardview = itemView.findViewById(R.id.chapterscardviewid);
            chatpterstitle = itemView.findViewById(R.id.chapterstitleid);
        }
    }
}
