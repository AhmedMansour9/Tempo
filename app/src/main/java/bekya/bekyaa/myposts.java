package bekya.bekyaa;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bekya.bekyaa.Interface.itemViewinterface;
import bekya.bekyaa.Model.Retrivedata;
import bekya.bekyaa.adapter.Adapteritems;
import bekya.bekyaa.adapter.imgclick;
import bekya.bekyaa.tokenid.SharedPrefManager;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class myposts extends Fragment implements SwipeRefreshLayout.OnRefreshListener,imgclick {
    DatabaseReference data;
    RecyclerView recyclerView;
    private Adapteritems mAdapter;
    List<Retrivedata> array;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ImageView imgone,imgtwo,imgthree,imgfour;
    ImageView deltone,deletetwo,deletethree,deletefour;
    ImageView cameraone,cameratwo,camerathree,camerafour;
    EditText editname,editdiscrp,editdiscount,editphone;
    FrameLayout rootlayout;
    Dialog update_items_layout;
    String imgOne,imgTwo,imgThree,imgFour;
    private Uri filePathone,filePathtwo,filePaththree,filePathfour;
    FirebaseStorage storage;
    StorageReference storageReference;
    public static String token;
    ArrayList<CustomGallery> dataT;
    String Name,Discrption,Discount,Price;
    String child;
    String Nameone;
    Button finish;
    public ChildEventListener mListener;
    public myposts() {
        // Required empty public constructor
    }

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.myposts, container, false);

        data= FirebaseDatabase.getInstance().getReference().child("Products");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        array=new ArrayList<>();
        rootlayout =v.findViewById(R.id.rootlayout);

        token = SharedPrefManager.getInstance(getContext()).getDeviceToken();
        Recyclview();
        SwipRefresh();


        return v;
    }
    public void Recyclview(){
        recyclerView =v.findViewById(R.id.recyclerposts);
        recyclerView.setHasFixedSize(true);
        mAdapter = new Adapteritems(array,getContext());
        mAdapter.setClickButton(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onRefresh() {
        Retrivedata();
    }

    @Override
    public void onClickCallback(View view, int adapterPosition) {
        update_items_layout = new Dialog(getContext());
        update_items_layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        update_items_layout.setContentView(R.layout.edititems);
        init2();
        Nameone=array.get(adapterPosition).getName();
        child=array.get(adapterPosition).getKey();
        imgOne=array.get(adapterPosition).getImg1();
        if(imgOne!=null){
            Uri u = Uri.parse(imgOne);
            Picasso.with(getContext())
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(imgone);

        }else {
            deltone.setVisibility(View.INVISIBLE);
        }
        imgTwo=array.get(adapterPosition).getImg2();
        if(imgTwo!=null){
            Uri u = Uri.parse(imgTwo);
            Picasso.with(getContext())
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(imgtwo);
        }else {
            deletetwo.setVisibility(View.INVISIBLE);
        }
        imgThree=array.get(adapterPosition).getImg3();
        if(imgThree!=null){
            Uri u = Uri.parse(imgThree);
            Picasso.with(getContext())
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(imgthree);
        }else {
            deletethree.setVisibility(View.INVISIBLE);
        }

        imgFour=array.get(adapterPosition).getImg4();
        if(imgFour!=null){
            Uri u = Uri.parse(imgFour);
            Picasso.with(getContext())
                    .load(u)
                    .fit()
                    .placeholder(R.drawable.no_media)
                    .into(imgfour);
        }else {
            deletefour.setVisibility(View.INVISIBLE);
        }
        editname.setText(array.get(adapterPosition).getName());
        editdiscrp.setText(array.get(adapterPosition).getDiscrption());
        editdiscount.setText(array.get(adapterPosition).getDiscount());
        editphone.setText(array.get(adapterPosition).getPhone());
        clickimgeon();
        clickimgtwo();
        clickimgethree();
        clickimgfour();

        deleteone();
        deletetwo();
        deletethree();
        deletefour();

        btnfinish();


        update_items_layout.show();

    }

    @Override
    public void onClickdelete(View view, final int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do You Want to Delete This Post ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Name =array.get(adapterPosition).getName();
              String key=array.get(adapterPosition).getKey();
                data.removeEventListener(mListener);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(key);
                databaseReference.orderByChild("name").equalTo(Name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            data.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                array.remove(adapterPosition);
                mAdapter.notifyDataSetChanged();

                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();


    }

    public void SwipRefresh(){
        mSwipeRefreshLayout = v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                Retrivedata();
            }
        });
    }
    private boolean hasId(String idc){
        if(!TextUtils.isEmpty(idc)) {
            for (Retrivedata fr : array) {
                if (fr.getToken().equals(idc)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
    public void Retrivedata(){
        array.clear();
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(true);
        mListener=data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String key = dataSnapshot.getKey();

                DatabaseReference datas= FirebaseDatabase.getInstance().getReference().child("Products");
                datas.child(key).orderByChild("token").equalTo(token).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.exists()) {
                            Retrivedata r = dataSnapshot.getValue(Retrivedata.class);

                            if(r!=null &&!hasId(r.getName())) {
                                r.setKey(key);
                                array.add( r);
                                mAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                        }
                        else {
                            mSwipeRefreshLayout.setRefreshing(false);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 50 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePathone=null;
            filePathone = data.getData();
            if(filePathone != null)
            {
                String imagechild="img1";
                imgfileone(filePathone,imagechild,imgone,Nameone);
            }
        }else   if(requestCode == 100 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePathtwo=null;
            filePathtwo = data.getData();
            if(filePathtwo != null)
            {
                String imagechild="img2";
                imgfileone(filePathtwo,imagechild,imgtwo,Nameone);
            }
        }

        else   if(requestCode == 150 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePaththree=null;
            filePaththree = data.getData();
            if(filePaththree != null)
            {

                String imagechild="img3";
                imgfileone(filePaththree,imagechild,imgthree,Nameone);

            }
        }  else   if(requestCode == 250 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePathfour=null;
            filePathfour = data.getData();
            if(filePathfour != null)
            {
                String imagechild="img4";
                imgfileone(filePathfour,imagechild,imgfour,Nameone);

            }
        }
    }
    public void init2(){
        imgone=update_items_layout.findViewById(R.id.imgone);
        imgtwo=update_items_layout.findViewById(R.id.imgtwo);
        imgthree=update_items_layout.findViewById(R.id.imgthree);
        imgfour=update_items_layout.findViewById(R.id.imgfour);
        deltone=update_items_layout.findViewById(R.id.deleteone);
        deletetwo=update_items_layout.findViewById(R.id.deletetwo);
        deletethree=update_items_layout.findViewById(R.id.deletethree);
        deletefour=update_items_layout.findViewById(R.id.deletrefour);
        cameraone =update_items_layout.findViewById(R.id.cameraeone);
        cameratwo =update_items_layout.findViewById(R.id.cameratwo);
        camerathree =update_items_layout.findViewById(R.id.camerathree);
        camerafour =update_items_layout.findViewById(R.id.camerafour);
        editname=update_items_layout.findViewById(R.id.editname);
        editdiscrp=update_items_layout.findViewById(R.id.editdiscrp);
        editdiscount=update_items_layout.findViewById(R.id.editdiscount);
        editphone=update_items_layout.findViewById(R.id.editphone);
        finish=update_items_layout.findViewById(R.id.finish);
    }
    public void clickimgeon(){
        cameraone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 50);

            }
        });

    }
    public void clickimgtwo(){
        cameratwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 100);

            }
        });

    }
    public void clickimgethree(){
        camerathree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 150);

            }
        });

    }
    public void clickimgfour(){
        camerafour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 250);

            }
        });
    }

    public void imgfileone(Uri filePath,final String childimage, final ImageView image, final String IMAGE) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        final Uri u = taskSnapshot.getDownloadUrl();
                        Picasso.with(getActivity())
                                .load(u)
                                .fit()
                                .placeholder(R.drawable.no_media)
                                .into(image);

                        DatabaseReference data=FirebaseDatabase.getInstance().getReference().child("Products").child(child);
                        data.orderByChild("name").equalTo(IMAGE).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot data:dataSnapshot.getChildren()){
                                    data.getRef().child(childimage).setValue(u.toString());

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });

    }
    public void deleteimage(final String childimage){
        data.child(child).orderByChild("name").equalTo(Nameone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    data.getRef().child(childimage).removeValue();                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void deleteone(){
        deltone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageone="img1";
                deleteimage(imageone);
                imgone.setImageResource(R.drawable.no_media);
                deltone.setVisibility(View.INVISIBLE);
            }
        });


    }
    public void deletetwo(){
        deletetwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageone="img2";
                deleteimage(imageone);
                imgtwo.setImageResource(R.drawable.no_media);
                deletetwo.setVisibility(View.INVISIBLE);
            }
        });


    }
    public void deletethree(){
        deletethree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageone="img3";
                deleteimage(imageone);
                imgthree.setImageResource(R.drawable.no_media);
                deletethree.setVisibility(View.INVISIBLE);
            }
        });


    }
    public void deletefour(){
        deletefour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageone="img4";
                deleteimage(imageone);
                imgfour.setImageResource(R.drawable.no_media);
                deletefour.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void btnfinish(){
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editname.getText().toString().isEmpty()||editdiscount.getText().toString().isEmpty()||
                        editdiscrp.getText().toString().isEmpty()||editphone.getText().toString().isEmpty() ){
                    Toast.makeText(getContext(), "من فضلك املئ البيانات ", Toast.LENGTH_SHORT).show();

                }else {
                    data.child(child).orderByChild("name").equalTo(Nameone).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data:dataSnapshot.getChildren()){
                                data.getRef().child("name").setValue(editname.getText().toString());
                                data.getRef().child("discount").setValue(editdiscount.getText().toString());
                                data.getRef().child("discrption").setValue(editdiscrp.getText().toString());
                                data.getRef().child("phone").setValue(editphone.getText().toString());

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    update_items_layout.dismiss();
                    Snackbar.make(rootlayout, "تم تعديل منجك بنجاح", Snackbar.LENGTH_SHORT)
                            .show();

                }

            }
        });
    }

}
