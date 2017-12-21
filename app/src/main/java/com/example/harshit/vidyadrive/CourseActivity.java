package com.example.harshit.vidyadrive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {
    RecyclerView courserecycler;
    CourseAdapter courseAdapter;

    List<Course> courseList;
    public String yearTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        courseList = new ArrayList<>();

        courserecycler = (RecyclerView) findViewById(R.id.courserecyclerid);
        courserecycler.setHasFixedSize(true);
        courserecycler.setLayoutManager(new LinearLayoutManager(this));


        if(getIntent().getExtras()!=null){
        yearTitle = getIntent().getExtras().get("yeartitle").toString();}




        courseList.add(new Course(
                "Information Technology",
                "All those coders",
                R.drawable.it


        ));

        courseList.add(new Course(
                "Computer Engineering",
                "All those Developers",
                R.drawable.ce


        ));

        courseList.add(new Course(
                "Mechanical Engineering",
                "All those Workshop",
                R.drawable.me
        ));
        courseList.add(new Course(
                "Electronics Engineering",
                "All those Networkers",
                R.drawable.ee

        ));
        courseList.add(new Course(
                "Electronics and Telecommunication Engineering",
                "All those Networkers ",
                R.drawable.elx

        ));

        courseAdapter = new CourseAdapter(this, courseList);
        courserecycler.setAdapter(courseAdapter);

    }
    public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

        private Context mCtx;
        private List<Course> courseList;



        CourseAdapter(Context mCtx, List<Course> courseList) {
            this.mCtx = mCtx;
            this.courseList = courseList;
        }

        @NonNull
        @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.coursecard,parent,false);
            return new CourseViewHolder(view);


        }

        @Override
        public void onBindViewHolder(@NonNull final CourseViewHolder holder, int position) {
            final Course course = courseList.get(position);
            holder.title1.setText(course.getTitle());
            holder.desc1.setText(course.getDesc());
            holder.yearimage1.setImageDrawable(mCtx.getResources().getDrawable(course.getImage()));
            holder.forwardimage1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.forwardimage1.animate().rotation(360).start();

                    Intent subintent = new Intent(mCtx,SubjectsActivity.class);
                    subintent.putExtra("coursetitle",course.getTitle());
                    subintent.putExtra("yeartitle",yearTitle);
                    mCtx.startActivity(subintent);
                }
            });




        }

        @Override
        public int getItemCount() {
            return courseList.size();
        }

        class CourseViewHolder extends RecyclerView.ViewHolder{
            TextView title1;
            TextView desc1;
            ImageView yearimage1;
            ImageButton forwardimage1;

            CourseViewHolder(View itemView) {
                super(itemView);


                title1 = (TextView) itemView.findViewById(R.id.courstitleid);
                yearimage1 = (ImageView) itemView.findViewById(R.id.imagecour);
                desc1 = (TextView) itemView.findViewById(R.id.coursdescid);
                forwardimage1 =(ImageButton) itemView.findViewById(R.id.imageButton);
            }
        }

    }


}
