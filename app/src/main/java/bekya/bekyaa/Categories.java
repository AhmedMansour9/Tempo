package bekya.bekyaa;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bekya.bekyaa.Model.Category;
import bekya.bekyaa.Model.Companymodel;
import bekya.bekyaa.adapter.ImageAdapterGride;
import bekya.bekyaa.tokenid.SharedPrefManager;
import me.relex.circleindicator.CircleIndicator;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Categories extends Fragment implements  Open_Galler_View,SwipeRefreshLayout.OnRefreshListener {


    public Categories() {
        // Required empty public constructor
    }
    SliderPagerAdapter sliderPagerAdapter;
    ViewPager viewPager;
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
    String img1, img2;
    Companymodel companymodel;
    RelativeLayout relativeLayout;
    RelativeLayout rela;
    DatabaseReference data;
    WindowManager.LayoutParams params;
    ImageView imgone, imgtwo;
    CardView card;
    Context con;
    Gallery gallery;
    private RecyclerView rv_autoScroll;
    LinearLayoutManager linearLayoutManager;
    Boolean end;
    Timer timer;
    int position = 0;
    Context context;
    Banner_Adapter banerAdapter;
    List<Gallery> list = new ArrayList<>();
    int dxx, dyy;
    private ViewPager vp_slider;
    List<Gallery> banne = new ArrayList<>();
    final int duration = 2500;
    final int pixelsToMove = 400;
    int page_position = 0;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    final Handler handler = new Handler();
//
        final Runnable update = new Runnable() {
            public void run() {
//                if (page_position == banne.size()-1) {
//                    page_position --;
//                } else {
//                    page_position ++;
//                }

                if(position == list.size()-1){
                    end = true;
                }
                else if (position == 0) {
                    end = false;
                }
                if(!end){
                    position++;
                } else {
                    position--;
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
        listcatgory = new ArrayList<>();
        con = this.getActivity();
        context = this.getActivity();
        database = FirebaseDatabase.getInstance();
        category = database.getReference("category");
        card = v.findViewById(R.id.friendCardView2);
        rv_autoScroll = v.findViewById(R.id.recycler_banner2);
//        viewPager=v.findViewById( R.id.vp_slider );
//        skip=findViewById( R.id.view_pager_text_skip );

       GetImages();
        companymodel = new Companymodel();
        data = FirebaseDatabase.getInstance().getReference("Company");
        gridView = (GridView) v.findViewById(R.id.grid_view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SharedPreferences.Editor share = getActivity().getSharedPreferences("cat", MODE_PRIVATE).edit();
                share.putString("Category", listcatgory.get(position).getCatogories());
                share.putString("categoryadmin", listcatgory.get(position).getCategory());
                share.commit();
                startActivity(new Intent(getContext(), ProductList.class));

            }
        });
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setHorizontalScrollBarEnabled(false);

        myLastVisiblePos = gridView.getFirstVisiblePosition();

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int currentFirstVisPos = view.getFirstVisiblePosition();
                if (firstVisibleItem == 0) {
                    Home.toolbar.setVisibility(View.VISIBLE);

                }
                if (currentFirstVisPos == 4) {
                    //scroll down
                    Home.toolbar.setVisibility(View.GONE);

                }
                if (currentFirstVisPos < myLastVisiblePos) {
                    //scroll up

                }
                myLastVisiblePos = currentFirstVisPos;
            }
        });


//        vp_slider = (ViewPager) v.findViewById(R.id.vp_slider);
//        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

//        slider_image_list = new ArrayList<>();

//        slider_image_list.add("http://images.all-free-download.com/images/graphiclarge/bird_mountain_bird_animal_226401.jpg");


//        GetImages(new firebasecallback() {
//            @Override
//            public void Company(Companymodel comp) {
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

