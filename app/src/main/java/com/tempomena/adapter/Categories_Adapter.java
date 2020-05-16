package com.tempomena.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.tempomena.ChangeLanguage;
import com.tempomena.Interface.Categories_View;
import com.tempomena.Model.Category;
import com.tempomena.R;

/**
 * Created by HP on 05/06/2018.
 */

public class Categories_Adapter extends RecyclerView.Adapter<Categories_Adapter.MyViewHolder>{

    private List<Category> filteredList=new ArrayList<>();
    View itemView;
    Context con;
    Categories_View categories_view;
    int row_index=0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView Name;
        View views;
        RelativeLayout relative_row;
        public MyViewHolder(View view) {
            super(view);
            Name=view.findViewById(R.id.Name);
            views=view.findViewById(R.id.view);
            relative_row=view.findViewById(R.id.relative_row);


        }
    }

    public Categories_Adapter(List<Category> list, Context context){
        this.filteredList=list;
        this.con=context;
    }
    //    public Restaurants_Adapter(List<Units_Detail> list){
//        this.filteredList=list;
//
//    }
    public void setClickListener(Categories_View restaurantDetails_view) {
        this.categories_view = restaurantDetails_view;

    }

    @Override
    public Categories_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category, parent, false);
        return new Categories_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Categories_Adapter.MyViewHolder holder, final int position) {

       if(ChangeLanguage.getLanguage(con).equals("en")){
           holder.Name.setText(filteredList.get(position).getCat_en());
       }else {
           holder.Name.setText(filteredList.get(position).getCat_ar());

       }
        if(row_index==position){
            categories_view.cat(filteredList.get(position).getKey());
            holder.views.setVisibility(View.VISIBLE);
            holder.Name.setTextColor(con.getResources().getColor(R.color.holo_blue_bright));
        }
        else
        {
            holder.Name.setTextColor(con.getResources().getColor(R.color.light_black));
            holder.views.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                notifyDataSetChanged();
                categories_view.cat(filteredList.get(position).getKey());

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

