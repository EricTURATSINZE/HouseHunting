package com.example.househunting.authActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.househunting.MainActivity;
import com.example.househunting.R;
import com.example.househunting.model.auth.OTPResponse;
import com.example.househunting.network.AuthApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.Storage;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailActivity extends AppCompatActivity {
    PinView pinView;
    TextView resendCode;
    Storage storage;
    ProgressDialog progressDialog;
    Button veriyCode_Btn;
    private String otp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        storage = new Storage(this);

        veriyCode_Btn = (Button) findViewById(R.id.verify_code_btn);
        veriyCode_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                veriyEmail(otp);
            }
        });
        progressDialog = new ProgressDialog(this);
        resendCode = (TextView) findViewById(R.id.resendCode_tv);
        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendCode();
            }
        });
        pinView = (PinView) findViewById(R.id.otp_code);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() == 4) {
                    otp = charSequence.toString();
                    veriyEmail(otp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void veriyEmail(String otp) {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        RetrofitClient.getClient("").create(AuthApiService.class)
                .verifyEmail(otp.toString(), storage.getToken())
                .enqueue(new Callback<OTPResponse>() {
                    @Override
                    public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                        progressDialog.dismiss();
                        if (response.code()== 200) {
                            try {
                                storage.setLogin(true);
                                Intent i = new Intent(VerifyEmailActivity.this, MainActivity.class);
                                startActivity(i);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                String json = response.errorBody().string();
                                JSONObject jObjError = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
                                Toast.makeText(VerifyEmailActivity.this, jObjError.getJSONObject("message").getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(VerifyEmailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<OTPResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resendCode() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RetrofitClient.getClient("").create(AuthApiService.class)
                .resendOtp(storage.getToken())
                .enqueue(new Callback<OTPResponse>() {
                    @Override
                    public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                        progressDialog.dismiss();
                        if (response.code()== 200) {
                            try {
                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getApplicationContext(), "Error " + response.code(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<OTPResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
