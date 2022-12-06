package com.example.househunting;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import java.util.ArrayList;
import java.util.Map;

/**
 * Author: NGIRIMANA Schadrack
 */

public class RegisterHouseSecondStep extends AppCompatActivity
{
    Button submitButton;
    ImageView moreImages;
    EditText landLordNames;
    EditText landLordContact;
    Uri imagePath;
    EditText description;
    ArrayList<Uri> images = new ArrayList<>();
    ArrayList<String> imagesUrl =new ArrayList<>();
    private static final int IMAGE_PICK_GALLERY_CODE=103;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_house_second_step);
        moreImages = findViewById(R.id.house_more_images);
        landLordNames = findViewById(R.id.house_landlord_name);
        landLordContact = findViewById(R.id.house_contact);
        submitButton = findViewById(R.id.house_submit_btn);
        description = findViewById(R.id.house_description);

        moreImages.setOnClickListener(v->
        {
            selectImage();
        });

        submitButton.setOnClickListener(v->
        {
            for (Uri uri : images)
            {
                MediaManager.get().upload(uri).callback(new UploadCallback()
                {
                    @Override
                    public void onStart(String requestId)
                    {
                        Log.d(TAG, "=================================: "+"started");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes)
                    {
                        Log.d(TAG, "=================================: "+"uploading");
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData)
                    {
                        Log.d(TAG, "=================================: "+"success");
                        imagesUrl.add((String) resultData.get("url"));
                        System.out.println("========================================="+resultData.get("url"));
                    }
                    @Override
                    public void onError(String requestId, ErrorInfo error)
                    {
                        Log.d(TAG, "=================================: "+error);
                    }
                    @Override
                    public void onReschedule(String requestId, ErrorInfo error)
                    {
                        Log.d(TAG, "=================================: "+error);
                    }
                }).dispatch();
            }
            String address= getIntent().getStringExtra("address");
            String price= getIntent().getStringExtra("price");
            String bedroom= getIntent().getStringExtra("bedroom");
            String bathroom = getIntent().getStringExtra("bathroom");
            String mainImageUrl = getIntent().getStringExtra("main_image_url");
            String desc= String.valueOf(description.getText());
            String ownerNames= String.valueOf(landLordNames.getText());
            String ownerContact= String.valueOf(landLordContact.getText());

        });
    }

    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select images"), IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            if(data.getClipData() != null)
            {
                int count = data.getClipData().getItemCount();

                for(int i = 0; i < count; i++)
                {
                    imagePath = data.getClipData().getItemAt(i).getUri();
                    images.add(imagePath);
                }
            }
        }
    }
}