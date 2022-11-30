package com.example.househunting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.househunting.model.OTPResponse;
import com.example.househunting.model.SignupResponse;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        storage = new Storage(this);

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
                    Toast.makeText(getApplicationContext(), "working " + charSequence, Toast.LENGTH_SHORT).show();
                    veriyEmail(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void veriyEmail(String otp) {
        progressDialog.show();
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
        RetrofitClient.getClient("").create(AuthApiService.class)
                .resendOtp(storage.getToken())
                .enqueue(new Callback<OTPResponse>() {
                    @Override
                    public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                        progressDialog.dismiss();
                        if (response.code()== 200) {
                            try {
                                System.out.println("Tokennnnnnnnnnnnnnnnnnnnnnnn " + response.body().getMessage());
                                Toast.makeText(getApplicationContext(), "Code successfully sent to your email!", Toast.LENGTH_SHORT);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                System.out.println("Elseeeeeeeeeeeeeeeee " + response + " oo " + jObjError.getJSONObject("error").getString("message"));
                                Toast.makeText(getApplicationContext(), jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
//                            System.out.println("Elseeeeeeeeeeeeeeeee " + response + " oo " + jObjError.getJSONObject("error").getString("message"));
                        }
                    }
                    @Override
                    public void onFailure(Call<OTPResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        System.out.println("ttttttttttttttttttttttttttttttttttttttttttttttt " + t);
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
