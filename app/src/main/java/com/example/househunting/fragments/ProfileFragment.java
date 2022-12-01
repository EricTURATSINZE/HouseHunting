package com.example.househunting.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.househunting.R;
import com.example.househunting.model.auth.Profile;
import com.example.househunting.model.auth.ProfileResponse;
import com.example.househunting.network.AuthApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.LoadImage;
import com.example.househunting.utils.Storage;
import com.facebook.shimmer.ShimmerFrameLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private TextView greetings;
    private ImageView profile;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView phone;
    private TextView manageHousebtn;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout profileContainer;
    private RelativeLayout info;

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.profile_fragment, container, false);
        shimmerFrameLayout = view.findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();
        greetings = view.findViewById(R.id.greeting);
        profile = view.findViewById(R.id.profile_pic);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email_tv);
        phone = view.findViewById(R.id.phone_tv);
        manageHousebtn = view.findViewById(R.id.manage_btn);
        profileContainer = view.findViewById(R.id.profile_container);
        info = view.findViewById(R.id.info);
        info.setVisibility(View.GONE);
        profile.setVisibility(View.GONE);
        profileContainer.setVisibility(View.GONE);

        manageHousebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TO DO
            }
        });

        fetchData();
        return view;
    }

    private void fetchData(){
        Storage storage = new Storage(getContext());
        String token = storage.getToken();
        RetrofitClient.getClient("").create(AuthApiService.class)
                .getMe(token)
                .enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                        if(response.code() == 200){
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            profileContainer.setVisibility(View.VISIBLE);
                            profile.setVisibility(View.VISIBLE);
                            info.setVisibility(View.VISIBLE);

                            Profile data = response.body().getData();
                            String fname = data.getNames().split(" ")[0];
                            String lname = data.getNames().split(" ")[1];
                            LoadImage.loadImage(getContext(), data.getProfile(), profile, R.drawable.ic_profile);
                            greetings.setText(getString(R.string.greeting) + " " + fname);
                            firstName.setText(fname);
                            lastName.setText(lname);
                            phone.setText(data.getPhone());
                            email.setText(data.getEmail());

                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {

                    }
                });
    }
}