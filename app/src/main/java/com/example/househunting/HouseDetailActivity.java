package com.example.househunting;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.househunting.model.house.Data;
import com.example.househunting.model.house.ViewHouseResponse;
import com.example.househunting.network.HouseApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.LoadImage;
import com.example.househunting.utils.Storage;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Author: FABRICE IRANKUNDA
 */

public class HouseDetailActivity extends AppCompatActivity {
    private String houseId;
    private ImageView coverImage;
    private TextView price;
    private TextView bedRooms;
    private TextView internet;
    private TextView address;
    private TextView description;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout houseContainer;
    private Button mapBtn;
    private Button bookNowBtn;
    private String token;
    ArrayList<Double> houseLocation = new ArrayList<>();
    String landlordContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_activity);
        houseContainer = findViewById(R.id.houseContainer);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();
        houseId = getIntent().getStringExtra("houseId");
        coverImage = findViewById(R.id.coverImage);
        price = findViewById(R.id.priceView);
        bedRooms = findViewById(R.id.numBedRooms);
        internet = findViewById(R.id.internet);
        address = findViewById(R.id.address);
        description = findViewById(R.id.txt_description);
        houseContainer.setVisibility(View.GONE);
        mapBtn =  findViewById(R.id.map_btn);
        bookNowBtn =  findViewById(R.id.book_btn);

        Storage storage = new Storage(this);
        token = storage.getToken();
        fetchData(houseId);

        mapBtn.setOnClickListener(v-> {
            /**
             * Author NGIRIMANA Schadrack
             */
            Intent intent = new Intent(HouseDetailActivity.this, HouseMapLocation.class);
            intent.putExtra("longitude",houseLocation.get(0));
            intent.putExtra("latitude",houseLocation.get(1));
            startActivity(intent);

        });

        bookNowBtn.setOnClickListener(v-> {

            /**
             * Author NGIRIMANA Schadrack
             */
            try
            {
                Intent intent = new Intent ( Intent.ACTION_VIEW );
                intent.setData ( Uri.parse ( "https://wa.me/" +landlordContact + "/?text=" + "") );
                startActivity ( intent );
            } catch (Exception e)
            {
                Toast.makeText(HouseDetailActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void fetchData(String id){
        RetrofitClient.getClient("").create(HouseApiService.class)
                .getHouse(houseId, token)
                .enqueue(new Callback<ViewHouseResponse>() {
                    @Override
                    public void onResponse(Call<ViewHouseResponse> call, Response<ViewHouseResponse> response) {
                        if(response.code() == 200){
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            houseContainer.setVisibility(View.VISIBLE);
                            Data data = response.body().getData();
                            LoadImage.loadImage(HouseDetailActivity.this, data.getImageCover(), coverImage, R.drawable.card_back);

                            price.setText(String.valueOf(data.getPriceMonthly()) + getString(R.string.currency ) + getString(R.string.month));
                            bedRooms.setText(String.valueOf(data.getBedRooms()) + getString(R.string.rooms));
                            internet.setText(String.join("-", data.getInternet()));
                            address.setText(data.getLocation().getAddress());
                            description.setText(data.getDescription());
                            houseLocation=  data.getLocation().getCoordinates();
                            landlordContact = data.getOwnerInfo().getPhone();
                            GridLayout gallery = findViewById(R.id.gallery);
                            for(String image: data.getImages()){
                                CardView card = new CardView(HouseDetailActivity.this);
                                LinearLayout.LayoutParams lyt = new LinearLayout.LayoutParams(
                                        200,
                                        200,
                                        1.0f
                                );

                                card.setLayoutParams(lyt);
                                ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
                                margin.setMargins(5, 5,5,5);
                                card.setRadius(20);

                                ImageView imageHolder = new ImageView(HouseDetailActivity.this);
                                LinearLayout.LayoutParams imgLyt = new LinearLayout.LayoutParams(
                                        ActionBar.LayoutParams.MATCH_PARENT,
                                        ActionBar.LayoutParams.MATCH_PARENT,
                                        1.0f
                                );
                                imageHolder.setLayoutParams(imgLyt);
                                imageHolder.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                LoadImage.loadImage(HouseDetailActivity.this, image, imageHolder, R.drawable.card_back);

                                card.addView(imageHolder);
                                gallery.addView(card);

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ViewHouseResponse> call, Throwable t) {
                        Toast.makeText(HouseDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
