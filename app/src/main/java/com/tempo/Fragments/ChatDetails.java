package com.tempo.Fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
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

import com.tempo.Activites.Home;
import com.tempo.MyVolley;
import com.tempo.R;
import com.tempo.adapter.Messages;
import com.tempo.Model.meesage;
import com.tempo.tokenid.SharedPrefManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatDetails extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    public ChatDetails() {
        // Required empty public constructor
    }

    DatabaseReference datamsg;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String Msg;
    public static String Myuserid;
    public static Uri Myuserphoto;
    String userid;
    RecyclerView recyclerView;
    com.tempo.adapter.Messages mAdapter;
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
    FirebaseAuth mAuth;
    ImageView imgefriend,sendmessage;
    EditText Messages;
    View view;
    String usertoken,SocialUser,mytoken,MySocial;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_chat_details, container, false);
        mAuth = FirebaseAuth.getInstance();

        mytoken = SharedPrefManager.getInstance(getContext()).getDeviceToken();
        sendmessage=view.findViewById(R.id.Btn_SendMessage);
        Messages=view.findViewById(R.id.E_Messsage);
        MySocial=SharedPrefManager.getInstance(getContext()).getSocialId();
        Bundle a=getArguments();
        if(a!=null) {
            usertoken = a.getString("token");
            SocialUser=a.getString("social");
        }
//        data= FirebaseDatabase.getInstance().getReference("Users");
        datamsg= FirebaseDatabase.getInstance().getReference("ChatUsers");
//        dat=FirebaseDatabase.getInstance().getReference("Users").child(userid);

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


        return view;
    }
    public void loadesmassg(){
        DatabaseReference datams=datamsg.child(MySocial).child(SocialUser);

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
    public void SendMeesges(){
        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).format(new Date());
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                format.setTimeZone(new SimpleTimeZone(0, "GMT"));


                Msg = Messages.getText().toString().trim();
                if (Msg != null && Msg.equals("")) {
                } else {
                    SendMessage(usertoken,Msg);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Msg", Msg);
                    map.put("from",MySocial);
                    map.put("to", SocialUser);
                    map.put("from_token", mytoken);
                    map.put("to_token", usertoken);

                    map.put("date", date);
                    datamsg.child(SocialUser).child(MySocial).push().setValue(map);
                    datamsg.child(MySocial).child(SocialUser).push().setValue(map);
                    Messages.setText("");
                }
            }


        });


    }

    public void SwipRefresh(){
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_Chat);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


    }
    private void loademoresmassg() {
        DatabaseReference datams=datamsg.child(MySocial).child(SocialUser);

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
        recyclerView = view.findViewById(R.id.recycler_Chat);
        recyclerView.setHasFixedSize(true);
        mAdapter = new Messages(moviesList,getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());
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

    public void SendMessage(final String token, final String Msg){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://wasalniegy.com/pushem.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

                params.put("title", mAuth.getCurrentUser().getDisplayName());


                params.put("message", Msg);
                params.put("email", token);
                return params;
            }
        };

        MyVolley.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            Home.Visablty = false;
        } else {
            Home.Visablty = true;
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Home.Visablty=false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Home.Visablty = true;

    }

}
