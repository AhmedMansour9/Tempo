package com.tempomena.Activites;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tempomena.Interface.itemViewinterface;
import com.tempomena.Model.Cities_Response;
import com.tempomena.Model.Retrivedata;
import com.tempomena.R;
import com.tempomena.adapter.Adapteritems;
import com.tempomena.adapter.imgclick;
import com.tempomena.tokenid.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class All_Product extends AppCompatActivity implements imgclick,itemViewinterface,SwipeRefreshLayout.OnRefreshListener{
    ImageView imgone,imgtwo,imgthree,imgfour;
    ImageView deltone,deletetwo,deletethree,deletefour;
    EditText editname,editdiscrp,editdiscount,editphone,editgovern;
    Dialog update_items_layout;
    String Names,Country_Id;
    Button finish;
    ImageView cameraone,cameratwo,camerathree,camerafour;
    String imgOne,imgTwo,imgThree,imgFour;
    String Nameone;
    DatabaseReference data;

    public static String date2;
    ArrayList<Retrivedata> arrayadmin;
    ArrayList<Retrivedata> testArray,EmptyArray;
    RecyclerView recyclerView;
    private Adapteritems mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    DatabaseReference category,dataproduct,dataadmin;
    String Sub_Id,Name;
    ArrayList<Cities_Response> arrayacities;
    ArrayAdapter<String> listTypes;
    TextView Title;
    Spinner spinner_country,spinner_Types;
    private String Type_id;
    ArrayAdapter<Cities_Response> listCountries;
    Button p_service,R_service;
    int count,CountAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__product);
        data= FirebaseDatabase.getInstance().getReference().child("NewProducts");

        init();
        Recyclview();
        SwipRefresh();
        SelectedTabs();
    }

    private void SelectedTabs() {
      p_service.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Type_id="1";
              p_service.setTextColor(Color.parseColor("#ffffff"));
              p_service.setBackgroundResource(R.drawable.bc_selected);
              R_service.setTextColor(Color.parseColor("#003152"));
              R_service.setBackgroundResource(R.drawable.bc_type);
              arrayadmin.clear();
              mAdapter.notifyDataSetChanged();
              Retrivedatauser(Sub_Id);
          }
      });

        R_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type_id="2";
                R_service.setTextColor(Color.parseColor("#ffffff"));
                R_service.setBackgroundResource(R.drawable.bc_selected);
                p_service.setTextColor(Color.parseColor("#003152"));
                p_service.setBackgroundResource(R.drawable.bc_type);
                arrayadmin.clear();
                mAdapter.notifyDataSetChanged();
                Retrivedatauser(Sub_Id);
            }
        });
    }






    private void init(){
        R_service=findViewById(R.id.R_service);
        p_service=findViewById(R.id.P_service);
        spinner_country=findViewById(R.id.spinner_country);
        spinner_Types=findViewById(R.id.spinner_Types);
        Type_id="1";
        Title=findViewById(R.id.Title);
        Sub_Id=getIntent().getStringExtra("id");
        Name=getIntent().getStringExtra("name");
        Title.setText(Name);
        date2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
        arrayadmin=new ArrayList<>();
        testArray=new ArrayList<>();

    }


    public void Recyclview(){
        recyclerView =findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        mAdapter = new Adapteritems(arrayadmin,getBaseContext());
        mAdapter.setClickListener(this);
        mAdapter.setClickButton(this);
//        recyclerView.setNestedScrollingEnabled(false);
//        mAdapter.setClickListener(this);
//        mAdapter.setClickButton(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    public void SwipRefresh(){
        mSwipeRefreshLayout =findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

           Retrivedatauser(Sub_Id);

            }
        });
    }

    public void Retrivedatauser(String Cat_Name) {
         count = 0;
        CountAdmin=0;
        mSwipeRefreshLayout.setRefreshing(false);
        dataproduct= FirebaseDatabase.getInstance().getReference().child("NewProducts");
        Query mqery=dataproduct.orderByChild("sub_id").equalTo(Cat_Name);
        mqery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                 if (dataSnapshot1.exists()) {
                     Retrivedata r = dataSnapshot1.getValue(Retrivedata.class);
                     testArray.add(r);
                     count++;
                     if (count == dataSnapshot.getChildrenCount()) {
                         Collections.reverse(testArray);

                         mSwipeRefreshLayout.setRefreshing(false);
                         String Country= SharedPrefManager.getInstance(getBaseContext()).getCountryId();
                         for (int i = 0; i < testArray.size(); i++) {
                             if (testArray.get(i).getStatues() && testArray.get(i).getCit_id() == Integer.parseInt(Country) && testArray.get(i).getType_service().equals(Type_id)) {
                                 if (!hasId(testArray.get(i).getKey())) {
                                     Retrivedata retrivedata = new Retrivedata();
                                     retrivedata.setStatues(testArray.get(i).getStatues());
                                     retrivedata.setName(testArray.get(i).getName());
                                     retrivedata.setKey(testArray.get(i).getKey());
                                     retrivedata.setCurrency(testArray.get(i).getCurrency());
                                     retrivedata.setAdmin(testArray.get(i).getAdmin());
                                     retrivedata.setDate(testArray.get(i).getDate());
                                     retrivedata.setDiscount(testArray.get(i).getDiscount());
                                     retrivedata.setSocial_id(testArray.get(i).getSocial_id());
                                     retrivedata.setDiscrption(testArray.get(i).getDiscrption());
                                     retrivedata.setGovern(testArray.get(i).getGovern());
                                     retrivedata.setImg1(testArray.get(i).getImg1());
                                     retrivedata.setImg2(testArray.get(i).getImg2());
                                     retrivedata.setImg3(testArray.get(i).getImg3());
                                     retrivedata.setImg4(testArray.get(i).getImg4());
                                     retrivedata.setKey(testArray.get(i).getKey());
                                     retrivedata.setToken(testArray.get(i).getToken());
                                     retrivedata.setPhone(testArray.get(i).getPhone());
                                     if(testArray.get(i).getAdmin()){
                                         arrayadmin.add(CountAdmin,retrivedata);
                                         CountAdmin++;
                                     }else {
                                         arrayadmin.add(retrivedata);
                                     }
                                 }
                             }
                         }

                         mSwipeRefreshLayout.setRefreshing(false);

                         mAdapter.notifyDataSetChanged();

                     }

                 }else {
                     mSwipeRefreshLayout.setRefreshing(false);

                 }

             }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean hasId(String idc){
        if(!TextUtils.isEmpty(idc)) {
            for (Retrivedata fr : arrayadmin) {
                if (fr.getKey().equals(idc)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onRefresh() {
        arrayadmin.clear();
        mAdapter.notifyDataSetChanged();

        Retrivedatauser(Sub_Id);

    }

    @Override
    public void Callback(View v, int poistion) {
        if(!Adapteritems.filteredList.isEmpty()){
            Intent inty=new Intent(this, ActivityOneItem.class);
            inty.putExtra("child",Sub_Id);
            inty.putExtra("tokenuser", Adapteritems.array.get(poistion).getToken());
            inty.putExtra("key", Adapteritems.array.get(poistion).getImg1());
            inty.putExtra("name", Adapteritems.array.get(poistion).getName());
            inty.putExtra("discrp", Adapteritems.array.get(poistion).getDiscrption());
            inty.putExtra("discount", Adapteritems.array.get(poistion).getDiscount());
            inty.putExtra("phone", Adapteritems.array.get(poistion).getPhone());
            inty.putExtra("date", Adapteritems.array.get(poistion).getDate());
            inty.putExtra("govern", Adapteritems.array.get(poistion).getGovern());

            startActivity(inty);

        }else {
            Intent inty=new Intent(this,ActivityOneItem.class);
            inty.putExtra("child",Sub_Id);
            inty.putExtra("tokenuser", arrayadmin.get(poistion).getToken());
            inty.putExtra("key", arrayadmin.get(poistion).getImg1());
            inty.putExtra("name", arrayadmin.get(poistion).getName());
            inty.putExtra("discrp", arrayadmin.get(poistion).getDiscrption());
            inty.putExtra("discount", arrayadmin.get(poistion).getDiscount());
            inty.putExtra("phone", arrayadmin.get(poistion).getPhone());
            inty.putExtra("date", arrayadmin.get(poistion).getDate());
            inty.putExtra("govern", arrayadmin.get(poistion).getGovern());
            inty.putExtra("cu", arrayadmin.get(poistion).getCurrency());
            inty.putExtra("social", arrayadmin.get(poistion).getSocial_id());

            startActivity(inty);

        }
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

    @Override
    public void onClickCallback(View view, int adapterPosition) {
        update_items_layout = new Dialog(this);
        update_items_layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        update_items_layout.setContentView(R.layout.edititems);
        init2();
        Nameone=arrayadmin.get(adapterPosition).getName();
        imgOne=arrayadmin.get(adapterPosition).getImg1();
        if(imgOne!=null){
            Uri u = Uri.parse(imgOne);

            Glide.with(this)
                    .load( u)
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
                    .into(imgone);

        }else {
            deltone.setVisibility(View.INVISIBLE);
        }
        imgTwo=arrayadmin.get(adapterPosition).getImg2();
        if(imgTwo!=null){
            Uri u = Uri.parse(imgTwo);

            Glide.with(this)
                    .load( u)
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
                    .into(imgtwo);

        }else {
            deletetwo.setVisibility(View.INVISIBLE);
        }
        imgThree=arrayadmin.get(adapterPosition).getImg3();
        if(imgThree!=null){
            Uri u = Uri.parse(imgThree);

            Glide.with(this)
                    .load( u)
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
                    .into(imgthree);
        }else {
            deletethree.setVisibility(View.INVISIBLE);
        }

        imgFour=arrayadmin.get(adapterPosition).getImg4();
        if(imgFour!=null){
            Uri u = Uri.parse(imgFour);
//            Picasso.with(getContext())
//                    .load(u)
//                    .fit()
//                    .placeholder(R.drawable.no_media)
//                    .into(imgfour);

            Glide.with(this)
                    .load( u)
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

    public void btnfinish(){
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editname.getText().toString().isEmpty()||editdiscount.getText().toString().isEmpty()||
                        editdiscrp.getText().toString().isEmpty()||editphone.getText().toString().isEmpty() ){
                    Toast.makeText(All_Product.this,getResources().getString(R.string.validate_data), Toast.LENGTH_LONG).show();

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
                            arrayadmin.clear();
                            mAdapter.notifyDataSetChanged();
                            Retrivedatauser(Sub_Id);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    update_items_layout.dismiss();
                    Toast.makeText(All_Product.this,getResources().getString(R.string.savedsucces),Toast.LENGTH_LONG).show();
                }

            }
        });
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

    @Override
    public void onClickdelete(View view, final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.delete_post));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!Adapteritems.filteredList.isEmpty()){
                    Names =Adapteritems.filteredList.get(adapterPosition).getName();

                }else if(Adapteritems.filteredList.isEmpty()){
                    Names =arrayadmin.get(adapterPosition).getName();

                }
//                data.removeEventListener(mListener);
                String Name =arrayadmin.get(adapterPosition).getName();
                if(arrayadmin.get(adapterPosition).getAdmin()){
                    DeletetePost(Name);
                }else {
                    DeletetePost(Name);
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
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    public void DeletetePost(String Name){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NewProducts");
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
}
