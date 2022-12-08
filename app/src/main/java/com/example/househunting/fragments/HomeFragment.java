package com.example.househunting.fragments;

import static android.content.Context.LOCATION_SERVICE;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.househunting.R;
import com.example.househunting.adapter.HouseAdapter;
import com.example.househunting.model.house.CriteriaGlobal;
import com.example.househunting.model.house.CriteriaNearest;
import com.example.househunting.model.house.CriteriaRated;
import com.example.househunting.model.house.CriteriaVisible;
import com.example.househunting.model.house.CriteriaWifi;
import com.example.househunting.model.house.Data;
import com.example.househunting.model.house.ViewAllHouseResponse;
import com.example.househunting.network.ApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.CustomDistanceComparator;
import com.example.househunting.utils.CustomPriceComparator;
import com.example.househunting.utils.Storage;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements LocationListener {

    /**
     * Author: David
     * Start
     */
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    public static Location location;

    @Override
    public void onLocationChanged(@NonNull Location _location) {
        location = _location;
        Log.d("location", "location================" + _location.getAltitude());
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    private ShimmerFrameLayout shimmerFrameLayout;
    protected RecyclerView mRecyclerView;
    protected HouseAdapter houseAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected TextView textView;
    LocationManager locationManager;

    ArrayList<Data> houseList;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getLocation();
        view = inflater.inflate(R.layout.home_fragment, container, false);
        TextView wifi = view.findViewById(R.id.amenity1);
        TextView nearest = view.findViewById(R.id.amenity2);
        TextView rated = view.findViewById(R.id.amenity3);
        TextView empty_view = view.findViewById(R.id.empty_view);
        TextView search_button = view.findViewById(R.id.search_btn);
        TextView cheaper = view.findViewById(R.id.amenity4);

        shimmerFrameLayout = view.findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();
        fetchData();
        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = view.findViewById(R.id.house_recycleview);
        mRecyclerView.setVisibility(View.GONE);

        nearest.setOnClickListener(v -> {
            ArrayList<Data> houses = (ArrayList<Data>) new CriteriaNearest(location).meetCriteria(houseList);
            Collections.sort(houses, new CustomDistanceComparator());
            empty_view.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            houseAdapter = new HouseAdapter(houses, location);
            mRecyclerView.setAdapter(houseAdapter);
        });
        cheaper.setOnClickListener(v -> {
            Collections.sort(houseList, new CustomPriceComparator());
            empty_view.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            houseAdapter = new HouseAdapter(houseList, location);
            mRecyclerView.setAdapter(houseAdapter);

        });
        search_button.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ArrayList<Data> houses = (ArrayList<Data>) new CriteriaGlobal(s.toString()).meetCriteria(houseList);
                    if (houses != null) {
                        empty_view.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        houseAdapter = new HouseAdapter(houses, location);
                        mRecyclerView.setAdapter(houseAdapter);
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        rated.setOnClickListener(v -> {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ArrayList<Data> houses = (ArrayList<Data>) new CriteriaRated().meetCriteria(houseList);
                if (houses != null) {
                    empty_view.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    houseAdapter = new HouseAdapter(houses, location);
                    mRecyclerView.setAdapter(houseAdapter);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    empty_view.setVisibility(View.VISIBLE);
                }


            }
        });

        wifi.setOnClickListener(v -> {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ArrayList<Data> houses = (ArrayList<Data>) new CriteriaWifi().meetCriteria(houseList);
                if (houses != null) {
                    empty_view.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    houseAdapter = new HouseAdapter(houses, location);
                    mRecyclerView.setAdapter(houseAdapter);
                    Log.d("note", "Not Empty================" + houses.size());
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    empty_view.setVisibility(View.VISIBLE);
                    Log.d("note", "Empty=======================");
                }


            }

        });


        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        // Set CustomAdapter as the adapter for RecyclerView.
        // END_INCLUDE(initializeRecyclerView)

/** to be removed */

        return view;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        if (layoutManagerType == LayoutManagerType.GRID_LAYOUT_MANAGER) {
            mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    private void fetchData() {
        Storage storage = new Storage(getContext());
        String token = storage.getToken();
        RetrofitClient.getClient("").create(ApiService.class)
                .getAllHouse(token)
                .enqueue(new Callback<ViewAllHouseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ViewAllHouseResponse> call, @NonNull Response<ViewAllHouseResponse> response) {
                        if (response.code() == 200) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            assert response.body() != null;
                            houseList = (ArrayList<Data>) new CriteriaVisible().meetCriteria(response.body().getData());
                            houseAdapter = new HouseAdapter(houseList, location);
                            mRecyclerView.setAdapter(houseAdapter);

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ViewAllHouseResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

/** End of david edit */
    }
    @SuppressLint("MissingPermission")
    private void getLocation(){

        try{
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}

