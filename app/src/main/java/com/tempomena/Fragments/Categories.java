package com.tempomena.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.tempomena.Activites.All_Product;
import com.tempomena.Activites.Cities;
import com.tempomena.Activites.Home;
import com.tempomena.Activites.ProductList;
import com.tempomena.ChangeLanguage;
import com.tempomena.Interface.Categories_View;
import com.tempomena.Interface.CityId_View;
import com.tempomena.Interface.itemViewinterface;
import com.tempomena.Model.Cities_Response;
import com.tempomena.Model.Gallery;
import com.tempomena.Model.Category;
import com.tempomena.Model.Companymodel;
import com.tempomena.Interface.Open_Galler_View;
import com.tempomena.Model.Retrivedata;
import com.tempomena.Model.SubCategories_Model;
import com.tempomena.R;
import com.tempomena.adapter.Adapteritems;
import com.tempomena.adapter.Slider_Adapter;
import com.tempomena.adapter.Categories_Adapter;
import com.tempomena.adapter.SubCategories_Adapter;
import com.tempomena.adapter.imgclick;
import com.tempomena.tokenid.SharedPrefManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class Categories extends Fragment implements CityId_View,imgclick,itemViewinterface
        ,Categories_View,Open_Galler_View,SwipeRefreshLayout.OnRefreshListener {


    public Categories() {
        // Required empty public constructor
    }
    ImageView imgone,imgtwo,imgthree,imgfour;
    ImageView deltone,deletetwo,deletethree,deletefour;
    EditText editname,editdiscrp,editdiscount,editphone,editgovern;
    Dialog update_items_layout;
    String Nameone;
    ImageView cameraone,cameratwo,camerathree,camerafour;
    Button finish;
    String Names,Country_Id;
    LayoutInflater li;
    WindowManager wm;
    String child;
    View v;
    EditText product;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FirebaseDatabase database;
    DatabaseReference category,dataproduct,dataadmin;
    List<Category> listcatgory;
    List<SubCategories_Model> listSubcatgory;
    ArrayAdapter<Cities_Response> listCountries;

    ArrayList<Retrivedata> testArray;
    public ChildEventListener mListener;
    Companymodel companymodel;
    RecyclerView recyclerView;
    DatabaseReference data;
    WindowManager.LayoutParams params;
    Categories_Adapter categories_adapter;
    CardView card;
    String imgOne,imgTwo,imgThree,imgFour,name;
    String childadmin;
    Context con;
    Gallery gallery;
    private RecyclerView rv_autoScroll;
    LinearLayoutManager linearLayoutManager;
    Boolean end;
    RecyclerView recycler_Categories;
    int position = 0;
    public static String date2;
    Context context;
    Slider_Adapter banerAdapter;
    List<Gallery> list = new ArrayList<>();
    List<Gallery> banne = new ArrayList<>();
    ArrayList<Retrivedata> arrayadmin;
    final Handler handler = new Handler();
    private Adapteritems mAdapter;
    FloatingActionButton Float_AddPost;
    private String Language;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceSub;
    SubCategories_Adapter  subcateg_adapter;
        final Runnable update = new Runnable() {
            public void run() {

                if(position == banne.size()-1){
                    end = true;
                }
                else if (position == 0) {
                    end = false;
                }
                if(!end){
                    position++;
                } else {
                    position=0;
                }
//                vp_slider.setCurrentItem(page_position, true);
                rv_autoScroll.smoothScrollToPosition(position);
            }
        };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.catogries, container, false);
         databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReferenceSub= FirebaseDatabase.getInstance().getReference();
        Home.toolbar.setVisibility(View.VISIBLE);
        Home.Rela_Govern.setVisibility(View.VISIBLE);

        init();
         Recyclview();
        SwipRefresh();
        Add_Post();
        Filtertion();
