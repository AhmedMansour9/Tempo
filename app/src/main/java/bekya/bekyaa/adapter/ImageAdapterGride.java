package bekya.bekyaa.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
