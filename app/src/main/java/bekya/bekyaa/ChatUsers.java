package bekya.bekyaa;

import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;

import bekya.bekyaa.adapter.Messages;
import bekya.bekyaa.tokenid.SharedPrefManager;

public class ChatUsers extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    DatabaseReference datamsg;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String Msg;
    public static String Myuserid;
    public static Uri Myuserphoto;
    String userid;
    RecyclerView recyclerView;
    bekya.bekyaa.adapter.Messages mAdapter;
    List<meesage> moviesList=new ArrayList<>();
    public   static int totalnumber=10;
    private int itempostion=0;
    private int padge=1;
    private String key="";
    private String prekey="";
    LinearLayoutManager linearLayoutManager;
    ValueEventListener value;
    DatabaseReference dat;
    public static String image;
    TextView username,online;
    ImageView imgefriend,sendmessage;
    EditText Messages;
    View view;
    String token;
    String tokenadmin;
    String TokenUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users);

        sendmessage=findViewById(R.id.Btn_SendMessage);
        Messages=findViewById(R.id.E_Messsage);
        token = SharedPrefManager.getInstance(this).getDeviceToken();
        tokenadmin=SharedPrefManager.getInstance(this).getTokenAdmin();
//        data= FirebaseDatabase.getInstance().getReference("Users");
        datamsg= FirebaseDatabase.getInstance().getReference("ChatUsers");
//        dat=FirebaseDatabase.getInstance().getReference("Users").child(userid);
       TokenUser=getIntent().getStringExtra("tokenuser");
        Recyclview();

        SendMeesges();
        loadesmassg();
        SwipRefresh();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                padge++;

                itempostion=0;
                loademoresmassg();
            }
        });


    }
    public void loadesmassg(){
        DatabaseReference datams=datamsg.child(token).child(TokenUser);

        Query mqery=datams.limitToLast(padge+totalnumber);

        mqery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                meesage a=dataSnapshot.getValue(meesage.class);

                itempostion++;
                if(itempostion==1){
                    String KEY=dataSnapshot.getKey();
                    key=KEY;
                    prekey=KEY;
                }
                moviesList.add(a);
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(moviesList.size()-1);
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
    public void SendMessage(final String token, final String Msg){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://bekyaaa.000webhostapp.com/pushem.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(ChatUsers.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ChatUsers.this, ""+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("title", "User");


                params.put("message", Msg);
                params.put("email", token);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);

    }
    public void SendMeesges(){
        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tokenadmin=SharedPrefManager.getInstance(getBaseContext()).getTokenAdmin();
                String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).format(new Date());
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                format.setTimeZone(new SimpleTimeZone(0, "GMT"));


                Msg = Messages.getText().toString().trim();
                if (Msg != null && Msg.equals("")) {
                } else {
                    SendMessage(TokenUser,Msg);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Msg", Msg);
                    map.put("from", token);
                    map.put("to", TokenUser);
                    map.put("date", date);
                    datamsg.child(token).child(TokenUser).push().setValue(map);
                    datamsg.child(TokenUser).child(token).push().setValue(map);
                    Messages.setText("");
                }
            }


        });


    }

    public void SwipRefresh(){
        mSwipeRefreshLayout = findViewById(R.id.swipe_Chat);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


    }
    private void loademoresmassg() {
        DatabaseReference datams=datamsg.child(token).child(TokenUser);

        Query mqery=datams.orderByKey().endAt(key).limitToLast(10);
        mqery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                meesage a=dataSnapshot.getValue(meesage.class);
                String KEY=dataSnapshot.getKey();
                if(!prekey.equals(KEY)) {

                    moviesList.add(itempostion++,a);

                }else {
                    prekey=key;
                }

                if(itempostion==1){

                    key=KEY;
                }
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                linearLayoutManager.scrollToPositionWithOffset(10,0);
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

    public void Recyclview(){
        recyclerView = findViewById(R.id.recycler_Chat);
        recyclerView.setHasFixedSize(true);
        mAdapter = new Messages(moviesList,this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onStop() {
        super.onStop();
//        finish();



    }

    @Override
    public void onPause() {
        super.onPause();
//        finish();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();



    }

    @Override
    public void onRefresh() {

    }
}