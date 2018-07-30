package bekya.bekyaa;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.messaging.FirebaseMessaging;

import bekya.bekyaa.Common.Common;
import bekya.bekyaa.Model.Category;
import bekya.bekyaa.tokenid.SharedPrefManager;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Home extends AppCompatActivity
        implements   NavigationView.OnNavigationItemSelectedListener {
    Fragment fr;
    private int mCurrentSelectedPosition = 0;
   // FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    TextView txtFullName;
    public static String token;
    public static Toolbar toolbar;
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
        setContentView(R.layout.activity_home);

         toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("الصفحة الرئيسية");
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.actionbarlogotw);
        //Initialize Firebase
//       DatabaseReference dat=FirebaseDatabase.getInstance().getReference().child("Category").push();
//      dat.child("catogories").setValue("ayhaga");
//      dat.child("img").setValue("https://firebasestorage.googleapis.com/v0/b/bekya-5f805.appspot.com/o/images%2FBuilding-20-512%20(1).png?alt=media&token=30c517ca-f7dc-49d9-9265-264547bfb70f");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
       // txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        //showHome();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fr instanceof Categories){
               super.onBackPressed();

            }
            else
            {
                BackToHome();
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }

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
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(String.format("%1$s", getString(R.string.app_name)));
                builder.setMessage(getResources().getText(R.string.aboutus));
                builder.setPositiveButton("ok", null);
                builder.setIcon(R.mipmap.bekyaalogo);
                AlertDialog welcomeAlert = builder.create();
                welcomeAlert.show();

                return true;
            case R.id.nav_setting:
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


}
