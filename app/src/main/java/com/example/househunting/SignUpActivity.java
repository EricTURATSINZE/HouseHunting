package com.example.househunting;

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

import com.example.househunting.model.auth.SignupResponse;
import com.example.househunting.network.AuthApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.Storage;
import com.google.android.material.snackbar.Snackbar;

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
//    AwesomeValidation awesomeValidation;

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

        // Initilize validation style
//        awesomeValidation = new Aweso


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUp(view);
            }
        });
    }

    public void singUp(View view) {
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
                            Toast.makeText(SignUpActivity.this, response.code(), Toast.LENGTH_SHORT).show();
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

    public void checkAllFields() {
//        if
    }
}
