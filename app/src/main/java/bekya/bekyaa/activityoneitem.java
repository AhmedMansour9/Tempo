package bekya.bekyaa;

import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.List;

import bekya.bekyaa.Interface.btnclicks;
import bekya.bekyaa.Interface.imageclick;
import bekya.bekyaa.adapter.AdapterOneitem;

public class activityoneitem extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener ,imageclick,btnclicks {
    List<Retrivedata> array;
    public RecyclerView recyclerView;
    private AdapterOneitem mAdapter;
    TextView textprice;
    TextView textname,textdiscrp,textdiscount,textphone,textdate;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Retrivedata set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityitems);
        array=new ArrayList<>();
        textname= findViewById(R.id.textname);
        textdiscrp= findViewById(R.id.textdiscrp);
        textdiscount=findViewById(R.id.textdiscount);
        textphone= findViewById(R.id.textphone);
        textdate=findViewById(R.id.textdate);

        String name=getIntent().getStringExtra("name");
        String discrption=getIntent().getStringExtra("discrp");
        String discount=getIntent().getStringExtra("discount");
        String phone=getIntent().getStringExtra("phone");
        String date=getIntent().getStringExtra("date");
        textname.setText(name);
        textdiscrp.setText(discrption);
        textdiscount.setText(discount);
        textphone.setText(phone);
        textdate.setText(date);
        Recyclview();
        SwipRefresh();
    }

    public void getdata(){
        String key=getIntent().getStringExtra("key");
        array.clear();
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(true);

        DatabaseReference data= FirebaseDatabase.getInstance().getReference().child("Products");
        data.orderByChild("img1").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if(child.hasChild("img1")) {
                        set=new Retrivedata();
                        set.setImg1(child.child("img1").getValue().toString());
                        array.add(set);
                        mAdapter.notifyDataSetChanged();
                    }
                    if(child.hasChild("img2")) {
                        set=new Retrivedata();
                        set.setImg1(child.child("img2").getValue().toString());
                        array.add( set);
                        mAdapter.notifyDataSetChanged();
                    }
                    if(child.hasChild("img3")) {
                        set=new Retrivedata();
                        set.setImg1(child.child("img3").getValue().toString());
                        array.add( set);
                        mAdapter.notifyDataSetChanged();
                    }
                    if(child.hasChild("img4")) {
                        set=new Retrivedata();
                        set.setImg1(child.child("img4").getValue().toString());
                        array.add( set);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void Recyclview(){
        recyclerView =findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        mAdapter = new AdapterOneitem(array,activityoneitem.this);
         mAdapter.setClickListener(this);
         mAdapter.setClickList(this);
         linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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
                getdata();
            }
        });
    }

    @Override
    public void onRefresh() {
        getdata();
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

        Picasso.with(this)
                .load(id)
                .fit()
                .placeholder(R.drawable.no_media)
                .into(img);

          dialog.show();

    }
}
