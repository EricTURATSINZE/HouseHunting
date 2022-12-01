package com.example.househunting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.househunting.model.SignupResponse;
import com.example.househunting.network.AuthApiService;
import com.example.househunting.network.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView email;
    EditText password;
    Button login_btn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = findViewById(R.id.email_tv);
        password = (EditText) findViewById(R.id.password_et);
        login_btn = (Button) findViewById(R.id.login_btn);
//        startActivity(new Intent(Login.this, MainActivity.class));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });
    }

    private void login(View view) {
        progressDialog.show();
        RetrofitClient.getClient("").create(AuthApiService.class)
                .login("" + email.getText(), "" + password.getText())
                .enqueue(new Callback<SignupResponse>() {
                    @Override
                    public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                        progressDialog.dismiss();
                        if (response.code()== 200) {
                            try {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            } catch (Exception e) {
                                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SignupResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
