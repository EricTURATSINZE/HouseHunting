package com.example.househunting;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.househunting.model.HouseRegister.House;
import com.example.househunting.model.house.CreateHouseBody;
import com.example.househunting.model.house.CreateHouseResponse;
import com.example.househunting.network.HouseApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: NGIRIMANA Schadrack
 */

public class RegisterHouseSecondStep extends AppCompatActivity
{
    Button submitButton;
    ImageView moreImages;
    EditText landLordNames;
    EditText landLordContact;
    Spinner houseInternet;
    Uri imagePath;
    EditText description;
    Storage storage;
    ArrayList<Uri> images = new ArrayList<>();
    ArrayList<String> moreImagesUrls =new ArrayList<>();
    private static final int IMAGE_PICK_GALLERY_CODE=103;
    ArrayList<String> internetChoices = new ArrayList<>();
    ProgressDialog progressDialog;
    House house;

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
        houseInternet = findViewById(R.id.house_internet);
        house = ((MyCustomApplication)getApplication()).getHouse();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");

        storage = new Storage(this);


        internetChoices.add(getApplicationContext().getString(R.string.canalbox_internet_choice));
        internetChoices.add(getApplicationContext().getString(R.string.liquid_internet_choice));
        internetChoices.add(getApplicationContext().getString(R.string.mtn_internet_choice));
        internetChoices.add(getApplicationContext().getString(R.string.airtel_internet_choice));
        internetChoices.add(getApplicationContext().getString(R.string.mango_internet_choice));
        internetChoices.add(getApplicationContext().getString(R.string.no_internet_choice));

        /**
         * setting the location choice mode
         */

        ArrayAdapter choiceAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, internetChoices);
        choiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        houseInternet.setAdapter(choiceAdapter );
        houseInternet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String choice= internetChoices.get(i);
                ArrayList<String> internet = new ArrayList<String>();
                internet.add(String.valueOf(choice));
                house.setInternet(internet);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes)
                    {
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData)
                    {

                        moreImagesUrls.add((String) resultData.get("url"));
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
            }
            String desc= String.valueOf(description.getText());
            house.getOwnerInfo().setNames(String.valueOf(landLordNames.getText()));
            house.getOwnerInfo().setPhone(String.valueOf(landLordContact.getText()));
            /* use images urls to get more images urls uploaded*/

            CreateHouseBody createHouseBody = new CreateHouseBody(house.getBedrooms(), house.getPriceMonthly(), house.getDescription(), house.getInternet(), moreImagesUrls, house.getImageCover(), house.getLocation(), house.getOwnerInfo());

            submitHouse(createHouseBody);

        });
    }


    /**
     * Selecting many images from gallery
     */
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

    public void submitHouse(CreateHouseBody body) {
        progressDialog.show();
        RetrofitClient.getClient("").create(HouseApiService.class)
                .uploadHouse(body, storage.getToken())
                .enqueue(new Callback<CreateHouseResponse>() {
                    @Override
                    public void onResponse(Call<CreateHouseResponse> call, Response<CreateHouseResponse> response) {
                        progressDialog.dismiss();
                        if (response.code()== 201) {
                            try {
                                Intent i = new Intent(RegisterHouseSecondStep.this, VerifyEmailActivity.class);
                                startActivity(i);
                            } catch (Exception e) {
                                Toast.makeText(RegisterHouseSecondStep.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                String json = response.errorBody().string();
                                JSONObject jObjError = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
                                Toast.makeText(RegisterHouseSecondStep.this, jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(RegisterHouseSecondStep.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateHouseResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterHouseSecondStep.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}