//                wm.addView(myview, params);
//            }
//
//        });
//        GetImages(new firebasecallback() {
//            @Override
//            public void Company(List<Gallery> comp) {
//                sliderPagerAdapter = new SliderPagerAdapter(getActivity(), comp);
//                vp_slider.setAdapter(sliderPagerAdapter);
//                CircleIndicator circleIndicator=v.findViewById( R.id.view_pager_circle_indicator );
//                circleIndicator.setViewPager( vp_slider );
////                Timer timer = new Timer();
////                timer.scheduleAtFixedRate(new SliderTimer(), 4000, 8000);
//
//
////                rv_autoScroll.addOnScrollListener(new RecyclerView.OnScrollListener() {
////                    @Override
////                    public void onScrolled(RecyclerView recyclerView, final int dx, final int dy) {
////                        super.onScrolled(recyclerView, dx, dy);
////                        dxx=dx;
////                        dyy=dy;
////                        int lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
////                        if(lastItem == linearLayoutManager.getItemCount()-1){
////                            mHandler.removeCallbacks(SCROLLING_RUNNABLE);
////                            Handler postHandler = new Handler();
////                            postHandler.postDelayed(new Runnable() {
////                                @Override
////                                public void run() {
////                                    rv_autoScroll.setAdapter(null);
////                                    rv_autoScroll.setAdapter(banerAdapter);
////                                    mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
////
////                                }
////                            }, 5000);
////                        }
////                    }
////                });
////                mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
//            }
//        });

//        vp_slider.setOnPageChangeListener( new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                Intent facebookIntent = getOpenFacebookIntent(getContext(),banne.get(position).getLinl());
////                startActivity(facebookIntent);
//
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//                if(state==ViewPager.SCROLL_STATE_IDLE)
//                {
////                    int pagesCount=images.length;
//                    /*if(CURRENT_PAGE==0)
//                    {
//                        viewPager.setCurrentItem( pagesCount-1,false );
//                    }else if(CURRENT_PAGE==pagesCount-1)
//                    {
//                        viewPager.setCurrentItem( 0,false );
//                    }*/
//
//                }
//            }
//        } );
        SwipRefresh();

        return v;
    }

    //    private class SliderTimer extends TimerTask {
//
//        @Override
//        public void run() {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (position < banne.size() - 1) {
//                        vp_slider.setCurrentItem(position);
//                       position++;
//                    } else {
//                        position=0;
//                        vp_slider.setCurrentItem(0);
//                    }
//                }
//            });
//        }
//    }
    @Override
    public void delete(String a) {

        if (a != null) {
//            if (a.contains("http")) {
//            Intent i = new Intent(Intent.ACTION_VIEW,
//                    Uri.parse(a));
//            startActivity(i);
//                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
//                String facebookUrl = getFacebookPageURL(getContext(), a);
//                facebookIntent.setData(Uri.parse(facebookUrl));
//                startActivity(facebookIntent);

                Intent facebookIntent = getOpenFacebookIntent(getContext(),a);
                startActivity(facebookIntent);

//            }
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


    public String getFacebookPageURL(Context context,String a) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
//            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + a;
//            } else { //older versions of fb app
////                return "fb://page/" + FACEBOOK_PAGE_ID;
//            }
        } catch (PackageManager.NameNotFoundException e) {
            return a; //normal web url
        }
    }

    private class AutoScrollTask extends TimerTask {
        @Override
        public void run() {
            try {
            if(position == list.size()){
                end = true;
            }
            else if (position == 0) {
                end = false;
            }
            if(!end){
                position++;
                rv_autoScroll.smoothScrollToPosition(position);
                return;
            } else {
                position--;
                rv_autoScroll.smoothScrollToPosition(position);
                return;
            }



            }catch ( Exception e){

            }
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
                card.setVisibility(View.VISIBLE);
                banne = list;
                banerAdapter = new Banner_Adapter(list, getContext());
                banerAdapter.DeleteImage(Categories.this);
                linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv_autoScroll.setLayoutManager(linearLayoutManager);
                rv_autoScroll.setAdapter(banerAdapter);

//                sliderPagerAdapter=new SliderPagerAdapter( getContext(),list );
//                vp_slider.setAdapter( sliderPagerAdapter );
//                CircleIndicator circleIndicator=v.findViewById( R.id.view_pager_circle_indicator );
//                circleIndicator.setViewPager( vp_slider );
//                if (context != null) {
//                    timer = new Timer();
//                    timer.scheduleAtFixedRate(new AutoScrollTask(), 2000, 4000);
//                }
                Timer swipeTimer = new Timer();
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(update);
                    }
                }, 2000, 2000);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public interface firebasecallback{
        void Company(List<Gallery> comp);
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
      context=null;
//        timer.cancel();
        v=null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        context=null;
//        timer.cancel();
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

}
