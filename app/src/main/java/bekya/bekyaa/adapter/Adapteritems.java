package bekya.bekyaa.adapter;

/**
 * Created by HP on 31/03/2018.
 */

import android.content.Context;
import android.net.Uri;
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

import bekya.bekyaa.Interface.itemViewinterface;
import bekya.bekyaa.R;
import bekya.bekyaa.Model.Retrivedata;


public class Adapteritems extends RecyclerView.Adapter<Adapteritems.MyViewHolder>{

    itemViewinterface itemclick;
    List<Retrivedata> array=new ArrayList<>();

  Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public ImageView image;
        RelativeLayout rela;
        TextView textname,textdiscraption,textdiscount,textphone,textdate;
        public MyViewHolder(View view) {
            super(view);
            image =  view.findViewById(R.id.product_image);
            textname= view.findViewById(R.id.product_name);
            textdiscraption= view.findViewById(R.id.discount);
            textdiscount= view.findViewById(R.id.discraptionn);
            textphone= view.findViewById(R.id.textphone);
            textdate=view.findViewById(R.id.textdate);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(itemclick!=null)
            itemclick.Callback(view,getAdapterPosition());

        }
    }
    public Adapteritems(List<Retrivedata> moviesList , Context context) {
        this.array = moviesList;
        this.context=context;
        setHasStableIds(true);
    }
    public void setClickListener(itemViewinterface itemClickListener) {
        this.itemclick = itemClickListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       Retrivedata ha=array.get(position);

        String textdate=ha.getDate();
        holder.textdate.setText(textdate);

        String text=ha.getName();
        holder.textname.setText(text);

        String textdiscrp=ha.getDiscrption();
        holder.textdiscraption.setText(textdiscrp);

        String textdiscount=ha.getDiscount();
        holder.textdiscount.setText(textdiscount);

        String textphone=ha.getPhone();
        holder.textphone.setText(textphone);

        String i = ha.getImg1();
            Uri u = Uri.parse(i);
         if(i!=null) {
             Picasso.with(context)
                     .load(u)
                     .fit()
                     .placeholder(R.drawable.no_media)
                     .into(holder.image);
         }



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




