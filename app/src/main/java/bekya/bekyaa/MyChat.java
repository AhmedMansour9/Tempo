package bekya.bekyaa;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bekya.bekyaa.tokenid.SharedPrefManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyChat extends Fragment implements Token_View,SwipeRefreshLayout.OnRefreshListener{
    DatabaseReference datamsg;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String Msg;
    public static String Myuserid;
    public static Uri Myuserphoto;
    String userid;
    RecyclerView recyclerView;
    lastmessage mAdapter;
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
    String Id;


    public MyChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_my_chat, container, false);
        token = SharedPrefManager.getInstance(getContext()).getDeviceToken();
//        data= FirebaseDatabase.getInstance().getReference("Users");
        datamsg= FirebaseDatabase.getInstance().getReference("ChatUsers");

        Recyclview();




        SwipRefresh();



        return view;
    }
    public void loadesmassg(){
        DatabaseReference datams=datamsg.child(token);

//        Query mqery=datams.limitToLast(padge+totalnumber);

        datams.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                moviesList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    DatabaseReference datams=datamsg.child(token).child(dataSnapshot1.getKey());

                    Query mqery=datams.limitToLast(1);

                    mqery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            meesage a=dataSnapshot.getValue(meesage.class);
                            if(a.getTo().equals(token)){

                            }else {
                                Id=a.getTo();
                            }
                            if(a.getFrom().equals(token)){

                            }else {
                                Id=a.getFrom();
                            }
                            if (a != null && !hasId(Id)) {
                                moviesList.add(0, a);

                            }
//                          Collections.reverse(moviesList);
                            mAdapter.notifyDataSetChanged();
//                          recyclerView.scrollToPosition(moviesList.size()-1);
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

                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private boolean hasId(String idc){
        if(!TextUtils.isEmpty(idc)) {
            for (meesage fr : moviesList) {
                if (fr.getFrom().equals(idc)||fr.getTo().equals(idc)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public void SwipRefresh(){
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_Chat);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                loadesmassg();
            }
        });

    }


    public void Recyclview(){
        recyclerView = view.findViewById(R.id.recycler_Chat);
        recyclerView.setHasFixedSize(true);
        mAdapter = new lastmessage(moviesList,getContext());
        mAdapter.setClickListener(this);
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
        mSwipeRefreshLayout.setRefreshing(true);
        loadesmassg();
    }

    @Override
    public void token(String token) {
        ChatDetails chatDetails=new ChatDetails();
        Bundle args = new Bundle();
        args.putString("token",token);
        chatDetails.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.chatt, chatDetails )
                .addToBackStack(null)
                .commitAllowingStateLoss();



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
