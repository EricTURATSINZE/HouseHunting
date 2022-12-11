package com.example.househunting.houseActivities;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.househunting.utils.MyCustomApplication;
import com.example.househunting.R;
import com.example.househunting.model.HouseRegister.House;
import com.example.househunting.services.LocationService;
import com.example.househunting.utils.ManageLanguage;
import com.example.househunting.utils.Storage;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Map;


/**
 * Author: NGIRIMANA Schadrack
 */

public class RegisterHouseFirstStep extends AppCompatActivity
{
    private RelativeLayout next_button;
    private TextView next;
    private EditText houseAddress_et;
    private EditText pricePerMonth;
    private EditText numberOfBedrooms;
    private EditText numberOfBathroom;
    private ImageView mainImage;
    private String houseAddress;
    private String price;
    private String bedroom;
    private String bathroom;
    private String mainImageUrl;
    private static int IMAGE_REQ=1;
    private static final int REQUEST_LOCATION =1 ;
    private Uri imagePath;
    private static final int IMAGE_PICK_CAMERA_CODE=102;
    private Spinner locationSpinner;
    private ArrayList<String> locationChoices = new ArrayList<>();
    private Location houseCoordinates;
    private MyCustomApplication application;
    private ProgressBar progressBar;
    private House house;
    private Storage storage;
    private Spinner spinner;

    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_house_first_step);
        application = new MyCustomApplication();
        houseAddress_et = findViewById(R.id.house_address);
        pricePerMonth = findViewById(R.id.price_per_month);
        numberOfBathroom = findViewById(R.id.house_bathroom);
        numberOfBedrooms = findViewById(R.id.house_bedroom);
        mainImage= findViewById(R.id.house_main_image);
        next_button = findViewById(R.id.loading_button);
        next = findViewById(R.id.house_next_btn);
        progressBar = (ProgressBar) findViewById(R.id.next_btn_pb);
        locationSpinner = (Spinner) findViewById(R.id.spinner_location);
        spinner = findViewById(R.id.spinner);
        storage = new Storage(RegisterHouseFirstStep.this);
        String lan = storage.getLanguage();
        ManageLanguage manageLanguage = new ManageLanguage();
        manageLanguage.setLocal(this, lan);

        /**
         * Populating fields with their prior values, if there are, once you are navigating back to this screen
         */
        house = ((MyCustomApplication)getApplication()).getHouse();
        if(house != null) {
            numberOfBathroom.setText(String.valueOf(house.getBathrooms()));
            numberOfBedrooms.setText(String.valueOf(house.getBedrooms()));
            houseAddress_et.setText(house.getLocation().getAddress());
            pricePerMonth.setText(String.valueOf(house.getPriceMonthly()));
        } else {
            House h = new House();
            ((MyCustomApplication)getApplication()).setHouse(new House());
            house = ((MyCustomApplication)getApplication()).getHouse();
        }


        locationChoices.add(getApplicationContext().getString(R.string.location_choice));
        locationChoices.add(getApplicationContext().getString(R.string.current_location_choice));
        locationChoices.add(getApplicationContext().getString(R.string.map_location_choice));

        /**
         * setting the location choice mode
         */

        ArrayAdapter choiceAdapter = new ArrayAdapter(this, R.layout.spinner_item, locationChoices);
        choiceAdapter.setDropDownViewResource(R.layout.spinner_item);
        locationSpinner.setAdapter(choiceAdapter );
        locationSpinner .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationChoiceSelected(locationChoices.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /**
         * Picking image from gallery or camera
         */
        mainImage.setOnClickListener(v->
        {
            imagePickDialog();

        });

        final String[] languages = {getString(R.string.select_language), "English", "Swahili", "Français"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,languages);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang = parent.getItemAtPosition(position).toString();
                ManageLanguage manageLanguage = new ManageLanguage();
                if(selectedLang.equals("English")){
                    manageLanguage.setLocal(RegisterHouseFirstStep.this, "en");
                    storage.setLanguage("English");
                    finish();
                    startActivity(getIntent());

                } else if(selectedLang.equals("Swahili")){
                    storage.setLanguage("Swahili");
                    manageLanguage.setLocal(RegisterHouseFirstStep.this, "sw");
                    finish();
                    startActivity(getIntent());
                } else if(selectedLang.equals("Français")){
                    storage.setLanguage("Français");
                    manageLanguage.setLocal(RegisterHouseFirstStep.this, "fr");
                    finish();
                    startActivity(getIntent());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /**
         * Setting sending data to the next screen and upload main image to cloudinary
         */
        next_button.setOnClickListener(v ->
        {
            isAllFieldsChecked= checkAllFields();
            if(isAllFieldsChecked)
            {
                if(imagePath != null) {
                    next.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    MediaManager.get().upload(imagePath).callback(new UploadCallback()
                    {
                        @Override
                        public void onStart(String requestId)
                        {
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes)
                        {
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            mainImageUrl= (String) resultData.get("url");
                            houseAddress = String.valueOf(houseAddress_et.getText());
                            bedroom= String.valueOf(numberOfBedrooms.getText());
                            bathroom = String.valueOf(numberOfBathroom.getText());
                            price = String.valueOf(pricePerMonth.getText());

                            Intent intent = new Intent(RegisterHouseFirstStep.this, RegisterHouseSecondStep.class);
                            house.getLocation().setAddress(houseAddress);
                            house.setBathrooms(Integer.parseInt(bathroom));
                            house.setBedrooms(Integer.parseInt(bedroom));
                            house.setPriceMonthly(Integer.parseInt(price));
                            house.setImageCover(mainImageUrl);
                            next.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            startActivity(intent);


                        }
                        @Override
                        public void onError(String requestId, ErrorInfo error)
                        {
                        }
                        @Override
                        public void onReschedule(String requestId, ErrorInfo error)
                        {
                        }
                    }).dispatch();
                } else {
                    Toast.makeText(getApplicationContext(),R.string.house_main_image_error,Toast.LENGTH_SHORT).show();
                }

            }


        });
    }

    private boolean checkAllFields() {
        if (houseAddress_et.length() == 0) {
            houseAddress_et.setError(getText(R.string.house_address_error));
            return false;
        }
        if(houseCoordinates == null && house.getLocation().getCoordinates()== null)
        {
            Toast.makeText(getApplicationContext(),R.string.house_coordinates_error,Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pricePerMonth.length() == 0 ||  Integer.valueOf(pricePerMonth.getText().toString())<=0) {
            pricePerMonth.setError(getText(R.string.house_price_error));
            return false;
        }
        if (numberOfBedrooms.length() == 0 ||  Integer.valueOf(numberOfBedrooms.getText().toString())<=0) {
            numberOfBedrooms.setError(getText(R.string.house_bedroom_error));
            return false;
        }
        if (numberOfBathroom.length() == 0 ||  Integer.valueOf(numberOfBathroom.getText().toString())<=0) {
            numberOfBathroom.setError(getText(R.string.house_bathroom_error));
            return false;
        }
        return true;
    }
    /**
     * Picking image dialog
     */
    private void imagePickDialog()
    {
        String[] options={"Camera","Gallery"};
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_how_to_take_image);
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


    public void locationChoiceSelected(String choice)
    {
        if(choice.equals(getApplicationContext().getString(R.string.current_location_choice)))
        {
            houseCoordinates= LocationService.getCurrentLocation(this, this, REQUEST_LOCATION);
            house.getLocation().setCoordinates(new double[]{houseCoordinates.getLongitude(), houseCoordinates.getLatitude()});
        } else if (choice.equals(getApplicationContext().getString(R.string.map_location_choice)))
        {
            house.getLocation().setAddress(String.valueOf(houseAddress_et.getText()));
            if(!String.valueOf(numberOfBathroom.getText()).equals("")) house.setBathrooms(Integer.parseInt(String.valueOf(numberOfBathroom.getText())));
            if(!String.valueOf(numberOfBedrooms.getText()).equals("")) house.setBedrooms(Integer.parseInt(String.valueOf(numberOfBedrooms.getText())));
            if(!String.valueOf(pricePerMonth.getText()).equals("")) house.setPriceMonthly(Integer.parseInt(String.valueOf(pricePerMonth.getText())));
            house.setImageCover(mainImageUrl);
            startActivity(new Intent(RegisterHouseFirstStep.this, MapActivity.class));
        }
    }

}