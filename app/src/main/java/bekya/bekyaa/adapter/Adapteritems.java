package bekya.bekyaa.adapter;

/**
 * Created by HP on 31/03/2018.
 */

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bekya.bekyaa.Home;
import bekya.bekyaa.Interface.itemViewinterface;
import bekya.bekyaa.ProductList;
import bekya.bekyaa.R;
import bekya.bekyaa.Model.Retrivedata;


public class Adapteritems extends RecyclerView.Adapter<Adapteritems.MyViewHolder>{

    itemViewinterface itemclick;
    List<Retrivedata> array=new ArrayList<>();
    public imgclick btnclick;
  Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public ImageView image,imageedit,imgdelete;
        CardView itemcard;
        TextView textname,textdiscraption,textdiscount,textphone,textdate,textadmin;
        public MyViewHolder(View view) {
            super(view);
            image =  view.findViewById(R.id.product_image);
            textadmin=view.findViewById(R.id.admintext);
            imageedit=view.findViewById(R.id.imageedit);
            imgdelete=view.findViewById(R.id.imagedeleteee);
            textname= view.findViewById(R.id.product_name);
            textdiscraption= view.findViewById(R.id.discount);
            textdiscount= view.findViewById(R.id.discraptionn);
            textphone= view.findViewById(R.id.textphone);
            textdate=view.findViewById(R.id.textdate);
            itemcard=view.findViewById(R.id.itemcard);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(itemclick!=null)
            itemclick.Callback(view,getAdapterPosition());

        }
    }
    public Adapteritems( List<Retrivedata> moviesList , Context context) {
        this.array = moviesList;
        this.context=context;
        setHasStableIds(true);
    }
    public void setClickListener(itemViewinterface itemClickListener) {
        this.itemclick = itemClickListener;
    }
    public void setClickButton(imgclick btnclic){
        this.btnclick=btnclic;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

    Retrivedata y=array.get(position);





       Boolean admin=y.getAdmin();
       if(admin){
           holder.itemcard.setBackgroundColor(Color.RED);
           holder.textname.setTextColor(Color.WHITE);
           holder.textdate.setTextColor(Color.WHITE);
           holder.textdiscount.setTextColor(Color.WHITE);
           holder.textdiscraption.setTextColor(Color.WHITE);
           holder.textadmin.setVisibility(View.VISIBLE);

       }else {

       }

       String token=y.getToken();
       if(token.equals(Home.token)){
           holder.imageedit.setVisibility(View.VISIBLE);
           holder.imgdelete.setVisibility(View.VISIBLE);
       }else {
           holder.imageedit.setVisibility(View.INVISIBLE);
           holder.imgdelete.setVisibility(View.INVISIBLE);
       }

        String textdate=y.getDate();
        holder.textdate.setText(textdate);

        String text=y.getName();
        holder.textname.setText(text);

        String textdiscrp=y.getDiscrption();
        holder.textdiscraption.setText(textdiscrp);

        String textdiscount=y.getDiscount();
        holder.textdiscount.setText(textdiscount);

        String textphone=y.getPhone();
        holder.textphone.setText(textphone);

        String img1 = y.getImg1();
        String img2 = y.getImg2();
        String img3 = y.getImg3();
        String img4 = y.getImg4();

        if(img1!=null) {
            Uri u = Uri.parse(img1);
             Picasso.with(context)
                     .load(u)
                     .fit()
                     .placeholder(R.drawable.no_media)
                     .into(holder.image);
         }else if(img2!=null){
            Uri u = Uri.parse(img2);
            Picasso.with(context)
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(holder.image);

        }else if(img3!=null){
            Uri u = Uri.parse(img3);
            Picasso.with(context)
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(holder.image);
        }else if(img4!=null){
            Uri u = Uri.parse(img4);
            Picasso.with(context)
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(holder.image);

        }else {
            holder.image.setImageResource(R.drawable.no_media);
        }
       holder.imageedit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
             btnclick.onClickCallback(view,position);
           }
       });
        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnclick.onClickdelete(view,position);
            }
        });


    }
    @Override
    public int getItemCount() {
        return array.size();
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




