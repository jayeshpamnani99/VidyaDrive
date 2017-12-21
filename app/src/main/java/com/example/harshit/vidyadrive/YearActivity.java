package com.example.harshit.vidyadrive;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class YearActivity extends AppCompatActivity {
    RecyclerView yearrecycler;
    YearAdapter yearAdapter;

    List<Year> yearList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);
        yearList = new ArrayList<>();

        yearrecycler = (RecyclerView) findViewById(R.id.yearrecyclerid);
        yearrecycler.setHasFixedSize(true);
        yearrecycler.setLayoutManager(new LinearLayoutManager(this));


        yearList.add(new Year(
                "First Year BTech",
                "Welcome New Engineers",
                R.drawable.fe


        ));

        yearList.add(new Year(
                "Second Year BTech",
                "Well,Now You Have Gone Acquainted",
                R.drawable.sy


        ));

        yearList.add(new Year(
                "Third Year BTech",
                "These are those ghosts!!",
                R.drawable.ty
        ));
        yearList.add(new Year(
                "Final Year BTech",
                "Hope You Succeed",
                R.drawable.fy

        ));
        yearAdapter = new YearAdapter(this, yearList);
        yearrecycler.setAdapter(yearAdapter);

    }

}
