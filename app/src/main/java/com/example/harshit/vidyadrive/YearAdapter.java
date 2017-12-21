package com.example.harshit.vidyadrive;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;




import java.util.List;

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.YearViewHolder> {

    private Context mCtx;
    private List<Year> yearList;

    YearAdapter(Context mCtx, List<Year> yearList) {
        this.mCtx = mCtx;
        this.yearList = yearList;
    }

    @NonNull
    @Override
    public YearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.yearcard,parent,false);
        return new YearViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final YearViewHolder holder, int position) {
        final Year year = yearList.get(position);
        holder.title.setText(year.getTitle());
        holder.desc.setText(year.getDesc());
        holder.yearimage.setImageDrawable(mCtx.getResources().getDrawable(year.getImage()));
        holder.forwardimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.forwardimage.animate().rotation(360).start();

                Intent subintent = new Intent(mCtx,CourseActivity.class);
                subintent.putExtra("yeartitle",year.getTitle());
                mCtx.startActivity(subintent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return yearList.size();
    }

    class YearViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView desc;
        ImageView yearimage;
        ImageButton forwardimage;

        YearViewHolder(View itemView) {
            super(itemView);


            title = (TextView) itemView.findViewById(R.id.yeartitleid);
            yearimage = (ImageView) itemView.findViewById(R.id.imageyer);
            desc = (TextView) itemView.findViewById(R.id.yeardescid);
            forwardimage =(ImageButton) itemView.findViewById(R.id.imageButton2);
        }
    }
}
