package com.tempo.Activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.tempo.ChangeLanguage;
import com.tempo.Fragments.Categories;
import com.tempo.Fragments.Chat;
import com.tempo.Common.Common;
import com.tempo.Fragments.ContactUs;
import com.tempo.Fragments.Language;
import com.tempo.Fragments.MyChat;
import com.tempo.R;
import com.tempo.Fragments.myposts;
import com.tempo.tokenid.SharedPrefManager;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Home extends AppCompatActivity
        implements   NavigationView.OnNavigationItemSelectedListener {
    Fragment fr;
    private int mCurrentSelectedPosition = 0;
   // FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    public static String Id="";
    public static String Name="";
    public static TextView T_Government;
    public static RelativeLayout Rela_Govern;
    public static String token,Language;
    public static String Social_Id;
    public static Toolbar toolbar;
    private static final String URL_REGISTER_DEVICE = "http://wasalniegy.com/RegisterDevice.php";
    public static Boolean Visablty;
    private InterstitialAd mInterstitialAd;
    FirebaseAuth mAuth;
    SharedPreferences shared;

    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            // Wait 60 seconds
            mHandler.postDelayed(this, 100000);

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
        shared=getSharedPreferences("Language",MODE_PRIVATE);
        String Lan=shared.getString("Lann",null);
        if(Lan!=null) {
            Locale locale = new Locale(Lan);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }
        setContentView(R.layout.activity_home);
        // Create the InterstitialAd and set the adUnitId
        mInterstitialAd = newInterstitialAd();
        mAuth = FirebaseAuth.getInstance();
        Language=ChangeLanguage.getLanguage(this);
//        loadInterstitial();
//        showInterstitial();
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
        mHandler.postDelayed(mRunnable,6000);


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
        Rela_Govern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,Cities.class));
            }
        });
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

                fr = new ContactUs();
                break;

//            case R.id.about:
//                mCurrentSelectedPosition = 4;
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle(String.format("%1$s", getString(R.string.app_name)));
//                builder.setMessage(getResources().getText(R.string.aboutus));
//                builder.setPositiveButton("ok", null);
//                builder.setIcon(R.mipmap.bekyaalogo);
//                AlertDialog welcomeAlert = builder.create();
//                welcomeAlert.show();


            case R.id.nav_language:
                mCurrentSelectedPosition = 3;

                fr = new Language();
                break;



            case R.id.log_out:

                mCurrentSelectedPosition = 5;
                mAuth.signOut();
                Intent intent=new Intent(Home.this,Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
              break;


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















//
//    DatabaseReference da5=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        da5.child("id").setValue("0");
//        da5.child("name").setValue("Algeria");
//        da5.child("name_ar").setValue("الجزائر");
//        da5.child("cu").setValue("DZD");
//
//    DatabaseReference dat=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        dat.child("id").setValue("1");
//        dat.child("name").setValue("Bahrain");
//        dat.child("name_ar").setValue("البحرين");
//        dat.child("cu").setValue("BHD");
//
//    DatabaseReference datsss=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        datsss.child("id").setValue("2");
//        datsss.child("name").setValue("Iraq");
//        datsss.child("name_ar").setValue("Iraq");
//        datsss.child("cu").setValue("IQD");
//
//    DatabaseReference dat3=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        dat3.child("id").setValue("3");
//        dat3.child("name").setValue("Lebanon");
//        dat3.child("name_ar").setValue("لبنان");
//        dat3.child("cu").setValue("LBP");
//
//    DatabaseReference dat4=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        dat4.child("id").setValue("4");
//        dat4.child("name").setValue("Libya");
//        dat4.child("name_ar").setValue("ليبيا");
//        dat4.child("cu").setValue("LD");
//
//    DatabaseReference dat5=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        dat5.child("id").setValue("5");
//        dat5.child("name").setValue("Morocco");
//        dat5.child("name_ar").setValue("المغرب");
//        dat5.child("cu").setValue("MAD");
//
//    DatabaseReference dat6=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        dat6.child("id").setValue("6");
//        dat6.child("name").setValue("Palestine");
//        dat6.child("name_ar").setValue("فلسطين");
//        dat6.child("cu").setValue("Shekel");
//
//    DatabaseReference da7=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        da7.child("id").setValue("7");
//        da7.child("name").setValue("Sudan");
//        da7.child("name_ar").setValue("السوادن");
//        da7.child("cu").setValue("SDG");
//
//    DatabaseReference dat8=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        dat8.child("id").setValue("8");
//        dat8.child("name").setValue("Syria");
//        dat8.child("name_ar").setValue("سوريا");
//        dat8.child("cu").setValue("SYP");
//
//    DatabaseReference dat9=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        dat9.child("id").setValue("9");
//        dat9.child("name").setValue("Tunisia");
//        dat9.child("name_ar").setValue("تونس");
//        dat9.child("cu").setValue("TND");
//
//    DatabaseReference dat10=FirebaseDatabase.getInstance().getReference().child("Cities").push();
//        dat10.child("id").setValue("10");
//        dat10.child("name").setValue("Yemen");
//        dat10.child("name_ar").setValue("اليمن");
//        dat10.child("cu").setValue("YR");










//    DatabaseReference da5=FirebaseDatabase.getInstance().getReference().child("Category").push();
//        da5.child("cat_ar").setValue("المساعد الإفتراصي");
//        da5.child("cat_en").setValue("Virtual Assistant");
//            String key =da5.getKey();
//        da5.child("key").setValue(key);
//
//        DatabaseReference da6=FirebaseDatabase.getInstance().getReference().child("Category").push();
//        da6.child("cat_ar").setValue("أخرى");
//        da6.child("cat_en").setValue("Others");
//        String key6 =da6.getKey();
//        da6.child("key").setValue(key6);
//
//        DatabaseReference da7=FirebaseDatabase.getInstance().getReference().child("Category").push();
//        da7.child("cat_ar").setValue("الدورات المجانية");
//        da7.child("cat_en").setValue("Free Courses");
//        String key7 =da7.getKey();
//        da7.child("key").setValue(key7);




    //        DatabaseReference da5=FirebaseDatabase.getInstance().getReference().child("Sub_Category").push();
//        da5.child("cat_ar").setValue("التواصل الاجتماعي");
//        da5.child("cat_en").setValue("Social media");
//        da5.child("key").setValue("-M7-z6ANpmN8hf_CCfgS");
//        String key =da5.getKey();
//        da5.child("sub_key").setValue(key);
//
//        DatabaseReference da6=FirebaseDatabase.getInstance().getReference().child("Sub_Category").push();
//        da6.child("cat_ar").setValue("إعلانات");
//        da6.child("cat_en").setValue("Advertisment");
//        da6.child("key").setValue("-M7-z6ANpmN8hf_CCfgS");
//        String key6 =da6.getKey();
//        da6.child("sub_key").setValue(key6);
//
//        DatabaseReference da3=FirebaseDatabase.getInstance().getReference().child("Sub_Category").push();
//        da3.child("cat_ar").setValue("التلفاز");
//        da3.child("cat_en").setValue("TV");
//        da3.child("key").setValue("-M7-z6ANpmN8hf_CCfgS");
//        String key3 =da3.getKey();
//        da3.child("sub_key").setValue(key3);
//

//        DatabaseReference da7=FirebaseDatabase.getInstance().getReference().child("Sub_Category").push();
//        da7.child("cat_ar").setValue("عامة");
//        da7.child("cat_en").setValue("General");
//        da7.child("key").setValue("-M7-z6AVf5xPq30GChFL");
//        String key7 =da7.getKey();
//        da7.child("sub_key").setValue(key7);



}
