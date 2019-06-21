package bekya.bekyaa.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

import bekya.bekyaa.Model.Category;
import bekya.bekyaa.R;

/**
 * Created by HP on 05/06/2018.
 */

public class ImageAdapterGride extends BaseAdapter {
    private Context context;
    private List<Category> lis=new ArrayList<>();

    public ImageAdapterGride(Context context, List<Category> liscatgory) {
        this.context = context;
        this.lis = liscatgory;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;


            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.itemgrildviewhome, null);
            Category c = lis.get(position);

            ImageView imgview = gridView.findViewById(R.id.full_image_view);
            String i = c.getImg();
            Uri u = Uri.parse(i);
            Picasso.with(context)
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(imgview);
//
//        Glide.with(context)
//                .load(u)
//                .apply(new RequestOptions().override(800, 400))
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
////                            holder.ProgrossSpare.setVisibility(View.GONE);
//                        return false;
//                    }
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
////                            holder.ProgrossSpare.setVisibility(View.GONE);
//                        return false;
//                    }
//                })
//                .into(imgview);
           TextView text=gridView.findViewById(R.id.text);
           text.setText(c.getCatogories());





        return gridView;
    }

    @Override
    public int getCount() {
        return lis.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
