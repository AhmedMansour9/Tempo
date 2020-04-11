package bekya.bekyaa.Activites;

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

import bekya.bekyaa.Interface.CityId_View;
import bekya.bekyaa.Model.Cities_Response;
import bekya.bekyaa.R;
import bekya.bekyaa.adapter.Cities_Adapter;
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
    public void Id(String id,String Name) {
        Intent intent=new Intent(this,Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",id);
        intent.putExtra("name",Name);
        startActivity(intent);
        finish();
    }
}






















//    DatabaseReference da5=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        da5.child("id").setValue("100");
//                da5.child("name").setValue("الكل");
//                DatabaseReference dat=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat.child("id").setValue("1");
//                dat.child("name").setValue("القاهرة");
//                DatabaseReference datsss=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                datsss.child("id").setValue("2");
//                datsss.child("name").setValue("الجيزة");
//                DatabaseReference dat3=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat3.child("id").setValue("3");
//                dat3.child("name").setValue("المنيا");
//
//                DatabaseReference dat4=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat4.child("id").setValue("4");
//                dat4.child("name").setValue("السويس");
//
//                DatabaseReference dat5=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat5.child("id").setValue("5");
//                dat5.child("name").setValue("الاقصر");
//
//                DatabaseReference dat6=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat6.child("id").setValue("6");
//                dat6.child("name").setValue("الاسكندرية");
//                DatabaseReference da7=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                da7.child("id").setValue("7");
//                da7.child("name").setValue("بورسعيد");
//                DatabaseReference dat8=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat8.child("id").setValue("8");
//                dat8.child("name").setValue("دمياط");
//                DatabaseReference dat9=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat9.child("id").setValue("9");
//                dat9.child("name").setValue("اسوان");
//                DatabaseReference dat10=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat10.child("id").setValue("10");
//                dat10.child("name").setValue("القليوبية");
//                DatabaseReference dat11=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat11.child("id").setValue("11");
//                dat11.child("name").setValue("بني سويف");
//                DatabaseReference dat12=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat12.child("id").setValue("12");
//                dat12.child("name").setValue("الاسماعيلية");
//                DatabaseReference dat13=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat13.child("id").setValue("13");
//                dat13.child("name").setValue("سوهاج");
//                DatabaseReference dat14=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat14.child("id").setValue("14");
//                dat14.child("name").setValue("اسيوط");
//                DatabaseReference dat15=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat15.child("id").setValue("15");
//                dat15.child("name").setValue("البحر الاحمر");
//                DatabaseReference dat16=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat16.child("id").setValue("16");
//                dat16.child("name").setValue("البحيرة");
//                DatabaseReference dat17=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat17.child("id").setValue("17");
//                dat17.child("name").setValue("الدقهلية");
//                DatabaseReference dat18=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat18.child("id").setValue("18");
//                dat18.child("name").setValue("الغربية");
//                DatabaseReference dat19=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat19.child("id").setValue("19");
//                dat19.child("name").setValue("القيوم");
//                DatabaseReference dat20=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat20.child("id").setValue("20");
//                dat20.child("name").setValue("قنا");
//                DatabaseReference dat21=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat21.child("id").setValue("21");
//                dat21.child("name").setValue("كفر الشيخ");
//                DatabaseReference dat22=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat22.child("id").setValue("22");
//                dat22.child("name").setValue("مطروح");
//                DatabaseReference dat23=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat23.child("id").setValue("23");
//                dat23.child("name").setValue("المنوفية");
//                DatabaseReference dat24=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat24.child("id").setValue("24");
//                dat24.child("name").setValue("الوادي الجديد");
//                DatabaseReference dat25=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat25.child("id").setValue("25");
//                dat25.child("name").setValue("السادس من أكتوبر");
//                DatabaseReference dat26=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat26.child("id").setValue("26");
//                dat26.child("name").setValue("شمال سيناء");
//                DatabaseReference dat27=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat27.child("id").setValue("27");
//                dat27.child("name").setValue("جنوب سيناء");
//                DatabaseReference dat28=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat28.child("id").setValue("28");
//                dat28.child("name").setValue("حلوان");
//                DatabaseReference dat29=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//                dat29.child("id").setValue("29");
//                dat29.child("name").setValue("الشارقية");
