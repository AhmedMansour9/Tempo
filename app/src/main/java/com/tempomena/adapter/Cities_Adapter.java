package com.tempomena.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.tempomena.ChangeLanguage;
import com.tempomena.Interface.CityId_View;
import com.tempomena.Model.Cities_Response;
import com.tempomena.R;

public class Cities_Adapter extends RecyclerView.Adapter<Cities_Adapter.MyViewHolder> {
    CityId_View cityId_view;
    List<Cities_Response> array=new ArrayList<>();
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Title;
        public MyViewHolder(View view) {
            super(view);
            Title=view.findViewById(R.id.Title);

        }

    }
    public Cities_Adapter( List<Cities_Response> moviesList , Context context) {
        this.array = moviesList;
        this.context=context;

    }
    public void setClickListener(CityId_View itemClickListener) {
        this.cityId_view = itemClickListener;
    }


    @Override
    public Cities_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cities, parent, false);
        return new Cities_Adapter.MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(final Cities_Adapter.MyViewHolder holder, final int position) {
     if(ChangeLanguage.getLanguage(context).equals("en")) {
         holder.Title.setText(array.get(position).getName());
     }else {
         holder.Title.setText(array.get(position).getName_ar());

     }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            cityId_view.Id(array.get(position).getName(),array.get(position).getId(),array.get(position).getName_ar());

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




