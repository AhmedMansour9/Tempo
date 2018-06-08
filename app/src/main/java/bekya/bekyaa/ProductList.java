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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import bekya.bekyaa.Interface.itemViewinterface;
import bekya.bekyaa.Model.Retrivedata;
import bekya.bekyaa.adapter.Adapteritems;
import bekya.bekyaa.adapter.GalleryAdapter;
import bekya.bekyaa.adapter.imgclick;
import bekya.bekyaa.tokenid.SharedPrefManager;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,itemViewinterface,imgclick {

    RecyclerView recyclerView;
    RelativeLayout rootlayout;
    String Nameone;
    Button btnUpload;
    Button btnselect;
    SwipeRefreshLayout mSwipeRefreshLayout;
EditText name,descrip , discount, price;
    GridView gridGallery;
    Handler handler;
    ImageView imgone,imgtwo,imgthree,imgfour;
    ImageView deltone,deletetwo,deletethree,deletefour;
    ImageView cameraone,cameratwo,camerathree,camerafour;
    EditText editname,editdiscrp,editdiscount,editphone;
    Button finish;
    GalleryAdapter adapter;
    List<Retrivedata> arrayadmin;
    ArrayList<String> listimages=new ArrayList<>();
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;
    DatabaseReference data;
    private Adapteritems mAdapter;
    StorageReference storageRef;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editt;
    String imgOne,imgTwo,imgThree,imgFour;
    private Uri filePathone,filePathtwo,filePaththree,filePathfour;
    FirebaseStorage storage;
    StorageReference storageReference;
    public static String token;
    ArrayList<CustomGallery> dataT;
    String Name,Discrption,Discount,Price;
    String child;
    Dialog update_items_layout;
    Dialog update_info_layout;
    public ChildEventListener mListener;
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
          token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();

        handler = new Handler();
        arrayadmin=new ArrayList<>();
        SharedPreferences shared=getSharedPreferences("cat",MODE_PRIVATE);
         child=shared.getString("Category",null);
        data= FirebaseDatabase.getInstance().getReference().child("Products").child(child);

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
        mAdapter = new Adapteritems(arrayadmin,ProductList.this);
        mAdapter.setClickListener(this);
        mAdapter.setClickButton(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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

    public void Retrivedata(){
        arrayadmin.clear();
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(true);
        mListener= data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    Retrivedata r = dataSnapshot.getValue(Retrivedata.class);


                    if(r!=null &&!hasId(r.getName())) {
                        if(r.getAdmin()){
                            arrayadmin.add(r);
                        }

                        if(r.getAdmin()==false){
                            arrayadmin.add(r);
                        }

                        mAdapter.notifyDataSetChanged();
                    }





                    mSwipeRefreshLayout.setRefreshing(false);
                }else {
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
         update_info_layout = new Dialog(ProductList.this);
        update_info_layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        initImageLoader();
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
                                update_info_layout.dismiss();
                                Snackbar.make(rootlayout, "تم إضافة منجك بنجاح", Snackbar.LENGTH_SHORT)
                                        .show();
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

        if(requestCode == 50 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePathone=null;
            filePathone = data.getData();
            if(filePathone != null)
            {
                String imagechild="img1";
             imgfileone(filePathone,imagechild,imgone,Nameone);
            }
        }else   if(requestCode == 100 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePathtwo=null;
            filePathtwo = data.getData();
            if(filePathtwo != null)
            {
                String imagechild="img2";
                imgfileone(filePathtwo,imagechild,imgtwo,Nameone);
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
                imgfileone(filePaththree,imagechild,imgthree,Nameone);

            }
        }  else   if(requestCode == 250 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePathfour=null;
            filePathfour = data.getData();
            if(filePathfour != null)
            {
                String imagechild="img4";
                imgfileone(filePathfour,imagechild,imgfour,Nameone);

            }
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
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
        r.setToken(token);
        r.setAdmin(false);
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
        r.setToken(token);
        r.setAdmin(false);
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
        r.setToken(token);
        r.setAdmin(false);
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
        r.setToken(token);
        r.setAdmin(false);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منجك بنجاح", Snackbar.LENGTH_SHORT)
                .show();

    }}else {
        r.setName(name);
        r.setDiscrption(discroption);
        r.setDiscount(discount);
        r.setPhone(phone);
        r.setDate(date);
        r.setToken(token);
        r.setAdmin(false);
        data.push().setValue(r);
        Snackbar.make(rootlayout, "تم إضافة منجك بنجاح", Snackbar.LENGTH_SHORT)
                .show();
    }

}

    @Override
    public void Callback(View v, int poistion) {
        Intent inty=new Intent(ProductList.this,ActivityOneItem.class);
            inty.putExtra("child",child);
            inty.putExtra("key", arrayadmin.get(poistion).getImg1());
            inty.putExtra("name", arrayadmin.get(poistion).getName());
            inty.putExtra("discrp", arrayadmin.get(poistion).getDiscrption());
            inty.putExtra("discount", arrayadmin.get(poistion).getDiscount());
            inty.putExtra("phone", arrayadmin.get(poistion).getPhone());
            inty.putExtra("date", arrayadmin.get(poistion).getDate());
            startActivity(inty);

    }

    @Override
    public void onRefresh() {
        Retrivedata();
    }


    @Override
    public void onClickCallback(View view, int adapterPosition) {
         update_items_layout = new Dialog(ProductList.this);
        update_items_layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        update_items_layout.setContentView(R.layout.edititems);
       init2();
       Nameone=arrayadmin.get(adapterPosition).getName();

        imgOne=arrayadmin.get(adapterPosition).getImg1();
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
         imgTwo=arrayadmin.get(adapterPosition).getImg2();
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
         imgThree=arrayadmin.get(adapterPosition).getImg3();
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

         imgFour=arrayadmin.get(adapterPosition).getImg4();
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
        editname.setText(arrayadmin.get(adapterPosition).getName());
        editdiscrp.setText(arrayadmin.get(adapterPosition).getDiscrption());
        editdiscount.setText(arrayadmin.get(adapterPosition).getDiscount());
       editphone.setText(arrayadmin.get(adapterPosition).getPhone());
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
        builder.setMessage("Do You Want to Delete This Post ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                String Name =array.get(adapterPosition).getName();

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
                arrayadmin.remove(adapterPosition);
                mAdapter.notifyDataSetChanged();

                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();



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

    public void imgfileone(Uri filePath,final String childimage, final ImageView image, final String IMAGE) {

        final ProgressDialog progressDialog = new ProgressDialog(ProductList.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        final Uri u = taskSnapshot.getDownloadUrl();
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
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
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
                    Toast.makeText(getApplicationContext(), "من فضلك املئ البيانات ", Toast.LENGTH_SHORT).show();

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
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }
        });
    }
}
