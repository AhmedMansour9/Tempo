package bekya.bekyaa;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bekya.bekyaa.Model.Category;
import bekya.bekyaa.Model.Companymodel;
import bekya.bekyaa.adapter.ImageAdapterGride;
import bekya.bekyaa.tokenid.SharedPrefManager;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Categories extends Fragment implements  SwipeRefreshLayout.OnRefreshListener {


    public Categories() {
        // Required empty public constructor
    }
    LayoutInflater li;
    WindowManager wm;
    View myview;
    View v;
    int myLastVisiblePos;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FirebaseDatabase database;
    DatabaseReference category;
    List<Category> listcatgory;
    GridView gridView;
    String img1,img2;
    Companymodel companymodel;
    RelativeLayout relativeLayout;
    RelativeLayout rela;
    DatabaseReference data;
    WindowManager.LayoutParams params;
    ImageView imgone,imgtwo;
    CardView card;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.catogries, container, false);
        listcatgory = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        category = database.getReference("category");
        imgone = v.findViewById(R.id.img1);
        card = v.findViewById(R.id.friendCardView2);
        imgtwo = v.findViewById(R.id.img2);

        companymodel = new Companymodel();
        data = FirebaseDatabase.getInstance().getReference("Company");
        gridView = (GridView) v.findViewById(R.id.grid_view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SharedPreferences.Editor share = getActivity().getSharedPreferences("cat", MODE_PRIVATE).edit();
                share.putString("Category", listcatgory.get(position).getCatogories());
                share.commit();
                startActivity(new Intent(getContext(), ProductList.class));

            }
        });
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setHorizontalScrollBarEnabled(false);

        myLastVisiblePos = gridView.getFirstVisiblePosition();

        gridView.setOnScrollListener( new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int currentFirstVisPos = view.getFirstVisiblePosition();
                if(firstVisibleItem ==0){
                    Home.toolbar.setVisibility(View.VISIBLE);

                }
                if(currentFirstVisPos ==4) {
                    //scroll down
                    Home.toolbar.setVisibility(View.GONE);

                }
                if(currentFirstVisPos < myLastVisiblePos) {
                    //scroll up

                }
                myLastVisiblePos = currentFirstVisPos;
            }
        });

        GetImages(new firebasecallback() {
            @Override
            public void Company(Companymodel comp) {
//                li = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
//                wm = (WindowManager)getActivity().getSystemService(WINDOW_SERVICE);
////                Toast.makeText(getContext(), ""+comp.getImg1(), Toast.LENGTH_SHORT).show();
//                  params = new WindowManager.LayoutParams(
//                        //WindowManager.LayoutParams.TYPE_INPUT_METHOD |
//                        WindowManager.LayoutParams.MATCH_PARENT,
//                        WindowManager.LayoutParams.WRAP_CONTENT,
//                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                        PixelFormat.TRANSLUCENT);
//                params.gravity =  Gravity.TOP;
//                myview = li.inflate(R.layout.custoumdialog, null);
//                params.y = 50;
              card.setVisibility(View.VISIBLE);

//                rela.setVisibility(View.VISIBLE);
                if(comp.getImg1()!=null){
                    Picasso.with(getContext())
                            .load(comp.getImg1())
                            .fit()
                            .into(imgone);
                }
                if(comp.getImg2()!=null){
                    Picasso.with(getContext())
                            .load(comp.getImg2())
                            .fit()
                            .into(imgtwo);
                }
//                wm.addView(myview, params);
            }

        });



        SwipRefresh();
        return v;
    }

    public void GetImages(final firebasecallback fire){

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("img1")){
                    img1=dataSnapshot.child("img1").getValue().toString();
                    companymodel.setImg1(img1);

                }
                if(dataSnapshot.hasChild("img2")){
                    img2=dataSnapshot.child("img2").getValue().toString();
                    companymodel.setImg2(img2);
                }
                fire.Company(companymodel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public interface firebasecallback{
        void Company(Companymodel comp);
    }

    public void SwipRefresh(){
        mSwipeRefreshLayout = v.findViewById(R.id.swipe_cont);
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
    public void RetriveCategories(){
        listcatgory.clear();

        mSwipeRefreshLayout.setRefreshing(true);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Category");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Category c=dataSnapshot.getValue(Category.class);
                listcatgory.add(c);
                gridView.setAdapter(new ImageAdapterGride(getActivity(),listcatgory));
                mSwipeRefreshLayout.setRefreshing(false);
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
        RetriveCategories();
    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        
        }
}
