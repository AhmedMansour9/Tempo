package bekya.bekyaa.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.ChildEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bekya.bekyaa.Interface.Categories_View;
import bekya.bekyaa.Model.Category;
import bekya.bekyaa.R;

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


        holder.Name.setText(filteredList.get(position).getCatogories());
        if(row_index==position){
            categories_view.cat(filteredList.get(position).getCatogories(),filteredList.get(position).getCategory());
            holder.views.setVisibility(View.VISIBLE);
            holder.Name.setTextColor(con.getResources().getColor(R.color.darkblue));
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
                categories_view.cat(filteredList.get(position).getCatogories(),filteredList.get(position).getCategory());

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