//        RecycleviewSerach();
        return v;
    }


    public void SwipRefresh(){
        mSwipeRefreshLayout =v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                RetriveCategories();

            }
        });
    }
    void Add_Post(){
        Float_AddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProductList.class));

            }
        });
    }
    void Filtertion(){
        Home.T_Government.setVisibility(View.VISIBLE);
        String Country=SharedPrefManager.getInstance(getContext()).getCountry();
        if(Country!=null){
            if(Language.equals("en")) {
                Home.T_Government.setText(Country);
            }else {
                Home.T_Government.setText(SharedPrefManager.getInstance(getContext()).getCountryArabic());

            }
        }else {
            Home.T_Government.setText(getResources().getString(R.string.location));

        }
        Home.T_Government.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Cities.class));
            }
        });

    }



    private void init(){
        Language= ChangeLanguage.getLanguage(getContext());
//        product = v.findViewById(R.id.findyourproduct);
        date2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
        con = this.getActivity();
        arrayadmin=new ArrayList<>();
        testArray=new ArrayList<>();
        listcatgory = new ArrayList<>();
        listSubcatgory=new ArrayList<>();
        context = this.getActivity();
        database = FirebaseDatabase.getInstance();
        category = database.getReference("category");
        card = v.findViewById(R.id.friendCardView2);
        rv_autoScroll = v.findViewById(R.id.recycler_banner2);
        recycler_Categories=v.findViewById(R.id.recycler_Categories);
        GetImages();
        companymodel = new Companymodel();
        data = FirebaseDatabase.getInstance().getReference("Company");
        Float_AddPost=v.findViewById(R.id.Float_AddPost);
    }

    public void Recyclview(){
        recyclerView =v.findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        mAdapter = new Adapteritems(arrayadmin,getContext());
//        recyclerView.setNestedScrollingEnabled(false);

        mAdapter.setClickListener(this);
        mAdapter.setClickButton(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



    }

    @Override
    public void cat(String name) {
        recyclerView.setVisibility(View.GONE);
        listSubcatgory.clear();
        getSubCategories(name);
    }

    @Override
    public void delete(String a) {

        if (a != null) {

                Intent facebookIntent = getOpenFacebookIntent(getContext(),a);
                startActivity(facebookIntent);
        }
    }

    public static Intent getOpenFacebookIntent(Context context,String a) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/"+a)); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(a)); //catches and opens a url to the desired page
        }
    }


    public void GetImages(){
        DatabaseReference data=FirebaseDatabase.getInstance().getReference("Gallery");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    gallery=dataSnapshot1.getValue(Gallery.class);
                    list.add(gallery);

//                    fire.Company(list);
                }
