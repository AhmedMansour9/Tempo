package bekya.bekyaa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bekya.bekya.Common.Common;
import com.example.bekya.bekya.Interface.ItemClickListener;
import com.example.bekya.bekya.Model.Product;
import com.example.bekya.bekya.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    FirebaseStorage storage;
    RelativeLayout rootlayout;
    StorageReference storageReference;
    DatabaseReference productList;
    Uri saveUri;
    Button btnUpload;
    Button btnselect;
    Product newProduct;
EditText name,descrip , discount, price;
    String catergoryId="";
    FirebaseRecyclerAdapter<Product,ProductViewHolder> adapter;

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
        setContentView(R.layout.activity_product_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Products");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView = (RecyclerView)findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rootlayout = findViewById(R.id.rootlayout);
        FloatingTextButton floatingTextButton = findViewById(R.id.fabbutton);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabbutton);
        floatingTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showaddFooddialog();
            }
        });

        //Get Intent Here
        if (getIntent() != null) {
            catergoryId = getIntent().getStringExtra("CategoryId");
        } if (!catergoryId.isEmpty() && catergoryId != null) {
            loadListFood(catergoryId);
        }


    }

    private void showaddFooddialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductList.this);
        alertDialog.setTitle("أضف منتج جديد");
        alertDialog.setMessage("من فضلك أملآ جميع البيانات");
        LayoutInflater inflater = this.getLayoutInflater();
        View update_info_layout = inflater.inflate(R.layout.layout_add_product, null);
       name = update_info_layout.findViewById(R.id.Name);
       descrip = update_info_layout.findViewById(R.id.descrip);
        discount = update_info_layout.findViewById(R.id.discount);
        price = update_info_layout.findViewById(R.id.price);


        btnUpload = update_info_layout.findViewById(R.id.btnupload);
       btnselect = update_info_layout.findViewById(R.id.btnselect);

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadimage();
            }
        });
alertDialog.setView(update_info_layout);
        alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (newProduct != null)
                {
                    productList.push().setValue(newProduct);
                    Snackbar.make(rootlayout, "تم إضافة منجك بنجاح", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


            }
        });

        alertDialog.show();
    }

    private void uploadimage() {
        if (saveUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("يتم تحميل الصورة ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ProductList.this, "تم التحميل بنجاح", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newProduct = new Product();
                                    newProduct.setName(name.getText().toString());
                                    newProduct.setDescription(descrip.getText().toString());
                                    newProduct.setPrice(price.getText().toString());
                                    newProduct.setDiscount(discount.getText().toString());
                                    newProduct.setCategoryId(catergoryId);
                                    newProduct.setImage(uri.toString());
                                    //save url to user information table

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProductList.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading ..." + progress + "%");
                        }
                    });
        }


                        }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Image"), Common.PICKUP_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICKUP_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnselect.setText("تم تحديد الصورة");


        }

    }

    private void loadListFood(String catergoryId) {
        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productList.orderByChild("categoryId").equalTo(catergoryId)) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.product_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.product_image);

                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        {
                            Toast.makeText(ProductList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();

                           // Start New Activity
                            Intent foodDetail = new Intent(ProductList.this,ProductDetail.class);
                            foodDetail.putExtra("ProductId",adapter.getRef(position).getKey());    //send food id to new activity
                            startActivity(foodDetail);
                        }
                    }
                });
            }
        };
        //set Adapter
        recyclerView.setAdapter(adapter);
    }
}
