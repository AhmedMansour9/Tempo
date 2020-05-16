package com.tempo.Activites;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import com.tempo.Interface.CityId_View;
import com.tempo.Model.Cities_Response;
import com.tempo.R;
import com.tempo.adapter.Cities_Adapter;
import com.tempo.tokenid.SharedPrefManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Cities extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CityId_View {
    ArrayList<Cities_Response> arrayacities;
    private Cities_Adapter cities_adapter;
    private RecyclerView recycler_Cities;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/whatsappbold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_cities);
        init();
        SwipRefresh();

    }
    private void init(){
        recycler_Cities=findViewById(R.id.recycler_Cities);
    }

    public void RetriveCities() {
        arrayacities=new ArrayList<>();
        DatabaseReference data= FirebaseDatabase.getInstance().getReference().child("Cities");
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mSwipeRefreshLayout.setRefreshing(false);

                if (dataSnapshot.exists()) {
                    Cities_Response r = dataSnapshot.getValue(Cities_Response.class);
                    arrayacities.add(r);
                    cities_adapter=new Cities_Adapter(arrayacities,Cities.this);
                    cities_adapter.setClickListener(Cities.this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Cities.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recycler_Cities.setLayoutManager(linearLayoutManager);
                    recycler_Cities.setItemAnimator(new DefaultItemAnimator());
                    recycler_Cities.setAdapter(cities_adapter);

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

    public void SwipRefresh(){
        mSwipeRefreshLayout =  findViewById(R.id.swipe_Cities);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                RetriveCities();

            }
        });
    }

    @Override
    public void onRefresh() {
        RetriveCities();
    }

    @Override
    public void Id(String Name,String aa,String arabic) {
        SharedPrefManager.getInstance(getBaseContext()).saveCountry(Name);
        SharedPrefManager.getInstance(getBaseContext()).saveCountryId(aa);
        SharedPrefManager.getInstance(getBaseContext()).saveCountryArabic(arabic);

        Intent intent=new Intent(this,Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("id",id);
        intent.putExtra("name",Name);
        startActivity(intent);
        finish();
    }
}






















