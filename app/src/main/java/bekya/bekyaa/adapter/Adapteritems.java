package bekya.bekyaa.adapter;

/**
 * Created by HP on 31/03/2018.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import bekya.bekyaa.Activites.Home;
import bekya.bekyaa.Interface.itemViewinterface;
import bekya.bekyaa.R;
import bekya.bekyaa.Model.Retrivedata;


public class Adapteritems extends RecyclerView.Adapter<Adapteritems.MyViewHolder>  implements Filterable{
    private List<Retrivedata> mArrayList;
    ArrayList<Integer> list=new ArrayList<>();
    itemViewinterface itemclick;
    List<Retrivedata> array=new ArrayList<>();
    public imgclick btnclick;
  Context context;
  public static TextView textadmin;
   public static List<Retrivedata> filteredList = new ArrayList<>();
    int pos=5;
    AdRequest adRequest;

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                filteredList.clear();
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    array = mArrayList;
                } else {
                    for (Retrivedata androidVersion : mArrayList) {
                        if (androidVersion.getName().toLowerCase().contains(charString)) {
                            filteredList.add(androidVersion);
                        }
                    }
                    array = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = array;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                array = (List<Retrivedata>) filterResults.values;
                // Adapteritems.this.notify();
                array = (List<Retrivedata>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public ImageView image;
        Button imageedit,imgdelete;
        ProgressBar progross;
        private AdView adView;

        CardView itemcard;
      public   TextView textname,textprice,textgovern,descrption,textdate,textadmin;
        public MyViewHolder(View view) {
            super(view);
            image =  view.findViewById(R.id.product_image);
            textadmin=view.findViewById(R.id.admintext);
            imageedit=view.findViewById(R.id.imageedit);
            adView = view.findViewById(R.id.adView);
            imgdelete=view.findViewById(R.id.imagedeleteee);
            textname= view.findViewById(R.id.product_name);
            textprice= view.findViewById(R.id.price);
            textgovern= view.findViewById(R.id.govern);
            descrption= view.findViewById(R.id.descrption);
            textdate=view.findViewById(R.id.textdate);
            itemcard=view.findViewById(R.id.itemcard);
            progross=view.findViewById(R.id.progross);
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
        mArrayList = moviesList;
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
        textadmin=itemView.findViewById(R.id.admintext);
        return new MyViewHolder(itemView);
    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
          if(position==pos){
              pos+=5;

              if(!holder.adView.isShown()) {
                  MobileAds.initialize(context, "ca-app-pub-3940256099942544~3347511713");
                  adRequest = new AdRequest.Builder().build();

                  holder.adView.loadAd(adRequest);
                  holder.adView.setVisibility(View.VISIBLE);
              }

          }else {
//              holder.adView.setVisibility(View.GONE);

          }





        Retrivedata y=array.get(position);

        if(!filteredList.isEmpty()){
            holder.textadmin.setVisibility(View.INVISIBLE);
        }
        if(!filteredList.isEmpty()){
            holder.textadmin.setVisibility(View.INVISIBLE);
        }
            Boolean admin=y.getAdmin();
            if(admin){
                holder.textadmin.setVisibility(View.VISIBLE);

            }else {
                holder.textadmin.setVisibility(View.GONE);
            }

            String token=y.getToken();
            if(token.equals(Home.token)){
                holder.imageedit.setVisibility(View.VISIBLE);
                holder.imgdelete.setVisibility(View.VISIBLE);
            }else {
                holder.imageedit.setVisibility(View.GONE);
                holder.imgdelete.setVisibility(View.GONE);
            }
            holder.descrption.setText(y.getDiscrption());
            String textdate=y.getDate();
            holder.textdate.setText(textdate);

            String text=y.getName();
            holder.textname.setText(text);

            String textprice=y.getDiscount();
            holder.textprice.setText(textprice + " ج.م");

            String textgovern=y.getGovern();
            holder.textgovern.setText(textgovern);

//            String textphone=y.getPhone();
//            holder.textphone.setText(textphone);

            String img1 = y.getImg1();
            String img2 = y.getImg2();
            String img3 = y.getImg3();
            String img4 = y.getImg4();

            if(img1!=null) {
                Uri u = Uri.parse(img1);
               holder.progross.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load( u)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progross.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progross.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.image);

            }else if(img2!=null){
                Uri u = Uri.parse(img2);
                holder.progross.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(u)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progross.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progross.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.image);

            }else if(img3!=null){
                Uri u = Uri.parse(img3);
                holder.progross.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(u)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progross.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progross.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.image);
            }else if(img4!=null){
                Uri u = Uri.parse(img4);
                holder.progross.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(u)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.progross.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progross.setVisibility(View.GONE);
                                return false;
                            }
                        })
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




