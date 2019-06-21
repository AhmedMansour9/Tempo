package bekya.bekyaa;

import android.app.Activity;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bekya.bekyaa.Interface.btnclicks;
import bekya.bekyaa.Interface.imageclick;
import bekya.bekyaa.Model.Retrivedata;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityOneItem extends AppCompatActivity implements imageclick, btnclicks {
    List<Retrivedata> array;
    public RecyclerView recyclerView;
    private static final int REQUEST_CALL = 1;
    private Context context;
    String child;
    TextView textprice, textdiscrp, textphone, textdate, textgovern;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Retrivedata set;
    private static final String movieUrl = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4";
    String img1, img2, img3, img4;
    private ScrollGalleryView scrollGalleryView;
    String tokenUser;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-Bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activityitem);
        set = new Retrivedata();
        array = new ArrayList<>();
      //  textname = findViewById(R.id.textname);
        textdiscrp = findViewById(R.id.textdiscrp);
        textprice = findViewById(R.id.textprice);
        //textphone = findViewById(R.id.textphone);
        textdate = findViewById(R.id.textdate);
        textgovern = findViewById(R.id.textgovern);
        FloatingTextButton floatingTextButton = findViewById(R.id.makecall);
        floatingTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makecall();
            }
        });
        FloatingTextButton floatingT = findViewById(R.id.makechat);

         tokenUser = getIntent().getStringExtra("tokenuser");
        String discrption = getIntent().getStringExtra("discrp");
       // String discount = getIntent().getStringExtra("discount");
        String price = getIntent().getStringExtra("discount");
        String date = getIntent().getStringExtra("date");
        String govern = getIntent().getStringExtra("govern");

        //textname.setText(name);
        textgovern.setText(govern);
        textdiscrp.setText(discrption);
      //  textdiscount.setText(discount);
        textprice.setText(price + " ج.م");
        textdate.setText(date);
        getdata(new firebase() {
            @Override
            public void Call(Retrivedata r) {
                ShowImage(r.getImg1(),r.getImg2(),r.getImg3(),r.getImg4());

            }
        });
        floatingT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(ActivityOneItem.this,ChatUsers.class);
               intent.putExtra("tokenuser",tokenUser);
               startActivity(intent);
            }
        });
        getdatafromadmin(new firebase() {
            @Override
            public void Call(Retrivedata r) {
                ShowImage(r.getImg1(),r.getImg2(),r.getImg3(),r.getImg4());
            }
        });
//                .addMedia(MediaInfo.mediaLoader(new DefaultVideoLoader(movieUrl, R.mipmap.default_video)))
//                .addMedia(infos);
    }
    public void ShowImage(final String img1, String img2, String img3, String img4){
//        List<MediaInfo> infos = new ArrayList<>(images.size());

//        for (String url : images) infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));

        scrollGalleryView = (ScrollGalleryView) findViewById(R.id.scroll_gallery_view);
        if(img1!=null&&img2!=null&&img3!=null&&img4!=null) {
            scrollGalleryView
                    .setThumbnailSize(100)
                    .setZoom(true)
                    .setFragmentManager(getSupportFragmentManager())
                    .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader(img1)))
                    .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader((img2))))
                    .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader((img3))))
                    .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader((img4))));
        }
        if(img1!=null&&img2!=null&&img3!=null&&img4==null){
            scrollGalleryView
                    .setThumbnailSize(100)
                    .setZoom(true)
                    .setFragmentManager(getSupportFragmentManager())
                    .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader(img1)))
                    .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader((img2))))
                    .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader((img3))));

        }
        if(img1!=null&&img2!=null&&img3==null&&img4==null){
            scrollGalleryView
                    .setThumbnailSize(100)
                    .setZoom(true)
                    .setFragmentManager(getSupportFragmentManager())
                    .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader(img1)))
                    .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader((img2))));
        }
        if(img1!=null&&img2==null&&img3==null&&img4==null){
            scrollGalleryView
                    .setThumbnailSize(100)
                    .setZoom(true)
                    .setFragmentManager(getSupportFragmentManager())
                    .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader(img1)));
        }if(img1==null&&img2==null&&img3==null&&img4==null){
            scrollGalleryView
                    .setThumbnailSize(100)
                    .setZoom(true)
                    .setFragmentManager(getSupportFragmentManager())
                    .addMedia(MediaInfo.mediaLoader(new MediaLoader() {
                        @Override
                        public boolean isImage() {
                            return true;
                        }

                        @Override
                        public void loadMedia(Context context, ImageView imageView,
                                              MediaLoader.SuccessCallback callback) {
                            imageView.setImageBitmap(toBitmap(R.drawable.no_media));
//                                Picasso.with(context)
//                                        .load(img1)
//                                        .fit()
//                                        .placeholder(R.drawable.no_media)
//                                        .into(imageView);
                            callback.onSuccess();
                        }

                        @Override
                        public void loadThumbnail(Context context, ImageView thumbnailView,
                                                  MediaLoader.SuccessCallback callback) {
                            thumbnailView.setVisibility(View.GONE);
                        }
                    }));
        }

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

    private  final ArrayList<String> images = new ArrayList<>(Arrays.asList(
            img1,
            img2,
            img3
    ));




    public void getdata(final firebase f){
        String key=getIntent().getStringExtra("key");
        child=getIntent().getStringExtra("child");

        DatabaseReference data= FirebaseDatabase.getInstance().getReference().child("Products").child(child);
        data.orderByChild("img1").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.hasChild("img1")) {

                        set.setImg1(child.child("img1").getValue().toString());
                        array.add(set);
//                        mAdapter.notifyDataSetChanged();
                    }
                    if(child.hasChild("img2")) {

                        set.setImg2(child.child("img2").getValue().toString());
                        array.add( set);
//                        mAdapter.notifyDataSetChanged();
                    }
                    if(child.hasChild("img3")) {

                        set.setImg3(child.child("img3").getValue().toString());
                        array.add( set);
//                        mAdapter.notifyDataSetChanged();
                    }
                    if(child.hasChild("img4")) {

                        set.setImg4(child.child("img4").getValue().toString());
                        array.add( set);
//                        mAdapter.notifyDataSetChanged();
                    }
                    f.Call(set);
                }
//                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public interface firebase{
        void Call(Retrivedata r);
    }
    public void getdatafromadmin(final firebase f){
        String key=getIntent().getStringExtra("key");
        child=getIntent().getStringExtra("childadmin");

        DatabaseReference data= FirebaseDatabase.getInstance().getReference().child("Products").child(child);
        data.orderByChild("img1").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.hasChild("img1")) {

                        set.setImg1(child.child("img1").getValue().toString());
                        array.add(set);
//                        mAdapter.notifyDataSetChanged();
                    }
                    if(child.hasChild("img2")) {

                        set.setImg2(child.child("img2").getValue().toString());
                        array.add( set);
//                        mAdapter.notifyDataSetChanged();
                    }
                    if(child.hasChild("img3")) {

                        set.setImg3(child.child("img3").getValue().toString());
                        array.add( set);
//                        mAdapter.notifyDataSetChanged();
                    }
                    if(child.hasChild("img4")) {

                        set.setImg4(child.child("img4").getValue().toString());
                        array.add( set);
//                        mAdapter.notifyDataSetChanged();
                    }
                    f.Call(set);
                }
//                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
}
