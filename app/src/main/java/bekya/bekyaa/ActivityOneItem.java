package bekya.bekyaa;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;
import com.veinhorn.scrollgalleryview.loader.DefaultVideoLoader;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bekya.bekyaa.Interface.btnclicks;
import bekya.bekyaa.Interface.imageclick;
import bekya.bekyaa.Model.Retrivedata;
import bekya.bekyaa.adapter.AdapterOneitem;

public class ActivityOneItem extends AppCompatActivity implements  imageclick,btnclicks {
    List<Retrivedata> array;
    public RecyclerView recyclerView;
    private AdapterOneitem mAdapter;
    TextView textprice;
    TextView textname, textdiscrp, textdiscount, textphone, textdate;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Retrivedata set;
    private static final String movieUrl = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4";
    String img1,img2,img3,img4;
    private ScrollGalleryView scrollGalleryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityitem);
        set=new Retrivedata();
        array = new ArrayList<>();
//        textname = findViewById(R.id.textname);
//        textdiscrp = findViewById(R.id.textdiscrp);
//        textdiscount = findViewById(R.id.textdiscount);
//        textphone = findViewById(R.id.textphone);
//        textdate = findViewById(R.id.textdate);

//        String name = getIntent().getStringExtra("name");
//        String discrption = getIntent().getStringExtra("discrp");
//        String discount = getIntent().getStringExtra("discount");
//        String phone = getIntent().getStringExtra("phone");
//        String date = getIntent().getStringExtra("date");
//        textname.setText(name);
//        textdiscrp.setText(discrption);
//        textdiscount.setText(discount);
//        textphone.setText(phone);
//        textdate.setText(date);
//        Recyclview();
//        SwipRefresh();
        getdata(new firebase() {
            @Override
            public void Call(Retrivedata r) {
                img1=r.getImg1();
                img2=r.getImg2();
                img3=r.getImg3();
                List<MediaInfo> infos = new ArrayList<>(images.size());

                for (String url : images) infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));

                scrollGalleryView = (ScrollGalleryView) findViewById(R.id.scroll_gallery_view);
                scrollGalleryView
                        .setThumbnailSize(100)
                        .setZoom(true)
                        .setFragmentManager(getSupportFragmentManager())
                        .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader(img1)))
                        .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader((img2))))
                        .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader((img3))))
                        .addMedia(MediaInfo.mediaLoader(new PicassoImageLoader((img4))))


                        .addMedia(MediaInfo.mediaLoader(new MediaLoader() {
                            @Override
                            public boolean isImage() {
                                return true;
                            }

                            @Override
                            public void loadMedia(Context context, ImageView imageView,
                                                  MediaLoader.SuccessCallback callback) {
//                        imageView.setImageBitmap(toBitmap(R.drawable.wallpaper3));
//                                Picasso.with(context)
//                                        .load(img1)
//                                        .fit()
//                                        .placeholder(R.drawable.no_media)
//                                        .into(imageView);
//                                callback.onSuccess();
                            }

                            @Override
                            public void loadThumbnail(Context context, ImageView thumbnailView,
                                                      MediaLoader.SuccessCallback callback) {
//                                thumbnailView.setImageBitmap(toBitmap(R.drawable.wallpaper3));
//                                Picasso.with(context)
//                                        .load(img1)
//                                        .fit()
//                                        .placeholder(R.drawable.no_media)
//                                        .into(thumbnailView);
//                                callback.onSuccess();
                            }
                        }));

            }
        });

//                .addMedia(MediaInfo.mediaLoader(new DefaultVideoLoader(movieUrl, R.mipmap.default_video)))
//                .addMedia(infos);
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
        String child=getIntent().getStringExtra("child");
//        array.clear();
//        mAdapter.notifyDataSetChanged();
//        mSwipeRefreshLayout.setRefreshing(true);

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
    public void Recyclview(){
//        recyclerView =findViewById(R.id.recycler);
//        recyclerView.setHasFixedSize(true);
        mAdapter = new AdapterOneitem(array,ActivityOneItem.this);
         mAdapter.setClickListener(this);
         mAdapter.setClickList(this);
//         linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);

    }
//    public void SwipRefresh(){
//        mSwipeRefreshLayout =  findViewById(R.id.swipe_container);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);
//
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                getdata();
//            }
//        });
//    }
//
//    @Override
//    public void onRefresh() {
//        getdata();
//    }

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

        Picasso.with(this)
                .load(id)
                .fit()
                .placeholder(R.drawable.no_media)
                .into(img);

          dialog.show();

    }
}
