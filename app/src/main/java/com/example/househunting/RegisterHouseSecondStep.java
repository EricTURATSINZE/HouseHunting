package com.example.househunting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Author: NGIRIMANA Schadrack
 */

public class RegisterHouseSecondStep extends AppCompatActivity
{
    Button submitButton;
    ImageView moreImages;
    EditText landLordNames;
    EditText landLordContact;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_house_second_step);
        moreImages = findViewById(R.id.house_more_images);
        landLordNames = findViewById(R.id.house_landlord_name);
        landLordContact = findViewById(R.id.house_contact);
        submitButton = findViewById(R.id.house_submit_btn);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}