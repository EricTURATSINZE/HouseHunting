package com.example.househunting;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.househunting.model.house.Data;
import com.example.househunting.model.house.ViewHouseResponse;
import com.example.househunting.network.ApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.Storage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseDetailActivity extends AppCompatActivity {
    private String houseId;
    private ImageView coverImage;
    private TextView bedRooms;
    private TextView internet;
    private TextView address;
    private TextView description;
    private Button map;
    private Button bookNow;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_activity);
        Bundle bundle = new Bundle();
//        houseId = bundle.getString("houseId");
        houseId = "636e75d29bc3244f53ae4024";

        Storage storage = new Storage(this);
        token = storage.getToken();

        // TO DO:
//        final ProgressDialog progressDialog = new ProgressDialog(this);

    }

    private void fetchData(String id){

        RetrofitClient.getClient("").create(ApiService.class)
                .getHouse("", houseId, "")
                .enqueue(new Callback<ViewHouseResponse>() {
                    @Override
                    public void onResponse(Call<ViewHouseResponse> call, Response<ViewHouseResponse> response) {
                        if(response.code() == 200){
                            Data data = response.body().getData();
                            RequestOptions option = new RequestOptions().override(500, 500).optionalCenterCrop().placeholder(R.drawable.card_back).error(R.drawable.card_back);
                            Glide.with(HouseDetailActivity.this).load(data.getImageCover()).apply(option).into(coverImage);

                            bedRooms.setText(String.valueOf(data.getBedRooms()));
                            internet.setText(String.join("-", data.getInternet()));
                            address.setText(data.getLocation().getAddress());
                            description.setText(data.getDescription());

                            LinearLayout gallery = (LinearLayout) findViewById(R.id.gallery);

                            for(String image: data.getImages()){
                                CardView card = new CardView(HouseDetailActivity.this);
                                ActionBar.LayoutParams lyt = new ActionBar.LayoutParams(
                                        ActionBar.LayoutParams.MATCH_PARENT,
                                        ActionBar.LayoutParams.MATCH_PARENT
                                );


                                card.setLayoutParams(lyt);
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) card.getLayoutParams();
                                params.weight = 1f;
                                card.setLayoutParams(params);
                                ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
                                margin.setMargins(5, 5,5,5);
                                card.setRadius(20);

                                ImageView imageHolder = new ImageView(HouseDetailActivity.this);
                                ActionBar.LayoutParams imgLyt = new ActionBar.LayoutParams(
                                        ActionBar.LayoutParams.WRAP_CONTENT,
                                        ActionBar.LayoutParams.WRAP_CONTENT
                                );
                                imageHolder.setLayoutParams(imgLyt);
                                LinearLayout.LayoutParams imgParams = (LinearLayout.LayoutParams) imageHolder.getLayoutParams();
                                params.weight = 1f;
                                card.setLayoutParams(imgParams);
                                imageHolder.setScaleType(ImageView.ScaleType.CENTER_CROP);

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
