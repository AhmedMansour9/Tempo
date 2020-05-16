package com.tempomena.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tempomena.ChangeLanguage;
import com.tempomena.Interface.CityId_View;
import com.tempomena.Model.SubCategories_Model;
import com.tempomena.R;

import java.util.ArrayList;
import java.util.List;

public class SubCategories_Adapter extends RecyclerView.Adapter<SubCategories_Adapter.MyViewHolder> {
    CityId_View cityId_view;
    List<SubCategories_Model> array=new ArrayList<>();
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Title;
        public MyViewHolder(View view) {
            super(view);
            Title=view.findViewById(R.id.Title);

        }

    }
    public SubCategories_Adapter(List<SubCategories_Model> moviesList , Context context) {
        this.array = moviesList;
        this.context=context;

    }
    public void setClickListener(CityId_View itemClickListener) {
        this.cityId_view = itemClickListener;
    }


    @Override
    public SubCategories_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_subcategories, parent, false);
        return new SubCategories_Adapter.MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(final SubCategories_Adapter.MyViewHolder holder, final int position) {
        if(ChangeLanguage.getLanguage(context).equals("en")) {
            holder.Title.setText(array.get(position).getCat_en());
        }else {
            holder.Title.setText(array.get(position).getCat_ar());

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ChangeLanguage.getLanguage(context).equals("en")) {
                    cityId_view.Id(array.get(position).getSub_key(),array.get(position).getCat_en(),"");
                }else {
                    cityId_view.Id(array.get(position).getSub_key(),array.get(position).getCat_ar(),"");

                }
            }
        });

    }




    @Override
    public int getItemCount() {
        return   array.size();

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
