/* Author: Yves Ngabonziza */

package com.example.househunting.fragments;

import static androidx.core.content.ContextCompat.startForegroundService;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.househunting.R;
import com.example.househunting.RegisterPreferenceActivity;
import com.example.househunting.model.preference.Preference;
import com.example.househunting.model.preference.UserPreferenceResponse;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.network.UserPreferenceService;
import com.example.househunting.services.PullPreferredHousesService;
import com.example.househunting.utils.Storage;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesFragment extends Fragment {
    private TextView minPrefPrice;
    private TextView maxPrefPrice;
    private TextView prefLocation;
    private TextView prefInternets;
    private TextView numBeds;
    private TextView addPrefBtn;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout preferenceContainer;
    private RelativeLayout info;

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.preferences, container, false);
        shimmerFrameLayout = view.findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

        addPrefBtn = view.findViewById(R.id.manage_btn);
        minPrefPrice = view.findViewById(R.id.minPrefPrice);
        maxPrefPrice = view.findViewById(R.id.maxPrefPrice);
        prefLocation = view.findViewById(R.id.prefLocation);
        prefInternets = view.findViewById(R.id.prefInternets);
        numBeds = view.findViewById(R.id.prefNumBeds);
        preferenceContainer = view.findViewById(R.id.preference_container);
        preferenceContainer.setVisibility(View.GONE);
        info = view.findViewById(R.id.prefInfo);
        info.setVisibility(View.GONE);

        addPrefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RegisterPreferenceActivity.class);
//                intent.putExtra("houseId", data.get(position).get_id());
                startActivity(intent);
            }
        });

        fetchData();
        return view;
    }

    private void fetchData(){
        Storage storage = new Storage(getContext());
        String token = storage.getToken();
        RetrofitClient.getClient("").create(UserPreferenceService.class)
                .getPreferences(token)
                .enqueue(new Callback<UserPreferenceResponse>() {
                    @Override
                    public void onResponse(Call<UserPreferenceResponse> call, Response<UserPreferenceResponse> response) {
                        if(response.code() == 200){
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            preferenceContainer.setVisibility(View.VISIBLE);
                            info.setVisibility(View.VISIBLE);

                            Preference data = response.body().getPreference();
                            String internets = String.join(", ", data.getInternet()) ;
                            minPrefPrice.setText(String.valueOf(data.getPriceRange().getMin()));
                            maxPrefPrice.setText(String.valueOf(data.getPriceRange().getMax()));
                            prefLocation.setText(data.getLocation());
                            prefInternets.setText(internets);
                            numBeds.setText(String.valueOf(data.getNumOfBedRooms()));

                            // Notification Service: Note It will be taken from here!!!
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                if(!new PullPreferredHousesService().notificationServiceRunning(getContext())){
                                    Intent notificationService = new Intent(getContext(), PullPreferredHousesService.class);
                                    getContext().startForegroundService(notificationService);
                                }
                            }
                        } else {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            preferenceContainer.setVisibility(View.VISIBLE);
                            info.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserPreferenceResponse> call, Throwable t) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        preferenceContainer.setVisibility(View.VISIBLE);
                        info.setVisibility(View.VISIBLE);
                    }
                });
    }
}