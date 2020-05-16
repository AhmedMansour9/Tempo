package com.tempo.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import com.tempo.Model.Gallery;
import com.tempo.Interface.Open_Galler_View;
import com.tempo.R;

public class Slider_Adapter extends RecyclerView.Adapter<Slider_Adapter.MyViewHolder>{

    private List<Gallery> filteredList=new ArrayList<>();
    SharedPreferences.Editor share;

    public static String TotalPrice;
    View itemView;
    Context con;
    String prrice;
    Open_Galler_View delete_galler_view;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView T_Name,T_Discrption,T_Model,T_Price,count;
        ImageView mobile;
        ProgressBar progressBar;
        ImageView btncart;
        public ImageView plus,minus,delete;
        ImageView imggg;
        public MyViewHolder(View view) {
            super(view);
//            T_Name = view.findViewById(R.id.T_Name);
            imggg=view.findViewById(R.id.viewPagerItem_image1);

        }


    }

    public Slider_Adapter(List<Gallery> list, Context context){
        this.filteredList=list;
        this.con=context;
    }
    public void DeleteImage(Open_Galler_View delete_galler_view){
        this.delete_galler_view=delete_galler_view;

    }

    @Override
    public Slider_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.banner, parent, false);
        return new Slider_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Slider_Adapter.MyViewHolder holder, final int position) {


        String i = filteredList.get(position).getImg();
        Uri u = Uri.parse(i);
//        holder.progressBar.setVisibility(View.VISIBLE);


        Glide.with(con)
                .load(u)
                .apply(new RequestOptions())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            holder.ProgrossSpare.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }

                })
                .into(holder.imggg);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_galler_view.delete(filteredList.get(position).getLinl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
