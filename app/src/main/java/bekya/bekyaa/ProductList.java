package bekya.bekyaa;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import bekya.bekyaa.Interface.itemViewinterface;
import bekya.bekyaa.Model.Retrivedata;
import bekya.bekyaa.adapter.Adapteritems;
import bekya.bekyaa.adapter.GalleryAdapter;
import bekya.bekyaa.adapter.imgclick;
import bekya.bekyaa.tokenid.SharedPrefManager;
import dmax.dialog.SpotsDialog;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.provider.Settings.System.DATE_FORMAT;

public class ProductList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,itemViewinterface,imgclick{

    RecyclerView recyclerView;
    RelativeLayout rootlayout;
    String Names;
    String Nameone;
    Button btnUpload;
    Button btnselect;
    SwipeRefreshLayout mSwipeRefreshLayout;
EditText name,descrip , phone, price ,govern;
    GridView gridGallery;
    private RequestPermissionListener mRequestPermissionHandler;

    Handler handler;
    ImageView imgone,imgtwo,imgthree,imgfour;
    ImageView deltone,deletetwo,deletethree,deletefour;
    ImageView cameraone,cameratwo,camerathree,camerafour;
    EditText editname,editdiscrp,editdiscount,editphone,editgovern;
    Button finish;
    GalleryAdapter adapter;
    ArrayList<Retrivedata> arrayadmin;
    ArrayList<String> listimages=new ArrayList<>();
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;
    DatabaseReference data,dataadmin;
    private Bitmap bitmap;
    private Adapteritems mAdapter;
    StorageReference storageRef;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editt;
    String imgOne,imgTwo,imgThree,imgFour;
    private Uri filePathone,filePathtwo,filePaththree,filePathfour;
    FirebaseStorage storage;
    StorageReference storageReference;
    byte [] dat;
    public static String token;
    ArrayList<CustomGallery> dataT;
    String Name,Discrption,Phone,Price,Govern;
    String child,childadmin;
    Dialog update_items_layout;
    Dialog update_info_layout;
    Spinner s1, s2;
    public ChildEventListener mListener;
    public static String date2;
    EditText product;
    String state[] = null;
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
        Adapteritems.filteredList.clear();
          token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();
         date2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
        product = findViewById(R.id.findyourproduct);
        handler = new Handler();
        arrayadmin=new ArrayList<>();
        SharedPreferences shared=getSharedPreferences("cat",MODE_PRIVATE);
         child=shared.getString("Category",null);
        data= FirebaseDatabase.getInstance().getReference().child("Products").child(child);
        childadmin=shared.getString("categoryadmin",null);
        data = FirebaseDatabase.getInstance().getReference().child("Products").child(child);
        mRequestPermissionHandler = new RequestPermissionListener();
        dataadmin= FirebaseDatabase.getInstance().getReference().child("Products").child(childadmin);
        storage = FirebaseStorage.getInstance();
        rootlayout = findViewById(R.id.rootlayout);
        editor = getApplicationContext().getSharedPreferences("Photo", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        storageReference = storage.getReference();



        Recyclview();
        SwipRefresh();
        FloatingTextButton floatingTextButton = findViewById(R.id.fabbutton);
        floatingTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showaddFooddialog();
            }
        });

       RecycleviewSerach();
    }
    private void handleButtonClicked(){
        mRequestPermissionHandler.requestPermission(this, new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 200, new RequestPermissionListener.RequestPermissionListene() {
            @Override
            public void onSuccess() {
               // Toast.makeText(ProductList.this, "request permission success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);

            }

            @Override
            public void onFailed() {
                Toast.makeText(ProductList.this, "request permission failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);

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
                Retrivedatauser();
                Retrivedataadmin();

            }
        });
    }
    public void Recyclview(){
        recyclerView =findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        mAdapter = new Adapteritems(arrayadmin,ProductList.this);
        mAdapter.setClickListener(this);
        mAdapter.setClickButton(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }
    private boolean hasId(String idc){
        if(!TextUtils.isEmpty(idc)) {
            for (Retrivedata fr : arrayadmin) {
                if (fr.getName().equals(idc)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
    public void RecycleviewSerach(){
        product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mAdapter.getFilter().filter(charSequence);
                mAdapter.notifyDataSetChanged();



            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    public void Retrivedatauser() {
        mSwipeRefreshLayout.setRefreshing(true);
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Retrivedata r = dataSnapshot.getValue(Retrivedata.class);

                    String Date = r.getDate();
                    int days = GetDays(Date, ProductList.date2);
                    if (days > 350) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        if (r != null && !hasId(r.getName())) {
                            arrayadmin.add(0, r);

                            mAdapter.notifyDataSetChanged();
                        }


                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                } else {
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
    public void Retrivedataadmin() {
        mSwipeRefreshLayout.setRefreshing(true);
        mListener=dataadmin.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Retrivedata r = dataSnapshot.getValue(Retrivedata.class);

                    String Date = r.getDate();
                    int days = GetDays(Date, ProductList.date2);
                    if (days > 350) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        if (r != null && !hasId(r.getName())) {
                            arrayadmin.add(0,r);
                            mAdapter.notifyDataSetChanged();

                        }

                        mSwipeRefreshLayout.setRefreshing(false);

                    }
                } else {
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
         update_info_layout = new Dialog(ProductList.this);
        update_info_layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        initImageLoader();
        update_info_layout.setContentView(R.layout.layout_add_product);
       name = update_info_layout.findViewById(R.id.Name);
       descrip = update_info_layout.findViewById(R.id.descrip);
        phone = update_info_layout.findViewById(R.id.phone);
        price = update_info_layout.findViewById(R.id.price);
        govern = update_info_layout.findViewById(R.id.govern);


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
        Phone = phone.getText().toString().trim();
        Price = price.getText().toString().trim();
        Govern = govern.getText().toString().trim();


        if (Name.isEmpty() || Discrption.isEmpty() || Phone.isEmpty() || Price.isEmpty()||Govern.isEmpty()||token.isEmpty()) {
            Toast.makeText(getBaseContext(), "لازم تكتب كل البيانات", Toast.LENGTH_LONG).show();

        }
        else if(phone.length() < 11 || phone.length() >11 )
        {
            Toast.makeText(getBaseContext(), "لازم يكون رقم التليفون 11 رقم", Toast.LENGTH_LONG).show();

        } else if(price.length() >7 )
        {
            Toast.makeText(getBaseContext(), "لا يمكن زيادة السعر عن 7 أرقام", Toast.LENGTH_LONG).show();

        }
        else if(dataT==null){
            SavedSahredPrefrenceSwitch(Name, Discrption, Phone, Price,Govern);
            update_info_layout.dismiss();
        }
        else {
            if (dataT != null) {
                if (dataT != null) {

                    for (int i = 0; i < dataT.size(); i++) {
                        final SpotsDialog waitingdialog = new SpotsDialog(this);
                        waitingdialog.setTitle("يتم التحميل ..");
                        waitingdialog.show();
                     //   Uri fi = Uri.parse(dataT.get(i).sdcardPath);
                        File imageFile = new File(dataT.get(i).sdcardPath);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),options);
                            //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fi);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                          byte[] dat = bytes.toByteArray();

                        storageRef = storage.getReferenceFromUrl("gs://bekya-5f805.appspot.com/");
                        Uri file = Uri.fromFile(new File(dataT.get(i).sdcardPath));
                        StorageReference imageRef = storageRef.child("images" + "/" + file + ".jpg");
                        UploadTask uploadTask = imageRef.putBytes(dat);


                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.e("ss", "onSuccess: " + taskSnapshot);
                                waitingdialog.dismiss();
                                Uri u = taskSnapshot.getDownloadUrl();
                                Gson i = new Gson();
                                listimages.add(u.toString());
                                String jsonFavorites = i.toJson(listimages);
                                editor.putString("img", jsonFavorites);
                                editor.commit();
                                final int pos = dataT.size();
                                int y = listimages.size();

                                if (pos == y) {
                                    waitingdialog.dismiss();
                                    SavedSahredPrefrenceSwitch(Name, Discrption, Phone, Price, Govern);
                                    update_info_layout.dismiss();
                                    Snackbar.make(rootlayout, "تم إضافة منتجك بنجاح", Snackbar.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        waitingdialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    }
                                });
                    }}
            }
        }
    }

    private void chooseImage() {
handleButtonClicked();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 50 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePathone=null;
            filePathone = data.getData();
            if(filePathone != null)
            {
                String imagechild="img1";
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathone);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                    byte[] dat = bytes.toByteArray();

                    imgfileone(dat,imagechild,imgone,Nameone);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else   if(requestCode == 100 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePathtwo=null;
            filePathtwo = data.getData();
            if(filePathtwo != null)
            {
                String imagechild="img2";
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathtwo);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                    byte[] dat = bytes.toByteArray();

                    imgfileone(dat,imagechild,imgone,Nameone);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        else   if(requestCode == 150 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePaththree=null;
            filePaththree = data.getData();
            if(filePaththree != null)
            {
                String imagechild="img3";
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePaththree);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                    byte[] dat = bytes.toByteArray();

                    imgfileone(dat,imagechild,imgone,Nameone);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }  else   if(requestCode == 250 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePathfour=null;
            filePathfour = data.getData();
            if(filePathfour != null)
            {
                String imagechild="img4";
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathfour);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                    byte[] dat = bytes.toByteArray();

                    imgfileone(dat,imagechild,imgone,Nameone);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");


            if (all_path.length > 4) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
                Toast.makeText(this, "مينفعش أكتر من 4 صور", Toast.LENGTH_LONG).show();
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

public void SavedSahredPrefrenceSwitch(String name,String discroption,String phone,String discount , String govern){

    SharedPreferences sharedPref =getSharedPreferences("Photo", MODE_PRIVATE);
    String jsonFavorit = sharedPref.getString("img", null);
    Gson gson3 = new Gson();
    String[] favoriteIte = gson3.fromJson(jsonFavorit,String[].class);
    Retrivedata r=new Retrivedata();
    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
    if(jsonFavorit!=null){

    int postion = favoriteIte.length;
    if(postion==4) {
        r.setImg1(favoriteIte[0]);
        r.setImg2(favoriteIte[1]);
        r.setImg3(favoriteIte[2]);
        r.setImg4(favoriteIte[3]);
        r.setName(name);
        r.setGovern(govern);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setPhone(phone);
        r.setDate(date);
        r.setToken(token);
        r.setAdmin(false);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منتجك بنجاح", Snackbar.LENGTH_SHORT)
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
        r.setGovern(govern);
        r.setDate(date);
        r.setToken(token);
        r.setAdmin(false);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منتجك بنجاح", Snackbar.LENGTH_SHORT)
                .show();

    }
    if (postion == 2){
        r.setImg1(favoriteIte[0]);
        r.setImg2(favoriteIte[1]);
        r.setName(name);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setGovern(govern);
        r.setPhone(phone);
        r.setDate(date);
        r.setToken(token);
        r.setAdmin(false);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منتجك بنجاح", Snackbar.LENGTH_SHORT)
                .show();

    }
    if(postion==1) {
        r.setImg1(favoriteIte[0]);
        r.setName(name);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setPhone(phone);
        r.setGovern(govern);
        r.setDate(date);
        r.setToken(token);
        r.setAdmin(false);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منتجك بنجاح", Snackbar.LENGTH_SHORT)
                .show();

    }}else {
        r.setName(name);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setPhone(phone);
        r.setDate(date);
        r.setGovern(govern);
        r.setToken(token);
        r.setAdmin(false);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منتجك بنجاح", Snackbar.LENGTH_SHORT)
                .show();
    }

}

    @Override
    public void Callback(View v, int poistion) {

      if(!Adapteritems.filteredList.isEmpty()){
          Intent inty=new Intent(ProductList.this,ActivityOneItem.class);
          inty.putExtra("child",child);
          inty.putExtra("childadmin",childadmin);
          inty.putExtra("key", Adapteritems.filteredList.get(poistion).getImg1());
          inty.putExtra("name", Adapteritems.filteredList.get(poistion).getName());
          inty.putExtra("discrp", Adapteritems.filteredList.get(poistion).getDiscrption());
          inty.putExtra("discount", Adapteritems.filteredList.get(poistion).getDiscount());
          inty.putExtra("phone", Adapteritems.filteredList.get(poistion).getPhone());
          inty.putExtra("date", Adapteritems.filteredList.get(poistion).getDate());
          inty.putExtra("govern", Adapteritems.filteredList.get(poistion).getGovern());

          startActivity(inty);

      }else if(Adapteritems.filteredList.isEmpty()){
          Intent inty=new Intent(ProductList.this,ActivityOneItem.class);
          inty.putExtra("child",child);
          inty.putExtra("childadmin",childadmin);
          inty.putExtra("key", arrayadmin.get(poistion).getImg1());
          inty.putExtra("name", arrayadmin.get(poistion).getName());
          inty.putExtra("discrp", arrayadmin.get(poistion).getDiscrption());
          inty.putExtra("discount", arrayadmin.get(poistion).getDiscount());
          inty.putExtra("phone", arrayadmin.get(poistion).getPhone());
          inty.putExtra("date", arrayadmin.get(poistion).getDate());
          inty.putExtra("govern", arrayadmin.get(poistion).getGovern());
          startActivity(inty);

      }


    }

    @Override
    public void onRefresh() {
        arrayadmin.clear();
        mAdapter.notifyDataSetChanged();
        Retrivedatauser();
        Retrivedataadmin();

    }


    @Override
    public void onClickCallback(View view, int adapterPosition) {
         update_items_layout = new Dialog(ProductList.this);
        update_items_layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        update_items_layout.setContentView(R.layout.edititems);
       init2();
        if(Adapteritems.filteredList.isEmpty()){
            Nameone=arrayadmin.get(adapterPosition).getName();
            imgOne=arrayadmin.get(adapterPosition).getImg1();
            imgTwo=arrayadmin.get(adapterPosition).getImg2();
            imgThree=arrayadmin.get(adapterPosition).getImg3();
            imgFour=arrayadmin.get(adapterPosition).getImg4();
            editname.setText(arrayadmin.get(adapterPosition).getName());
            editdiscrp.setText(arrayadmin.get(adapterPosition).getDiscrption());
            editdiscount.setText(arrayadmin.get(adapterPosition).getDiscount());
            editphone.setText(arrayadmin.get(adapterPosition).getPhone());

        }else if(!Adapteritems.filteredList.isEmpty()){
            Nameone=Adapteritems.filteredList.get(adapterPosition).getName();
            imgOne=Adapteritems.filteredList.get(adapterPosition).getImg1();
            imgTwo=Adapteritems.filteredList.get(adapterPosition).getImg2();
            imgThree=Adapteritems.filteredList.get(adapterPosition).getImg3();
            imgFour=Adapteritems.filteredList.get(adapterPosition).getImg4();
            editname.setText(Adapteritems.filteredList.get(adapterPosition).getName());
            editdiscrp.setText(Adapteritems.filteredList.get(adapterPosition).getDiscrption());
            editdiscount.setText(Adapteritems.filteredList.get(adapterPosition).getDiscount());
            editphone.setText(Adapteritems.filteredList.get(adapterPosition).getPhone());

        }


        if(imgOne!=null){
           Uri u = Uri.parse(imgOne);
           Picasso.with(getApplicationContext())
                   .load(u)
                   .fit()
                   .placeholder(R.drawable.no_media)
                   .into(imgone);

       }else {
          deltone.setVisibility(View.INVISIBLE);
       }

        if(imgTwo!=null){
            Uri u = Uri.parse(imgTwo);
            Picasso.with(getApplicationContext())
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(imgtwo);
        }else {
            deletetwo.setVisibility(View.INVISIBLE);
        }

        if(imgThree!=null){
            Uri u = Uri.parse(imgThree);
            Picasso.with(getApplicationContext())
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(imgthree);
        }else {
            deletethree.setVisibility(View.INVISIBLE);
        }


        if(imgFour!=null){
            Uri u = Uri.parse(imgFour);
            Picasso.with(getApplicationContext())
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(imgfour);
        }else {
            deletefour.setVisibility(View.INVISIBLE);
        }
    clickimgeon();
    clickimgtwo();
    clickimgethree();
    clickimgfour();

    deleteone();
     deletetwo();
     deletethree();
     deletefour();

      btnfinish();


        update_items_layout.show();
    }

    @Override
    public void onClickdelete(View view, final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("عايز تمسح الإعلان ده ؟");
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!Adapteritems.filteredList.isEmpty()){
                    Names =Adapteritems.filteredList.get(adapterPosition).getName();

                }else if(Adapteritems.filteredList.isEmpty()){
                    Names =arrayadmin.get(adapterPosition).getName();

                }
                data.removeEventListener(mListener);
                String Name =arrayadmin.get(adapterPosition).getName();
                if(arrayadmin.get(adapterPosition).getAdmin()){
                    DeletetePost(childadmin,Name);
                }else {
                    DeletetePost(child,Name);
                }

                if(!Adapteritems.filteredList.isEmpty()){
                    Adapteritems.filteredList.remove(adapterPosition);
                    mAdapter.notifyDataSetChanged();


                }else if(Adapteritems.filteredList.isEmpty()){
                    arrayadmin.remove(adapterPosition);
                    mAdapter.notifyDataSetChanged();


                }

                dialog.cancel();
            }
        });
        builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();



    } public void DeletetePost(String child,String Name){
        data.removeEventListener(mListener);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(child);
        databaseReference.orderByChild("name").equalTo(Name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    data.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void init2(){
    imgone=update_items_layout.findViewById(R.id.imgone);
    imgtwo=update_items_layout.findViewById(R.id.imgtwo);
    imgthree=update_items_layout.findViewById(R.id.imgthree);
    imgfour=update_items_layout.findViewById(R.id.imgfour);
    deltone=update_items_layout.findViewById(R.id.deleteone);
    deletetwo=update_items_layout.findViewById(R.id.deletetwo);
    deletethree=update_items_layout.findViewById(R.id.deletethree);
    deletefour=update_items_layout.findViewById(R.id.deletrefour);
    cameraone =update_items_layout.findViewById(R.id.cameraeone);
    cameratwo =update_items_layout.findViewById(R.id.cameratwo);
    camerathree =update_items_layout.findViewById(R.id.camerathree);
    camerafour =update_items_layout.findViewById(R.id.camerafour);
    editname=update_items_layout.findViewById(R.id.editname);
    editdiscrp=update_items_layout.findViewById(R.id.editdiscrp);
    editdiscount=update_items_layout.findViewById(R.id.editdiscount);
    editphone=update_items_layout.findViewById(R.id.editphone);
    finish=update_items_layout.findViewById(R.id.finish);
    }
    public void clickimgeon(){
        cameraone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 50);

            }
        });

    }
    public void clickimgtwo(){
        cameratwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 100);

            }
        });

    }
    public void clickimgethree(){
        camerathree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 150);

            }
        });

    }
    public void clickimgfour(){
        camerafour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 250);

            }
        });
    }

    public void imgfileone(byte [] a,final String childimage, final ImageView image, final String IMAGE) {

        final SpotsDialog waitingdialog = new SpotsDialog(ProductList.this);
        waitingdialog.setTitle("يتم التحميل ..");
        waitingdialog.show();
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        UploadTask uploadTask=ref.putBytes(a);
         uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                waitingdialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                final Uri u = taskSnapshot.getMetadata().getDownloadUrl();
                Picasso.with(getApplicationContext())
                        .load(u)
                        .fit()
                        .placeholder(R.drawable.no_media)
                        .into(image);

                DatabaseReference data=FirebaseDatabase.getInstance().getReference().child("Products").child(child);
                data.orderByChild("name").equalTo(IMAGE).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            data.getRef().child(childimage).setValue(u.toString());

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }
    public void deleteimage(final String childimage){
        data.orderByChild("name").equalTo(Nameone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    data.getRef().child(childimage).removeValue();                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void deleteone(){
        deltone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageone="img1";
                deleteimage(imageone);
                imgone.setImageResource(R.drawable.no_media);
                deltone.setVisibility(View.INVISIBLE);
            }
        });


    }
    public void deletetwo(){
        deletetwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageone="img2";
                deleteimage(imageone);
                imgtwo.setImageResource(R.drawable.no_media);
                deletetwo.setVisibility(View.INVISIBLE);
            }
        });


    }
    public void deletethree(){
        deletethree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageone="img3";
                deleteimage(imageone);
                imgthree.setImageResource(R.drawable.no_media);
                deletethree.setVisibility(View.INVISIBLE);
            }
        });


    }
    public void deletefour(){
        deletefour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageone="img4";
                deleteimage(imageone);
                imgfour.setImageResource(R.drawable.no_media);
                deletefour.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void btnfinish(){
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editname.getText().toString().isEmpty()||editdiscount.getText().toString().isEmpty()||
                        editdiscrp.getText().toString().isEmpty()||editphone.getText().toString().isEmpty() ){
                    Toast.makeText(getApplicationContext(), "لازم تكتب كل البيانات ", Toast.LENGTH_SHORT).show();

                }else {
                    data.orderByChild("name").equalTo(Nameone).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data:dataSnapshot.getChildren()){
                                data.getRef().child("name").setValue(editname.getText().toString());
                                data.getRef().child("discount").setValue(editdiscount.getText().toString());
                                data.getRef().child("discrption").setValue(editdiscrp.getText().toString());
                                data.getRef().child("phone").setValue(editphone.getText().toString());

                            }
                            update_items_layout.dismiss();
                            Adapteritems.filteredList.clear();
                            product.setText("");

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }
        });
    }
    public int GetDays(String dateone,String datetwo){
        String date1 = dateone;
        String date2 =datetwo;
        DateTimeFormatter formatter =  DateTimeFormat.forPattern("dd-MM-yyyy").withLocale(Locale.ENGLISH);
        DateTime d1 = formatter.parseDateTime(date1);
        DateTime d2 = formatter.parseDateTime(date2);
        long diffInMillis = d2.getMillis() - d1.getMillis();

        Duration duration = new Duration(diffInMillis);
        int days = (int) duration.getStandardDays();

        return days;
    }


}
