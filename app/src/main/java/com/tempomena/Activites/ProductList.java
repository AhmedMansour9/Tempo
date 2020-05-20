package com.tempomena.Activites;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.tempomena.ChangeLanguage;
import com.tempomena.Interface.Categories_View;
import com.tempomena.Interface.itemViewinterface;
import com.tempomena.Model.Action;
import com.tempomena.Model.Category;
import com.tempomena.Model.Cities_Response;
import com.tempomena.Model.CustomGallery;
import com.tempomena.Model.Retrivedata;
import com.tempomena.Model.SubCategories_Model;
import com.tempomena.MyVolley;
import com.tempomena.R;
import com.tempomena.RequestPermissionListener;
import com.tempomena.adapter.Adapteritems;
import com.tempomena.adapter.Cities_Adapter;
import com.tempomena.adapter.GalleryAdapter;
import com.tempomena.tokenid.SharedPrefManager;
import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductList extends AppCompatActivity implements Categories_View,SwipeRefreshLayout.OnRefreshListener,
        itemViewinterface{

    RecyclerView recyclerView;
    RelativeLayout rootlayout;
    String Nameone;
    Button btnUpload;
    Button btnselect;
    String Category_Name=null;
    String SubCategory_Name=null;

    String Cat_Id,SubId;
    String Country;
    String Country_Id=null;
EditText name,descrip , phone, price ,govern;
    GridView gridGallery;
    private RequestPermissionListener mRequestPermissionHandler;
    Spinner spinner_country,spinner_Types;
    Handler handler;
    ImageView imgone,imgtwo,imgthree,imgfour;
    Button finish;
    GalleryAdapter adapter;
    ArrayList<Retrivedata> arrayadmin;
    ArrayList<Retrivedata> testArray;
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
    ArrayAdapter<Cities_Response> listCountries;
    ArrayAdapter<String> listTypes;
    ArrayAdapter<Category> listCategories;
    ArrayAdapter<SubCategories_Model> listSubCategories;

    private InterstitialAd mInterstitialAd;
    FirebaseAuth mAuth;

    public static String token;
    ArrayList<CustomGallery> dataT;
    String Name,Discrption,Phone,Price,Govern,Type_id;
    String child,childadmin;
    Dialog update_info_layout;
    List<Category> listcatgory=new ArrayList<>();
    Spinner spinner_Categories,spinner_SubCategories;
    public ChildEventListener mListener;
    EditText product;
    String state[] = null;
    ArrayList<Cities_Response> arrayacities;
    private Cities_Adapter cities_adapter;
    AdView adView;
    TextView Currency;
    public static String Language;
    List<SubCategories_Model> listSubcatgory;

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
        setContentView(R.layout.layout_add_product);
        mAuth = FirebaseAuth.getInstance();
         init();
         getTypes();


    }

    private void getTypes() {

        listTypes = new ArrayAdapter<String>(this, R.layout.spinner_item_selected, getResources().getStringArray(R.array.types)) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                return textView;
            }
        };
        listTypes.setDropDownViewResource(R.layout.customtextcolor);
        spinner_Types.setAdapter(listTypes);
        spinner_Types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String type = spinner_Types.getSelectedItem().toString();
                if(type.equals("Present a service")||type.equals("تقديم خدمة")){
                    Type_id="1";
                }else {
                    Type_id="2";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private  void init(){
        Currency=findViewById(R.id.Currency);
        spinner_Types=findViewById(R.id.spinner_Types);
        Language= ChangeLanguage.getLanguage(this);
        spinner_country=findViewById(R.id.spinner_country);
        spinner_Categories=findViewById(R.id.spinner_Categories);
        spinner_SubCategories=findViewById(R.id.spinner_SubCategories);
        token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();
        product = findViewById(R.id.findyourproduct);
        adView=findViewById(R.id.adView);
        handler = new Handler();
        arrayadmin=new ArrayList<>();
        testArray=new ArrayList<>();
        showaddFooddialog();
        SharedPreferences shared=getSharedPreferences("cat",MODE_PRIVATE);
        listSubcatgory=new ArrayList<>();

        mRequestPermissionHandler = new RequestPermissionListener();
        storage = FirebaseStorage.getInstance();
        rootlayout = findViewById(R.id.rootlayout);
        editor = getApplicationContext().getSharedPreferences("Photo", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        storageReference = storage.getReference();
        RetriveCities();
        RetriveCategories();
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
        initImageLoader();
       name = findViewById(R.id.Name);
       descrip = findViewById(R.id.descrip);
        phone = findViewById(R.id.phone);
        price = findViewById(R.id.price);



        gridGallery =findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        viewSwitcher =findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        btnUpload = findViewById(R.id.btnupload);
       btnselect = findViewById(R.id.btnselect);

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

    }

    public void RetriveCities() {
        arrayacities=new ArrayList<>();
        DatabaseReference data= FirebaseDatabase.getInstance().getReference().child("Cities");
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    Cities_Response r = dataSnapshot.getValue(Cities_Response.class);
                    arrayacities.add(r);
                }

                getCountries(arrayacities);
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

    void getCountries(final List<Cities_Response> tripsData){

        listCountries = new ArrayAdapter<Cities_Response>(this, R.layout.spinner_item_selected, tripsData) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                return textView;
            }
        };
        listCountries.setDropDownViewResource(R.layout.customtextcolor);
        spinner_country.setAdapter(listCountries);
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Country = spinner_country.getSelectedItem().toString();
                if(Language.equals("en")){
                    for (i = 0; i < tripsData.size(); i++) {
                        if (tripsData.get(i).getName().equals(Country)) {
                            Country_Id = String.valueOf(tripsData.get(i).getId());
                            Currency.setText(tripsData.get(i).getCu());
                        }
                    }
                }else {
                    for (i = 0; i < tripsData.size(); i++) {
                        if (tripsData.get(i).getName_ar().equals(Country)) {
                            Country_Id = String.valueOf(tripsData.get(i).getId());
                            Currency.setText(tripsData.get(i).getCu());

                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void RetriveCategories(){

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Category");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    Category c = dataSnapshot.getValue(Category.class);
                    listcatgory.add(c);
                }
              getCategories(listcatgory);

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

    void getCategories(final List<Category> tripsData){

        listCategories = new ArrayAdapter<Category>(this, R.layout.spinner_item_selected, tripsData) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                return textView;
            }
        };
        listCategories.setDropDownViewResource(R.layout.customtextcolor);
        spinner_Categories.setAdapter(listCategories);
        spinner_Categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category_Name = spinner_Categories.getSelectedItem().toString();

                  if(Language.equals("en")){
                      for (i = 0; i < tripsData.size(); i++) {
                          if (tripsData.get(i).getCat_en().equals(Category_Name)) {
                              Cat_Id = String.valueOf(tripsData.get(i).getKey());
                              getSubCategories(Cat_Id);
                              spinner_SubCategories.setVisibility(View.INVISIBLE);
                              SubId=null;
                          }
                      }
                  }else {
                      for (i = 0; i < tripsData.size(); i++) {
                          if (tripsData.get(i).getCat_ar().equals(Category_Name)) {
                              Cat_Id = String.valueOf(tripsData.get(i).getKey());
                              getSubCategories(Cat_Id);
                              spinner_SubCategories.setVisibility(View.INVISIBLE);
                          SubId=null;
                          }
                      }
                  }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    void getSubCategories(final List<SubCategories_Model> tripsData){
        spinner_SubCategories.setVisibility(View.VISIBLE);
        listSubCategories = new ArrayAdapter<SubCategories_Model>(this, R.layout.spinner_item_selected, tripsData) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                return textView;
            }
        };
        listSubCategories.setDropDownViewResource(R.layout.customtextcolor);
        spinner_SubCategories.setAdapter(listSubCategories);
        spinner_SubCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SubCategory_Name = spinner_SubCategories.getSelectedItem().toString();

                if(Language.equals("en")){
                    for (i = 0; i < tripsData.size(); i++) {
                        if (tripsData.get(i).getCat_en().equals(SubCategory_Name)) {
                            SubId = String.valueOf(tripsData.get(i).getSub_key());

                        }
                    }
                }else {
                    for (i = 0; i < tripsData.size(); i++) {
                        if (tripsData.get(i).getCat_ar().equals(SubCategory_Name)) {
                            SubId = String.valueOf(tripsData.get(i).getSub_key());

                        }
                    }
                }

//                data= FirebaseDatabase.getInstance().getReference().child("Products").child(Category_Name);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void getSubCategories(String Sub) {
        listSubcatgory.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Sub_Category");
        Query mqery = databaseReference.orderByChild("key").equalTo(Sub);
        mqery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SubCategories_Model c = dataSnapshot.getValue(SubCategories_Model.class);
                listSubcatgory.add(c);

              getSubCategories(listSubcatgory);
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

        @Override
    protected void onStop() {
        super.onStop();

    }

    private void uploadimage() {

        Name = name.getText().toString().trim();
        Discrption = descrip.getText().toString().trim();
        Phone = phone.getText().toString().trim();
        Price = price.getText().toString().trim();


        if (Category_Name==null || SubId==null|| Name.isEmpty() || Discrption.isEmpty() || Phone.isEmpty() || Price.isEmpty()||token.isEmpty()) {

            Toast.makeText(getBaseContext(), getResources().getString(R.string.validate_data), Toast.LENGTH_LONG).show();

        } else if(Country_Id==null )
        {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.validate_city), Toast.LENGTH_LONG).show();

        }
        else if(dataT==null){
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            String tokenadmin=SharedPrefManager.getInstance(getBaseContext()).getTokenAdmin();

            SendMessage(tokenadmin,child);

            SavedSahredPrefrenceSwitch(Name, Discrption, Phone, Price,Country);

        }
        else {
            if (dataT != null) {
                if (dataT != null) {

                    for (int i = 0; i < dataT.size(); i++) {
                        final SpotsDialog waitingdialog = new SpotsDialog(this);
                        waitingdialog.setTitle(getResources().getString(R.string.loading));
                        waitingdialog.show();
                     //   Uri fi = Uri.parse(dataT.get(i).sdcardPath);
                        File imageFile = new File(dataT.get(i).sdcardPath);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),options);
                            //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fi);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                          byte[] dat = bytes.toByteArray();

                        storageRef = storage.getReferenceFromUrl("gs://temp-95e43.appspot.com");
                        Uri file = Uri.fromFile(new File(dataT.get(i).sdcardPath));
                        final StorageReference imageRef = storageRef.child("images" + "/" + file + ".jpg");
                        UploadTask uploadTask = imageRef.putBytes(dat);

                        uploadTask = imageRef.putFile(file);

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();


                                }
                                // Continue with the task to get the download URL
                                return imageRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String downloadURL = downloadUri.toString();
                                    waitingdialog.dismiss();
                                    Uri u = Uri.parse(downloadURL);
                                    Gson i = new Gson();
                                    listimages.add(u.toString());
                                    String jsonFavorites = i.toJson(listimages);
                                    editor.putString("img", jsonFavorites);
                                    editor.commit();
                                    final int pos = dataT.size();
                                    int y = listimages.size();

                                    if (pos == y) {
                                        String tokenadmin = SharedPrefManager.getInstance(getBaseContext()).getTokenAdmin();
                                        SendMessage(tokenadmin, child);
                                        waitingdialog.dismiss();
                                        SavedSahredPrefrenceSwitch(Name, Discrption, Phone, Price, Country);
                                        View view = ProductList.this.getCurrentFocus();
                                        if (view != null) {
                                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                        }
                                        Name=null;
                                        Discrption=null;
                                        Phone=null;
                                        Price=null;
                                        name.setText(null);
                                        descrip.setText(null);
                                        phone.setText(null);
                                        price.setText(null);
                                        Toast.makeText(ProductList.this, getResources().getString(R.string.review_post), Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(ProductList.this,Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                    }



                            } else {
                                    waitingdialog.dismiss();
                                }
                            }
                        });

                    }}
            }
        }
    }
    public void SendMessage(final String token, final String Msg){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://wasalniegy.com/pushem.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Toast.makeText(ProductList.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("title", getResources().getString(R.string.received_newpost));


                params.put("message",Category_Name +" , "+ SubCategory_Name);
                params.put("email", token);
                return params;
            }
        };

        MyVolley.getInstance(getBaseContext()).addToRequestQueue(stringRequest);

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
    data= FirebaseDatabase.getInstance().getReference().child("NewProducts").push();
     String UserName=SharedPrefManager.getInstance(getBaseContext()).getMyName();
    SharedPreferences sharedPref =getSharedPreferences("Photo", MODE_PRIVATE);
    String jsonFavorit = sharedPref.getString("img", null);
    Gson gson3 = new Gson();
    String[] favoriteIte = gson3.fromJson(jsonFavorit,String[].class);
    Retrivedata r=new Retrivedata();
    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
    if(jsonFavorit!=null){
    int postion = favoriteIte.length;
    if(postion==4) {
        r.setStatues(false);
        r.setImg1(favoriteIte[0]);
        r.setImg2(favoriteIte[1]);
        r.setImg3(favoriteIte[2]);
        r.setImg4(favoriteIte[3]);
        r.setType_service(Type_id);
        r.setCit_id(Long.parseLong(Country_Id));
        r.setSub_id(SubId);
        r.setSub_name(SubCategory_Name);
        r.setCat_name(Category_Name);
        r.setUser_name(UserName);
        r.setSocial_id(mAuth.getUid());
        r.setName(name);
        r.setGovern(govern);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setCurrency(Currency.getText().toString());
        r.setKey(data.getKey());
        r.setPhone(phone);
        r.setDate(date);
        r.setToken(token);
        r.setAdmin(false);
        data.setValue(r);
        Toast.makeText(ProductList.this, getResources().getString(R.string.review_post), Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(ProductList.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
    if (postion == 3) {
        r.setStatues(false);
        r.setImg1(favoriteIte[0]);
        r.setImg2(favoriteIte[1]);
        r.setImg3(favoriteIte[2]);
        r.setCit_id(Long.parseLong(Country_Id));
        r.setName(name);
        r.setType_service(Type_id);
        r.setUser_name(UserName);
        r.setSub_id(SubId);
        r.setSub_name(SubCategory_Name);
        r.setCat_name(Category_Name);
        r.setSocial_id(mAuth.getUid());
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setCurrency(Currency.getText().toString());
        r.setKey(data.getKey());
        r.setPhone(phone);
        r.setGovern(govern);
        r.setDate(date);
        r.setToken(token);
        r.setAdmin(false);
        data.setValue(r);
        Toast.makeText(ProductList.this, getResources().getString(R.string.review_post), Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(ProductList.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
    if (postion == 2){
        r.setStatues(false);
        r.setImg1(favoriteIte[0]);
        r.setImg2(favoriteIte[1]);
        r.setCit_id(Long.parseLong(Country_Id));
        r.setUser_name(UserName);
        r.setName(name);
        r.setType_service(Type_id);
        r.setSocial_id(mAuth.getUid());
        r.setSub_id(SubId);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setSub_name(SubCategory_Name);
        r.setCat_name(Category_Name);
        r.setCurrency(Currency.getText().toString());
        r.setKey(data.getKey());
        r.setGovern(govern);
        r.setPhone(phone);
        r.setDate(date);
        r.setToken(token);
        r.setAdmin(false);
        data.setValue(r);
        Toast.makeText(ProductList.this, getResources().getString(R.string.review_post), Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(ProductList.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
    if(postion==1) {
        r.setStatues(false);
        r.setImg1(favoriteIte[0]);
        r.setName(name);
        r.setDiscrption(discroption);
        r.setCit_id(Long.parseLong(Country_Id));
        r.setDiscount(discount);
        r.setCurrency(Currency.getText().toString());
        r.setKey(data.getKey());
        r.setUser_name(UserName);
        r.setPhone(phone);
        r.setType_service(Type_id);
        r.setSub_name(SubCategory_Name);
        r.setCat_name(Category_Name);
        r.setSub_id(SubId);
        r.setSocial_id(mAuth.getUid());
        r.setGovern(govern);
        r.setDate(date);
        r.setToken(token);
        r.setAdmin(false);
        data.setValue(r);
        Toast.makeText(ProductList.this, getResources().getString(R.string.review_post), Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(ProductList.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }}else {
        r.setStatues(false);
        r.setName(name);
        r.setCit_id(Long.parseLong(Country_Id));
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setType_service(Type_id);
        r.setPhone(phone);
        r.setUser_name(UserName);
        r.setSub_name(SubCategory_Name);
        r.setCat_name(Category_Name);
        r.setDate(date);
        r.setSocial_id(mAuth.getUid());
        r.setCurrency(Currency.getText().toString());
        r.setKey(data.getKey());
        r.setSub_id(SubId);
        r.setGovern(govern);
        r.setToken(token);
        r.setAdmin(false);
        data.setValue(r);
        Toast.makeText(ProductList.this, getResources().getString(R.string.review_post), Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(ProductList.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}

    @Override
    public void Callback(View v, int poistion) {

      if(!Adapteritems.filteredList.isEmpty()){
          Intent inty=new Intent(ProductList.this, ActivityOneItem.class);
          inty.putExtra("child",child);
          inty.putExtra("tokenuser", Adapteritems.filteredList.get(poistion).getToken());
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
          inty.putExtra("tokenuser", arrayadmin.get(poistion).getToken());
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
//        Retrivedatauser();
//        Retrivedataadmin();

    }











    public void imgfileone(byte [] a,final String childimage, final ImageView image, final String IMAGE) {

        final SpotsDialog waitingdialog = new SpotsDialog(ProductList.this);
        waitingdialog.setTitle("يتم التحميل ..");
        waitingdialog.show();

        final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putBytes(a);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();


                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()) {
                    final Uri u = task.getResult();
//                Picasso.with(getApplicationContext())
//                        .load(u)
//                        .fit()
//                        .placeholder(R.drawable.no_media)
//                        .into(image);
                    Glide.with(getBaseContext())
                            .load(u)
                            .apply(new RequestOptions().override(500, 500).placeholderOf(R.drawable.no_media))
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

                } else {
                    waitingdialog.dismiss();
                }
            }
        });






    }




    @Override
    public void cat(String name) {


    }
}
