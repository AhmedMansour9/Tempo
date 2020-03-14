package bekya.bekyaa.Activites;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bekya.bekyaa.Fragments.Categories;
import bekya.bekyaa.Fragments.Chat;
import bekya.bekyaa.Common.Common;
import bekya.bekyaa.Fragments.MyChat;
import bekya.bekyaa.R;
import bekya.bekyaa.Fragments.myposts;
import bekya.bekyaa.tokenid.SharedPrefManager;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Home extends AppCompatActivity
        implements   NavigationView.OnNavigationItemSelectedListener {
    Fragment fr;
    private int mCurrentSelectedPosition = 0;
   // FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    public static String Id="100";
    public static String Name="الكل";
    public static TextView T_Government;
    public static RelativeLayout Rela_Govern;
    public static String token;
    public static Toolbar toolbar;
    private static final String URL_REGISTER_DEVICE = "http://wasalniegy.com/RegisterDevice.php";
    public static Boolean Visablty;
    private InterstitialAd mInterstitialAd;

    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            // Wait 60 seconds
            mHandler.postDelayed(this, 60*1000);

            // Show Ad
            showInterstitial();

        }
    };
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
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_home);
        // Create the InterstitialAd and set the adUnitId
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
        showInterstitial();
         sendTokenToServer();
           init();
         SendTokenFirebase();


    }
    private void SendTokenFirebase(){
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("admin");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String token = dataSnapshot.child("token").getValue().toString();
                    SharedPrefManager.getInstance(getBaseContext()).saveTokenAdmin(token);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start showing Ad in every 60 seconds
        //when activity is visible to the user
        mHandler = new Handler();
        //mHandler.post(mRunnable);

        // Run first add after 20 seconds
        mHandler.postDelayed(mRunnable,5*1000);


    }
    protected void onStop() {
        super.onStop();
        // Stop showing Ad when activity is not visible anymore
        mHandler.removeCallbacks(mRunnable);
    }
    private void loadInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }


    private InterstitialAd newInterstitialAd() {

        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
//                mNextLevelButton.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
//                mNextLevelButton.setEnabled(true);
            }

            @Override
            public void onAdClosed() {

                // Reload ad so it can be ready to be show to the user next time
                mInterstitialAd = newInterstitialAd();
                loadInterstitial();

            }
        });
        return interstitialAd;

    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
//            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            mInterstitialAd = newInterstitialAd();
            loadInterstitial();
        }
    }


    private void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("الصفحة الرئيسية");
        setSupportActionBar(toolbar);
//        toolbar.setLogo(R.mipmap.actionbarlogotw);
        //Initialize Firebase
        if(getIntent().getStringExtra("id")!=null) {
            Id = getIntent().getStringExtra("id");
        }
        if(getIntent().getStringExtra("name")!=null) {
            Name = getIntent().getStringExtra("name");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.darkblue));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        // txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        T_Government=findViewById(R.id.T_Government);
        Rela_Govern=findViewById(R.id.Rela_Govern);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (Visablty) {
                if (fr instanceof Categories) {
                    super.onBackPressed();

                } else if (fr instanceof myposts) {
                    BackToHome();
                } else if (fr instanceof MyChat) {
                    BackToHome();
                } else if (fr instanceof Chat) {
                    BackToHome();
                } else {
                    super.onBackPressed();
                }
            }
            else {

                super.onBackPressed();
            }
            }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    private void BackToHome()
    {
        fr = new Categories();
        if(fr !=null)
        {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.flContent,fr,fr.getTag()).commit();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.nav_products:
                mCurrentSelectedPosition = 0;
                fr = new Categories();
                break;
            case R.id.posts:
                mCurrentSelectedPosition = 1;

                fr = new myposts();
                break;
            case R.id.mychat:
                mCurrentSelectedPosition = 2;

                fr = new MyChat();
                break;

            case R.id.chat:
                mCurrentSelectedPosition = 3;

                fr = new Chat();
                break;

            case R.id.about:
                mCurrentSelectedPosition = 4;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(String.format("%1$s", getString(R.string.app_name)));
                builder.setMessage(getResources().getText(R.string.aboutus));
                builder.setPositiveButton("ok", null);
                builder.setIcon(R.mipmap.bekyaalogo);
                AlertDialog welcomeAlert = builder.create();
                welcomeAlert.show();

                return true;
            case R.id.nav_setting:
                mCurrentSelectedPosition = 4;
                showSettingdialog();




            default:
                mCurrentSelectedPosition = 0;

        }
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }
        item.setChecked(true);


        FragmentManager fragmentManager=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flContent,fr);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showSettingdialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("ضبط الإشعارات");
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_setting= inflater.inflate(R.layout.setting_layout,null);
        final CheckBox chk_subscribe =layout_setting.findViewById(R.id.chk_new);
        //remember state of checkbox
        Paper.init(this);
        String isSubscribe = Paper.book().read("sub_new","true");
        if(isSubscribe==null|| TextUtils.isEmpty(isSubscribe) || isSubscribe.equals("false"))
            chk_subscribe.setChecked(false);
        else
            chk_subscribe.setChecked(true);

        alertDialog.setView(layout_setting);
        alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(chk_subscribe.isChecked())
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(Common.topicName);
                    Paper.book().write("sub_new" , "true");
                }
                else
                {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Common.topicName);
                    Paper.book().write("sub_new" , "false");
                }

            }
        });
        alertDialog.show();


    }

    private void sendTokenToServer() {
            final String token = SharedPrefManager.getInstance(this).getDeviceToken();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("email", token);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }































}
