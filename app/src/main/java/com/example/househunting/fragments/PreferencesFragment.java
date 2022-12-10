/* Author: Yves Ngabonziza */

package com.example.househunting.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.househunting.R;

public class PreferencesFragment extends Fragment {

    View view;
    TextView max;
    TextView min;
    TextView internet;
    TextView location;
    TextView numOfBedRooms;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.preferences_fragment, container, false);
        return view;
    }
}