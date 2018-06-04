package bekya.bekyaa.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bekya.bekyaa.Interface.btnclicks;
import bekya.bekyaa.Interface.imageclick;
import bekya.bekyaa.R;
import bekya.bekyaa.Retrivedata;

/**
 * Created by HP on 31/05/2018.
 */

public class AdapterOneitem extends RecyclerView.Adapter<AdapterOneitem.MyViewHolder>{

    List<Retrivedata> array=new ArrayList<>();
    imageclick img;
    btnclicks btnclick;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView image;
        ImageView btnnext,btnpervious;
        public MyViewHolder(View view) {
            super(view);
            image =  view.findViewById(R.id.imageon);
            btnnext=view.findViewById(R.id.playnext);
            btnpervious=view.findViewById(R.id.playpervious);

            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            if(img!=null) img.Callback(view,getAdapterPosition());
        }
    }
    public AdapterOneitem(List<Retrivedata> moviesList , Context context) {
        this.array = moviesList;
        this.context=context;
        setHasStableIds(true);
    }
    public void setClickList(btnclicks itemClickListener) {
        this.btnclick = itemClickListener;
    }

    public void setClickListener(imageclick itemClickListener) {
        this.img = itemClickListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemrecycleherozantle, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Retrivedata ha=array.get(position);

        String i = ha.getImg1();
        Uri u = Uri.parse(i);
        if(i!=null) {
            Picasso.with(context)
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(holder.image);
        }
        holder.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            btnclick.Callnext(view,position);

            }
        });
        holder.btnpervious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 btnclick.Callbacks(view,position);

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
