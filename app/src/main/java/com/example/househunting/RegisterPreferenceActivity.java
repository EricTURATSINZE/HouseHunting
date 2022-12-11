package com.example.househunting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.househunting.constants.Internet;
import com.example.househunting.model.preference.Preference;
import com.example.househunting.model.preference.PriceRange;
import com.example.househunting.model.preference.UserPreferenceResponse;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.network.UserPreferenceService;
import com.example.househunting.utils.Storage;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: FABRICE IRANKUNDA
 */

public class RegisterPreferenceActivity extends AppCompatActivity {
    private TextView max;
    private TextView min;
    private CheckBox mtn;
    private CheckBox canal;
    private CheckBox liquid;
    private CheckBox none;
    private TextView location;
    private TextView numOfBedRooms;
    private TextView submitPreference_btn_txt;
    private ProgressBar progressBar;
    private RelativeLayout submitPreference_btn;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_preferences);
        max = findViewById(R.id.maxPrice);
        min = findViewById(R.id.minPrice);
        mtn = findViewById(R.id.mtn);
        canal = findViewById(R.id.canal);
        liquid = findViewById(R.id.liquid);
        none = findViewById(R.id.none);
        location = findViewById(R.id.location);
        numOfBedRooms = findViewById(R.id.numBedRooms);

        submitPreference_btn = findViewById(R.id.submitPreference_btn);
        submitPreference_btn_txt =  findViewById(R.id.submitPreference_btn_txt);
        progressBar = findViewById(R.id.submitPreference_btn_pb);

        submitPreference_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPreference_btn_txt.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                createPreference(view);
            }
        });
    }


    private void createPreference(View view) {
        PriceRange priceRange = new PriceRange(Integer.parseInt(max.getText().toString()), Integer.parseInt(min.getText().toString()));
        ArrayList<String> internet = new ArrayList<>();
        if(mtn.isChecked())
            internet.add(Internet.MTN.getValue());
        if(canal.isChecked())
            internet.add(Internet.CANAL.getValue());
        if(liquid.isChecked())
            internet.add(Internet.LIQUID.getValue());

        Preference preference = new Preference.PreferenceBuilder()
                .setPriceRange(priceRange)
                .setLocation(location.getText().toString())
                .setInternet(internet)
                .setNumOfBedRooms(Integer.parseInt(numOfBedRooms.getText().toString()))
                .create();


        Storage storage = new Storage(this);
        RetrofitClient.getClient("").create(UserPreferenceService.class)
                .createPreference(preference, storage.getToken())
                .enqueue(new Callback<UserPreferenceResponse>() {
                    @Override
                    public void onResponse(Call<UserPreferenceResponse> call, Response<UserPreferenceResponse> response) {
                        submitPreference_btn_txt.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        if (response.code()== 201) {
                            try {
                                    RegisterPreferenceActivity.this.finish();
                            } catch (Exception e) {
                                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegisterPreferenceActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserPreferenceResponse> call, Throwable t) {
                        submitPreference_btn.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterPreferenceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
