package com.tempomena.Activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.tempomena.Fragments.ChatUsers;
import com.tempomena.Interface.btnclicks;
import com.tempomena.Interface.imageclick;
import com.tempomena.Model.Retrivedata;
import com.tempomena.R;
import com.tempomena.adapter.Banner_Adapter;
import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityOneItem extends AppCompatActivity implements imageclick, btnclicks {
    List<Retrivedata> array;
    public RecyclerView recyclerView;
    private static final int REQUEST_CALL = 1;
    Banner_Adapter banner_adapter;
    String child,childadmiin;
    TextView textprice, textdiscrp, Title, textdate, textgovern;
    LinearLayoutManager linearLayoutManager;
    CircleIndicator circleIndicator;
    Retrivedata set;
    private static final String movieUrl = "";
    Timer timer;
    List<Retrivedata> sliders =new ArrayList();
    String tokenUser,SocialId;
    ViewPager vp_slider;
    AdView adView;
    String Img1,Img2,Img3,Img4=null;
    String ImgAdmin1,ImgAdmin2,ImgAdmin3,ImgAdmin4=null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/whatsappbold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activityitem);
        vp_slider=findViewById(R.id.vp_slider);
        set = new Retrivedata();
        array = new ArrayList<>();
        adView=findViewById(R.id.adView);
        Title = findViewById(R.id.Title);
        textdiscrp = findViewById(R.id.textdiscrp);
        textprice = findViewById(R.id.textprice);
        circleIndicator=findViewById(R.id.view_pager_circle_indicator);
        //textphone = findViewById(R.id.textphone);
        textdate = findViewById(R.id.textdate);
        textgovern = findViewById(R.id.textgovern);
        FloatingActionButton floatingTextButton = findViewById(R.id.floating_action_button);
        floatingTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makecall();
            }
        });
        FloatingActionButton floatingT = findViewById(R.id.floating_Message);

         tokenUser = getIntent().getStringExtra("tokenuser");
        SocialId = getIntent().getStringExtra("social");

        String discrption = getIntent().getStringExtra("discrp");
        String currency = getIntent().getStringExtra("cu");
        String price = getIntent().getStringExtra("discount");
        String date = getIntent().getStringExtra("date");
        String govern = getIntent().getStringExtra("govern");

        Title.setText( getIntent().getStringExtra("name"));
        textgovern.setText(govern);
        textdiscrp.setText(discrption);
      //  textdiscount.setText(discount);
        textprice.setText(price+" "+currency);
        textdate.setText(date);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
       getdata();
//       getdatafromadmin();


        floatingT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(ActivityOneItem.this, ChatUsers.class);
               intent.putExtra("tokenuser",tokenUser);
                intent.putExtra("social",SocialId);


               startActivity(intent);
            }
        });

//                .addMedia(MediaInfo.mediaLoader(new DefaultVideoLoader(movieUrl, R.mipmap.default_video)))
//                .addMedia(infos);
    }

    private void makecall() {
        String phone = getIntent().getStringExtra("phone");
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityOneItem.this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL);


            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        } else {
            startActivity(intent);
        }
    }
    private Bitmap toBitmap(int image) {
        return ((BitmapDrawable) getResources().getDrawable(image)).getBitmap();
    }





    public void getdata(){
        Img1=null;
        Img2=null;
        Img3=null;
        Img4=null;

        String key=getIntent().getStringExtra("key");
        child=getIntent().getStringExtra(key);
            DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("NewProducts");
            data.orderByChild("img1").equalTo(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.hasChild("img1")) {
                            Img1 = child.child("img1").getValue().toString();
                        }
                        if (child.hasChild("img2")) {
                            Img2 = child.child("img2").getValue().toString();
                        }
                        if (child.hasChild("img3")) {
                            Img3 = child.child("img3").getValue().toString();
                        }
                        if (child.hasChild("img4")) {
                            Img4 = child.child("img4").getValue().toString();
                        }
//                    f.Call(set);
                    }
