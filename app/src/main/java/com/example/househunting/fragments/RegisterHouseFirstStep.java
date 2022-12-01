package com.example.househunting.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.househunting.R;


public class RegisterHouseFirstStep extends Fragment {
    Button nextButton;
    Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view =inflater.inflate(R.layout.fragment_register_house_first_step, container, false);
        nextButton = view.findViewById(R.id.house_next_btn);
        FragmentManager fm = this.getActivity().getSupportFragmentManager();
        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                fragment = fm.findFragmentById(R.id.second_fragment);
                FragmentTransaction ft = fm.beginTransaction();
                fragment = new RegisterHouseSecondStep();
                ft.replace(android.R.id.content, fragment, null);
                ft.commit();

            }
        });
        return view;
    }
}