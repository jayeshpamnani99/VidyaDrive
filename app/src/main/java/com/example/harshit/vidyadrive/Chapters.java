package com.example.harshit.vidyadrive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class Chapters {
    String chaptertitle;
    public Chapters(){

    }
    public Chapters(String chaptertitle) {
        this.chaptertitle = chaptertitle;
    }

    public String getChaptertitle() {
        return chaptertitle;
    }

    public void setChaptertitle(String chaptertitle) {
        this.chaptertitle = chaptertitle;
    }
}
