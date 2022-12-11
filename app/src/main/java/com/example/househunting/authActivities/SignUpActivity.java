package com.example.househunting.authActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.househunting.R;
import com.example.househunting.model.auth.SignupResponse;
import com.example.househunting.network.AuthApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.Storage;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    TextView names;
    TextView email;
    TextView phone;
    TextView login_btn;
    EditText password;
    RelativeLayout signUp;
    TextView signup_btn_txt;
    ProgressBar progressBar;
    Storage storage;
    boolean isAllFieldsChecked = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        storage = new Storage(this);

        signUp = (RelativeLayout) findViewById(R.id.signup_btn);
        names = (TextView) findViewById(R.id.names_tv);
        email = (TextView) findViewById(R.id.email_tv);
        phone = (TextView) findViewById(R.id.phone_tv);
        password = (EditText) findViewById(R.id.password);
        signup_btn_txt = (TextView) findViewById(R.id.signup_btn_txt);
        progressBar = (ProgressBar) findViewById(R.id.signup_btn_pb);
        login_btn = (TextView) findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked) {
                    signup_btn_txt.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    signUp(view);
                }
            }
        });
    }


    private boolean CheckAllFields() {
        /**
         * Author NGIRIMANA Schadrack
         */
        if (names.length() == 0) {
            names.setError(getText(R.string.name_error));
            return false;
        }

        if (email.length() == 0) {
            email.setError(getText(R.string.email_empty_error));
            return false;
        }
        else if(!email.getText().toString().contains("@andrew.cmu.edu")) {
            email.setError(getText(R.string.email_type_error));
            return false;
        }
        if (phone.length() == 0) {
            phone.setError(getText(R.string.phone_empty_error));
            return false;
        }
        else if (!phone.getText().toString().substring(0,1).equals("+")){
            phone.setError(getText(R.string.phone_country_code_error));
            return false;
        }
        if (password.length() == 0) {
            password.setError(getText(R.string.password_empty_error));
            return false;
        } else if (password.length() < 6) {
            password.setError(getText(R.string.password_length_error));
            return false;
        }
        return true;
    }


    public void signUp(View view) {
        signup_btn_txt.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getClient("").create(AuthApiService.class)
                .signup("" + names.getText(), "" + email.getText(), "" + password.getText(), "" + phone.getText())
                .enqueue(new Callback<SignupResponse>() {
                    @Override
                    public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                        signup_btn_txt.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        if (response.code()== 201) {
                            try {
                                storage.setToken(response.body().getUser().getToken());
                                Intent i = new Intent(SignUpActivity.this, VerifyEmailActivity.class);
                                startActivity(i);
                            } catch (Exception e) {
                                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            try {
                                String json = response.errorBody().string();
                                JSONObject jObjError = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
                                Toast.makeText(SignUpActivity.this, jObjError.getJSONObject("message").getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SignupResponse> call, Throwable t) {
                        signup_btn_txt.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