//                mSwipeRefreshLayout.setRefreshing(false);
                    if (Img1 != null) {
                        set = new Retrivedata();
                        set.setImg1(Img1);
                        array.add(set);
                    }
                    if (Img2 != null) {
                        set = new Retrivedata();
                        set.setImg1(Img2);
                        array.add(set);
                    }
                    if (Img3 != null) {
                        set = new Retrivedata();
                        set.setImg1(Img3);
                        array.add(set);
                    }
                    if (Img4 != null) {
                        set = new Retrivedata();
                        set.setImg1(Img4);
                        array.add(set);
                    }
//                Toast.makeText(ActivityOneItem.this, ""+sliders.size(), Toast.LENGTH_SHORT).show();
                    banner_adapter = new Banner_Adapter(ActivityOneItem.this, array);

                    vp_slider.setAdapter(banner_adapter);
                    circleIndicator.setViewPager(vp_slider);
//                timer = new Timer();
//                timer.scheduleAtFixedRate(new SliderTimer(), 3000, 5000);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }
    public interface firebase{
        void Call(Retrivedata r);
    }
    public void getdatafromadmin(){
        String key=getIntent().getStringExtra("key");
        childadmiin=getIntent().getStringExtra("childadmin");
        if(childadmiin!=null) {
            DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Products").child(childadmiin);
            data.orderByChild("img1").equalTo(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.hasChild("img1")) {
                            ImgAdmin1 = child.child("img1").getValue().toString();
                        }
                        if (child.hasChild("img2")) {
                            ImgAdmin2 = child.child("img2").getValue().toString();
                        }
                        if (child.hasChild("img3")) {
                            ImgAdmin3 = child.child("img3").getValue().toString();
                        }
                        if (child.hasChild("img4")) {
                            ImgAdmin4 = child.child("img4").getValue().toString();
                        }
//                    f.Call(set);
                    }
//                mSwipeRefreshLayout.setRefreshing(false);
                    if (ImgAdmin1 != null) {
                        set = new Retrivedata();
                        set.setImg1(ImgAdmin1);
                        array.add(set);
                    }
                    if (ImgAdmin2 != null) {
                        set = new Retrivedata();
                        set.setImg1(ImgAdmin2);
                        array.add(set);
                    }
                    if (ImgAdmin3 != null) {
                        set = new Retrivedata();
                        set.setImg1(ImgAdmin3);
                        array.add(set);
                    }
                    if (ImgAdmin4 != null) {
                        set = new Retrivedata();
                        set.setImg1(ImgAdmin4);
                        array.add(set);
                    }
                    banner_adapter = new Banner_Adapter(ActivityOneItem.this, array);

                    vp_slider.setAdapter(banner_adapter);
                    circleIndicator.setViewPager(vp_slider);
//                timer = new Timer();
//                timer.scheduleAtFixedRate(new SliderTimer(), 3000, 5000);

//                mSwipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void Callnext(View view, int poistion) {
        int totalItemCount = recyclerView.getAdapter().getItemCount();
        if (totalItemCount <= 0) return;
        int lastVisibleItemIndex = linearLayoutManager.findLastVisibleItemPosition();

        if (lastVisibleItemIndex >= totalItemCount) return;
        linearLayoutManager.smoothScrollToPosition(recyclerView,null,lastVisibleItemIndex+1);

    }

    @Override
    public void Callbacks(View view, int poistion) {
        int firstVisibleItemIndex = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (firstVisibleItemIndex > 0) {
            linearLayoutManager.smoothScrollToPosition(recyclerView,null,firstVisibleItemIndex-1);
        }

    }

    @Override
    public void Callback(View view, int postion) {
        String id=array.get(postion).getImg1();
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.windowimage);
        ImageView img=dialog.findViewById(R.id.imagefullscreen);

//        Picasso.with(this)
//                .load(id)
//                .fit()
//                .placeholder(R.drawable.no_media)
//                .into(img);
        Glide.with(this)
                .load(id)
                .apply(new RequestOptions().placeholderOf(R.drawable.no_media))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            holder.ProgrossSpare.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            holder.ProgrossSpare.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img);
          dialog.show();

    }


//    private class SliderTimer extends TimerTask {

//        @Override
//        public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (vp_slider.getCurrentItem() < sliders.size() - 1) {
//                            vp_slider.setCurrentItem(vp_slider.getCurrentItem() + 1);
//                        } else {
//                            vp_slider.setCurrentItem(0);
//                        }
//
//                    }
//                });
//
//        }
//    }
}