//                card.setVisibility(View.VISIBLE);
                banne = list;
                banerAdapter = new Slider_Adapter(list, getContext());
                banerAdapter.DeleteImage(Categories.this);
                linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv_autoScroll.setLayoutManager(linearLayoutManager);
                rv_autoScroll.setAdapter(banerAdapter);

                if(banne.size()>1) {
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(update);
                        }
                    }, 8000, 8000);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void Callback(View v, int poistion) {


    }

    @Override
    public void Id(String Sub_Id,String Name,String aa) {
        String Country=SharedPrefManager.getInstance(getContext()).getCountryId();
       if(Country!=null) {
           Intent intent = new Intent(getContext(), All_Product.class);
           intent.putExtra("id", Sub_Id);
           intent.putExtra("name", Name);
           startActivity(intent);
       }else {
           Toast.makeText(con, ""+getActivity().getResources().getString(R.string.validate_city), Toast.LENGTH_SHORT).show();
       }
    }

    public interface firebasecallback{
        void Company(List<Gallery> comp);
    }

    private boolean hasCatId(String idc){
        if(!TextUtils.isEmpty(idc)) {
            for (Category fr : listcatgory) {
                if (fr.getKey().equals(idc)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public void RetriveCategories(){
        listcatgory.clear();
        databaseReference.child("Category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mSwipeRefreshLayout.setRefreshing(false);
                Category c=dataSnapshot.getValue(Category.class);
                if (c != null && !hasCatId(c.getKey())) {
                    listcatgory.add(c);
                    categories_adapter =new Categories_Adapter(listcatgory,getContext());
                    categories_adapter.setClickListener(Categories.this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                    recycler_Categories.setLayoutManager(linearLayoutManager);
                    recycler_Categories.setItemAnimator(new DefaultItemAnimator());
                    recycler_Categories.setAdapter(categories_adapter);

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
    private boolean hasSubId(String idc){
        if(!TextUtils.isEmpty(idc)) {
            for (SubCategories_Model fr : listSubcatgory) {
                if (fr.getSub_key().equals(idc)) {
                    return true;
                }
            }
        }
        return false;
    }


    public void getSubCategories(String Sub){
        mSwipeRefreshLayout.setRefreshing(false);
        listSubcatgory.clear();

        Query mqery=databaseReference.child("Sub_Category").orderByChild("key").equalTo(Sub);
        mqery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SubCategories_Model c = dataSnapshot.getValue(SubCategories_Model.class);
                if (c != null && !hasSubId(c.getSub_key())) {

                    listSubcatgory.add(c);
                    recyclerView.setVisibility(View.VISIBLE);
                    subcateg_adapter =new SubCategories_Adapter(listSubcatgory,getContext());
                    subcateg_adapter.setClickListener(Categories.this);
                    LinearLayoutManager linearLayoutManagersub = new LinearLayoutManager(getActivity());
                    linearLayoutManagersub.setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManagersub);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(subcateg_adapter);
//                mSwipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onRefresh() {
//        arrayadmin.clear();
//        mAdapter.notifyDataSetChanged();
//        Retrivedatauser(child);
//        Retrivedataadmin(childadmin);
        RetriveCategories();
    }

    @Override
    public void onPause() {
        super.onPause();
      context=null;
        v=null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        context=null;

        v=null;
        }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            Home.Visablty = true;
        } else {

        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Home.Visablty=true;

    }



//    public void Retrivedataadmin(String childadmin) {
//        mSwipeRefreshLayout.setRefreshing(true);
//        dataadmin= FirebaseDatabase.getInstance().getReference().child("Products").child(childadmin);
//        mListener=dataadmin.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if (dataSnapshot.exists()) {
//                    Retrivedata r = dataSnapshot.getValue(Retrivedata.class);
//
//                    String Date = r.getDate();
//                    int days = GetDays(Date, Categories.date2);
//                    if (days > 350) {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    } else {
//                        if (r != null && !hasId(r.getName())) {
//                            arrayadmin.add(0,r);
//
//                            mAdapter.notifyDataSetChanged();
//
//                        }
//
//                        mSwipeRefreshLayout.setRefreshing(false);
//
//                    }
//                } else {
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

//    public void RecycleviewSerach(){
//        product.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                mAdapter.getFilter().filter(charSequence);
//                mAdapter.notifyDataSetChanged();
//
//
//
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//    }


    public int GetDays(String dateone,String datetwo){
        String date1 = dateone;
        String date2 =datetwo;
        DateTimeFormatter formatter =  DateTimeFormat.forPattern("dd-MM-yyyy").withLocale(Locale.ENGLISH);
        DateTime d1 = formatter.parseDateTime(date1);
        DateTime d2 = formatter.parseDateTime(date2);

        Calendar startDate = Calendar.getInstance();
        startDate.set(d1.getYear(), d1.getMonthOfYear(), d1.getDayOfWeek());
        long startDateMillis = startDate.getTimeInMillis();

        Calendar endDate = Calendar.getInstance();
        endDate.set(d2.getYear(), d2.getMonthOfYear(), d2.getDayOfWeek());
        long endDateMillis = endDate.getTimeInMillis();

        long differenceMillis = endDateMillis - startDateMillis;
        int daysDifference = (int) (differenceMillis / (1000 * 60 * 60 * 24));

        return daysDifference;
    }


    @Override
    public void onClickdelete(View view, final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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



    }

    @Override
    public void onClickCallback(View view, int adapterPosition) {
        update_items_layout = new Dialog(getContext());
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
//               Picasso.with(getApplicationContext())
//                       .load(u)
//                       .fit()
//                       .placeholder(R.drawable.no_media)
//                       .into(imgone);
            Glide.with(getContext())
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

        if(imgTwo!=null){
            Uri u = Uri.parse(imgTwo);

            Glide.with(getContext())
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

        if(imgThree!=null){
            Uri u = Uri.parse(imgThree);
            Glide.with(getContext())
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


        if(imgFour!=null){
            Uri u = Uri.parse(imgFour);

            Glide.with(getContext())
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
                    Toast.makeText(getContext(), "لازم تكتب كل البيانات ", Toast.LENGTH_SHORT).show();

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
    public void DeletetePost(String child,String Name){
        data.removeEventListener(mListener);
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
    public void onStop() {
        super.onStop();
        SharedPrefManager.getInstance(getContext()).savePostion(null);

    }
    @Override
    public void onResume() {
        super.onResume();

    }


}
