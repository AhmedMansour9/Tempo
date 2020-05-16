package com.tempomena.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tempomena.Interface.itemViewinterface;
import com.tempomena.Model.Retrivedata;
import com.tempomena.R;
import com.tempomena.Time;
import com.tempomena.Model.meesage;
import com.tempomena.tokenid.SharedPrefManager;

public class Messages extends RecyclerView.Adapter<Messages.MyViewHolder> {
    private List<Retrivedata> mArrayList;
    itemViewinterface itemclick;
    List<meesage> array=new ArrayList<>();
    public imgclick btnclick;
    Context context;
    String LastDate="";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image,imageedit,imgdelete;
        CardView itemcard;
        public   TextView T_user,T_Time,T_user2;
        public MyViewHolder(View view) {
            super(view);
            T_user=view.findViewById(R.id.T_user);
            T_Time=view.findViewById(R.id.T_Time);
            T_user2=view.findViewById(R.id.T_user2);

        }

    }
    public Messages( List<meesage> moviesList , Context context) {
        this.array = moviesList;
        this.context=context;

    }
    public void setClickListener(itemViewinterface itemClickListener) {
        this.itemclick = itemClickListener;
    }
    public void setClickButton(imgclick btnclic){
        this.btnclick=btnclic;
    }

    @Override
    public Messages.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_user, parent, false);
        return new Messages.MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(final Messages.MyViewHolder holder, final int position) {


        String dtStart = array.get(position).getDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = format.parse(dtStart);
            Time a=new Time(context);
            String time= a.timeAgo(date);
            holder.T_Time.setText(time);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //        if(LastDate.equals(datee)){
//            holder.T_Time.setVisibility(View.GONE);
//
//        }else {
//            holder.T_Time.setVisibility(View.VISIBLE);
//            LastDate=datee;
//            holder.T_Time.setText(datee);
//
//        }

        String token= SharedPrefManager.getInstance(context).getSocialId();
        if(array.get(position).getFrom().equals(token)){
            holder.T_user2.setBackground(context.getResources().getDrawable(R.drawable.bcadmin));
            holder.T_user2.setTextColor(Color.BLACK);
            holder.T_user2.setText(array.get(position).getMsg());

        }else {
            holder.T_user.setBackground(context.getResources().getDrawable(R.drawable.bcuser));
            holder.T_user.setTextColor(Color.WHITE);
            holder.T_user.setText(array.get(position).getMsg());


        }


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




