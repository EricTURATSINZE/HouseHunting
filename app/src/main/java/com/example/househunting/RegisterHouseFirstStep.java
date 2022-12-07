package com.example.househunting;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Author: NGIRIMANA Schadrack
 */

public class RegisterHouseFirstStep extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener
{
    Button nextButton;
    EditText houseAddress;
    EditText pricePerMonth;
    EditText numberOfBedrooms;
    EditText numberOfBathroom;
    ImageView mainImage;
    String houseLocation;
    String price;
    String bedroom;
    String bathroom;
    String mainImageUrl;
    private static int IMAGE_REQ=1;
    private static final int REQUEST_LOCATION =1 ;
    private Uri imagePath;
    private static final int IMAGE_PICK_CAMERA_CODE=102;
    Spinner locationSpinner;
    ArrayList<String> locationChoices = new ArrayList<>();
    Location houseCoordinates;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_house_first_step);
        houseAddress = findViewById(R.id.house_address);
        pricePerMonth = findViewById(R.id.price_per_month);
        numberOfBathroom = findViewById(R.id.house_bathroom);
        numberOfBedrooms = findViewById(R.id.house_bedroom);
        mainImage= findViewById(R.id.house_main_image);
        nextButton = findViewById(R.id.house_next_btn);
        locationSpinner = (Spinner) findViewById(R.id.spinner_location);

        locationChoices.add("Select mode");
        locationChoices.add("Current location");
        locationChoices.add("From google map");

        /**
         * setting the location choice mode
         */
        locationSpinner .setOnItemSelectedListener(this);
        ArrayAdapter choiceAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, locationChoices);
        choiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(choiceAdapter );

        /**
         * Picking image from gallery or camera
         */
        mainImage.setOnClickListener(v->
        {
            imagePickDialog();
            System.out.println("========================================="+houseCoordinates.getLongitude());

        });



        initConfig();
        /**
         * Setting sending data to the next screen and upload main image to cloudinary
         */
        nextButton.setOnClickListener(v ->
        {
            Log.d(TAG, ": "+" button clicked");

            MediaManager.get().upload(imagePath).callback(new UploadCallback()
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
                public void onSuccess(String requestId, Map resultData) {
                    Log.d(TAG, "=================================: "+"success");
                    mainImageUrl= (String) resultData.get("url");
                    houseLocation = String.valueOf(houseAddress.getText());
                    bedroom= String.valueOf(numberOfBedrooms.getText());
                    bathroom = String.valueOf(numberOfBathroom.getText());
                    price = String.valueOf(pricePerMonth.getText());


                    if(houseLocation.length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"House address is empty",Toast.LENGTH_SHORT).show();
                    }
                    else if(bedroom.length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"House bedroom is empty",Toast.LENGTH_SHORT).show();
                    }
                    else if(bathroom.length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"House bathroom is empty",Toast.LENGTH_SHORT).show();
                    }
                    else if(price.length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"House price is empty",Toast.LENGTH_SHORT).show();
                    }
                    else if(mainImageUrl.length()==0)
                    {
                        Toast.makeText(getApplicationContext(),"House main image is empty",Toast.LENGTH_SHORT).show();
                    }
                    else if (houseCoordinates == null) {
                        Toast.makeText(getApplicationContext(),"The location is not available, try other way!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        Intent intent = new Intent(RegisterHouseFirstStep.this, RegisterHouseSecondStep.class);
                        intent.putExtra("address",houseLocation);
                        intent.putExtra("price",price);
                        intent.putExtra("bedroom",bedroom);
                        intent.putExtra("bathroom",bathroom);
                        intent.putExtra("main_image_url",mainImageUrl);
                        intent.putExtra("latitude",houseCoordinates.getLatitude());
                        intent.putExtra("longitude",houseCoordinates.getLongitude());

                        startActivity(intent);
                    }

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
        });
    }

    /**
     * Initializing cloudinary configuration
     */

    private void initConfig()
    {
        Map config = new HashMap();
        config.put("cloud_name", "kuranga");
        config.put("api_key","622489496465415");
        config.put("api_secret","dAogkM6LaPF9S_m9oHKnHquzITA");
        config.put("secure", true);
        MediaManager.init(this, config);
    }

    /**
     * Picking image dialog
     */
    private void imagePickDialog()
    {
        String[] options={"Camera","Gallery"};
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Select image");
        builder.setItems(options, (dialog, which) ->
        {
            if(which == 0)
            {
                    pickFromCamera();
            }
            else
            {
                    pickFromStorage();
            }
        });
        builder.create().show();
    }

    /**
     * picking image from gallery
     */

    public void pickFromStorage ()
    {
        if(ContextCompat.checkSelfPermission(RegisterHouseFirstStep.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            selectImage();
        }
        else
        {
            ActivityCompat.requestPermissions(RegisterHouseFirstStep.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},IMAGE_REQ);
        }

    }

    /**
     * picking image from camera
     */

    public void pickFromCamera ()
    {
        if(ContextCompat.checkSelfPermission(RegisterHouseFirstStep.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
        {
            selectFromCamera();
        }
        else
        {
            ActivityCompat.requestPermissions(RegisterHouseFirstStep.this,new String[]{Manifest.permission.CAMERA},IMAGE_REQ);
        }
    }

    private void selectImage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);

    }
    private void selectFromCamera()
    {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"images title");
        values.put(MediaStore.Images.Media.DESCRIPTION,"images description");

        imagePath=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Picasso.get().load(imagePath).into(mainImage);
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imagePath);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        imagePath=data.getData();
                        Picasso.get().load(imagePath).into(mainImage);
                    }
                }
            });

    /**
     * select how to get location
     */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String choice= locationChoices.get(position);
        if(choice.equals("Current location"))
        {
            houseCoordinates= getCurrentLocation();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    /**
     * getting current location
     */

    private Location getCurrentLocation()
    {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc=null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            loc = lm.getLastKnownLocation("gps");
        }
        return loc;
    }
}