package com.example.househunting.authActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.househunting.MainActivity;
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

public class LoginActivity extends AppCompatActivity {
    TextView email;
    TextView signup;
    EditText password;
    Button login_btn;
    ProgressDialog progressDialog;
    Storage storage;
    boolean isAllFieldsChecked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = findViewById(R.id.email_tv);
        password = (EditText) findViewById(R.id.password_et);
        login_btn = (Button) findViewById(R.id.login_btn);
        signup = (TextView) findViewById(R.id.signup_tv);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        storage = new Storage(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked) {
                    login(view);
                }
            }
        });
    }
    private boolean CheckAllFields() {
        /**
         * Author NGIRIMANA Schadrack
         */

        if (email.length() == 0) {
            email.setError(getText(R.string.email_empty_error));
            return false;
        }
        else if(!email.getText().toString().contains("@andrew.cmu.edu")) {
            email.setError(getText(R.string.email_type_error));
            return false;
        }

        if (password.length() == 0) {
            password.setError(getText(R.string.password_empty_error));
            return false;
        }
        return true;
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
                                storage.setToken(response.body().getUser().getToken());
                                storage.setLogin(true);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            } catch (Exception e) {
                                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            try {
                                String json = response.errorBody().string();
                                JSONObject jObjError = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
                                Toast.makeText(LoginActivity.this, jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
