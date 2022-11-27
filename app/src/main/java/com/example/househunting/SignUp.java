package com.example.househunting;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.househunting.model.SignupResponse;
import com.example.househunting.network.ApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.Storage;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUp extends AppCompatActivity {
    TextView names;
    TextView email;
    TextView phone;
    TextView login_btn;
    EditText password;
    RelativeLayout signUp;
    TextView signup_btn_txt;
    ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        final Storage storage = new Storage(this);
        final ProgressDialog progressDialog = new ProgressDialog(this);

        signUp = (RelativeLayout) findViewById(R.id.signup_btn);
        names = (TextView) findViewById(R.id.names_tv);
        email = (TextView) findViewById(R.id.email_tv);
        phone = (TextView) findViewById(R.id.phone_tv);
        password = (EditText) findViewById(R.id.password);
        signup_btn_txt = (TextView) findViewById(R.id.signup_btn_txt);
        progressBar = (ProgressBar) findViewById(R.id.signup_btn_pb);
        login_btn = (TextView) findViewById(R.id.login_btn);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_btn_txt.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                RetrofitClient.getClient("https://house-hunting.onrender.com/api/v1/").create(ApiService.class)
                        .signup(""+names.getText(), ""+email.getText(), ""+password.getText(), ""+phone.getText())
                        .enqueue(new Callback<SignupResponse>() {
                            @Override
                            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                                signup_btn_txt.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                System.out.println("Erorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + response.code());
                                if (response.code()== 201) {
                                    try {
                                        storage.setToken(response.body().getUser().getToken());
                                        storage.setLogin(true);
                                        Intent i = new Intent(SignUp.this, VerifyCode.class);
                                        startActivity(i);
                                    } catch (Exception e) {
                                        Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<SignupResponse> call, Throwable t) {
                                signup_btn_txt.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                System.out.println("Erorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + t.getMessage());
                                Toast.makeText(SignUp.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}
