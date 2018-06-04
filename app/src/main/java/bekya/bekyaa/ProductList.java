package bekya.bekyaa;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bekya.bekyaa.Interface.itemViewinterface;
import bekya.bekyaa.Model.Retrivedata;
import bekya.bekyaa.adapter.Adapteritems;
import bekya.bekyaa.adapter.GalleryAdapter;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,itemViewinterface {

    RecyclerView recyclerView;
    RelativeLayout rootlayout;
    Uri saveUri;
    Button btnUpload;
    Button btnselect;
    SwipeRefreshLayout mSwipeRefreshLayout;
EditText name,descrip , discount, price;
    GridView gridGallery;
    Handler handler;
    GalleryAdapter adapter;
    List<Retrivedata> array;
    ArrayList<String> listimages=new ArrayList<>();
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;
    DatabaseReference data;
    private Adapteritems mAdapter;
    FirebaseStorage storage;
    StorageReference storageRef;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editt;
    String text;
    View update_info_layout;
    ArrayList<CustomGallery> dataT;
    String Name,Discrption,Discount,Price;


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
        setContentView(R.layout.activity_product_list);
        handler = new Handler();
        array=new ArrayList<>();
        data= FirebaseDatabase.getInstance().getReference().child("Products");
        storage = FirebaseStorage.getInstance();
        rootlayout = findViewById(R.id.rootlayout);
        editor = getApplicationContext().getSharedPreferences("Photo", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

        initImageLoader();


        Recyclview();
        SwipRefresh();
        FloatingTextButton floatingTextButton = findViewById(R.id.fabbutton);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabbutton);
        floatingTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showaddFooddialog();
            }
        });



    }
    public void SwipRefresh(){
        mSwipeRefreshLayout =  findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                Retrivedata();
            }
        });
    }
    public void Recyclview(){
        recyclerView =findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        mAdapter = new Adapteritems(array,ProductList.this);
        mAdapter.setClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }
    public void Retrivedata(){
        array.clear();
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(true);
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    Retrivedata r = dataSnapshot.getValue(Retrivedata.class);

                    array.add(0, r);
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }
    private void showaddFooddialog() {
        Dialog update_info_layout = new Dialog(ProductList.this);
        update_info_layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        update_info_layout.setContentView(R.layout.layout_add_product);
       name = update_info_layout.findViewById(R.id.Name);
       descrip = update_info_layout.findViewById(R.id.descrip);
        discount = update_info_layout.findViewById(R.id.discount);
        price = update_info_layout.findViewById(R.id.price);

        gridGallery =update_info_layout.findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        viewSwitcher =update_info_layout.findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        btnUpload = update_info_layout.findViewById(R.id.btnupload);
       btnselect = update_info_layout.findViewById(R.id.btnselect);

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadimage();
            }
        });

        update_info_layout.show();
    }

    private void uploadimage() {
        Name = name.getText().toString().trim();
        Discrption = descrip.getText().toString().trim();
        Discount = discount.getText().toString().trim();
        Price = price.getText().toString().trim();

        if (Name.isEmpty() || Discrption.isEmpty() || Discount.isEmpty() || Price.isEmpty()) {
            Toast.makeText(getBaseContext(), "من فضلك أملآ جميع البيانات", Toast.LENGTH_SHORT).show();

        }else if(dataT==null){
            SavedSahredPrefrenceSwitch(Name, Discrption, Discount, Price);
        }
        else {
            if (dataT != null) {
                for (int i = 0; i < dataT.size(); i++) {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    storageRef = storage.getReferenceFromUrl("gs://bekya-5f805.appspot.com/");
                    Uri file = Uri.fromFile(new File(dataT.get(i).sdcardPath));
                    StorageReference imageRef = storageRef.child("images" + "/" + file + ".jpg");
                    imageRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.e("ss", "onSuccess: " + taskSnapshot);
                            progressDialog.dismiss();
                            Uri u = taskSnapshot.getDownloadUrl();
                            Gson i = new Gson();
                            listimages.add(u.toString());
                            String jsonFavorites = i.toJson(listimages);
                            editor.putString("img", jsonFavorites);
                            editor.commit();
                            final int pos = dataT.size();
                            int y = listimages.size();

                            if (pos == y) {
                                progressDialog.dismiss();
                                SavedSahredPrefrenceSwitch(Name, Discrption, Discount, Price);
                                Toast.makeText(getApplicationContext(), "Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                }
                            });
                }
            }
        }
    }

    private void chooseImage() {
        Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
        startActivityForResult(i, 200);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            if (all_path.length > 4) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
                Toast.makeText(this, "Choose 4 images Only", Toast.LENGTH_SHORT).show();
            }

            dataT = new ArrayList<CustomGallery>();

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;


                dataT.add(item);
            }
            viewSwitcher.setDisplayedChild(0);
            adapter.addAll(dataT);

        }

        }

public void SavedSahredPrefrenceSwitch(String name,String discroption,String discount,String phone){

    SharedPreferences sharedPref =getSharedPreferences("Photo", MODE_PRIVATE);
    String jsonFavorit = sharedPref.getString("img", null);
    Gson gson3 = new Gson();
    String[] favoriteIte = gson3.fromJson(jsonFavorit,String[].class);
    Retrivedata r=new Retrivedata();
    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    if(jsonFavorit!=null){

    int postion = favoriteIte.length;
    if(postion==4) {
        r.setImg1(favoriteIte[0]);
        r.setImg2(favoriteIte[1]);
        r.setImg3(favoriteIte[2]);
        r.setImg4(favoriteIte[3]);
        r.setName(name);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setPhone(phone);
        r.setDate(date);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منجك بنجاح", Snackbar.LENGTH_SHORT)
                .show();

    }
    if (postion == 3) {
        r.setImg1(favoriteIte[0]);
        r.setImg2(favoriteIte[1]);
        r.setImg3(favoriteIte[2]);
        r.setName(name);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setPhone(phone);
        r.setDate(date);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منجك بنجاح", Snackbar.LENGTH_SHORT)
                .show();

    }
    if (postion == 2){
        r.setImg1(favoriteIte[0]);
        r.setImg2(favoriteIte[1]);
        r.setName(name);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setPhone(phone);
        r.setDate(date);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منجك بنجاح", Snackbar.LENGTH_SHORT)
                .show();

    }
    if(postion==1) {
        r.setImg1(favoriteIte[0]);
        r.setName(name);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setPhone(phone);
        r.setDate(date);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منجك بنجاح", Snackbar.LENGTH_SHORT)
                .show();

    }}else {
        r.setName(name);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setPhone(phone);
        r.setDate(date);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منجك بنجاح", Snackbar.LENGTH_SHORT)
                .show();
    }

}

    @Override
    public void Callback(View v, int poistion) {
        Intent inty=new Intent(ProductList.this,ActivityOneItem.class);
        if(array.get(poistion).getImg1()!=null) {
            inty.putExtra("key", array.get(poistion).getImg1());
            inty.putExtra("name", array.get(poistion).getName());
            inty.putExtra("discrp", array.get(poistion).getDiscrption());
            inty.putExtra("discount", array.get(poistion).getDiscount());
            inty.putExtra("phone", array.get(poistion).getPhone());
            inty.putExtra("date", array.get(poistion).getDate());
            startActivity(inty);
        }

    }

    @Override
    public void onRefresh() {
        Retrivedata();
    }
}
