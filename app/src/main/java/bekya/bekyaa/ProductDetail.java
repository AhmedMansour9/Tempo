package bekya.bekyaa;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bekya.bekya.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductDetail extends AppCompatActivity {

    TextView product_Name,product_Price,product_Description;
    ImageView product_Image;
    CollapsingToolbarLayout collapsingToolbarLayout;

    String productId="";

    FirebaseDatabase database;
    DatabaseReference products;

    Product currentProduct;
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
        setContentView(R.layout.activity_product_detail);


        //Firebase
        database = FirebaseDatabase.getInstance();
        products = database.getReference("Products");

        //Initialize view



        product_Description = (TextView)findViewById(R.id.product_description);
        product_Name = (TextView)findViewById(R.id.product_name);
        product_Price = (TextView)findViewById(R.id.product_price);
        product_Image = (ImageView) findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        //Get food id from intent
        if (getIntent() != null)
            productId = getIntent().getStringExtra("ProductId");
        if (!productId.isEmpty())    {
            getDetailFood(productId);
        }

    }

    private void getDetailFood(String productId) {
        products.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProduct =dataSnapshot.getValue(Product.class);

                //set Image
                Picasso.with(getBaseContext()).load(currentProduct.getImage())
                        .into(product_Image);

                collapsingToolbarLayout.setTitle(currentProduct.getName());

                product_Price.setText(currentProduct.getPrice());

                product_Name.setText(currentProduct.getName());

                product_Description.setText(currentProduct.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
