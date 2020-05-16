package com.tempo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.tempo.Model.Retrivedata;
import com.tempo.R;


public class Banner_Adapter extends PagerAdapter {
    Context context;
    int[]images;
    String []Names;
    String []Descriptions;
    LayoutInflater layoutInflater;
    // int position=3;
    private List<Retrivedata> homeSliderDataList=new ArrayList<>();

    public Banner_Adapter(Context context, List<Retrivedata> list) {
        this.context = context;
        this.homeSliderDataList=list;
    }

    @Override
    public int getCount() {
        return homeSliderDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==( (LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView;
        TextView textNames;
        TextView textDescriptions;
//        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=LayoutInflater.from(container.getContext()).inflate(R.layout.slider,container,false );
        imageView=view.findViewById( R.id.viewPagerItem_image1 );
        String img1 = homeSliderDataList.get(position).getImg1();
        String img2 = homeSliderDataList.get(position).getImg2();
        String img3 = homeSliderDataList.get(position).getImg3();
        String img4 = homeSliderDataList.get(position).getImg4();
//        if(img1!=null) {
            Uri u = Uri.parse(img1);
            Glide.with(context)
                    .load( u)
                    .into(imageView);

//        }

//        else if(img2!=null){
//            Uri u = Uri.parse(img2);
//            Glide.with(context)
//                    .load(u)
//
//                    .into(imageView);
//
//        }else if(img3!=null){
//            Uri u = Uri.parse(img3);
//            Glide.with(context)
//                    .load(u)
//                    .into(imageView);
//        }else if(img4!=null){
//            Uri u = Uri.parse(img4);
//            Glide.with(context)
//                    .load(u)
//                    .into(imageView);
//
//        }

        ((ViewPager)container).addView( view );
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        ((ViewPager)container).removeView( (LinearLayout)object );
    }

}



