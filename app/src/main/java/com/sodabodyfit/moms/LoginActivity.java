package com.sodabodyfit.moms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sodabodyfit.moms.Common.Constants;
import com.sodabodyfit.moms.Common.User;
import com.sodabodyfit.moms.Interface.Login;
import com.sodabodyfit.moms.Common.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity{

    //    private ObjectAnimator m_AnimationTitle;
    private String email;
    private String password;
    private EditText etEmail;
    private EditText etPassword;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void init()
    {
        Button btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    login();
                }
            }
        });

        TextView tvRecoverPassword = (TextView)findViewById(R.id.txt_recover_password);
        tvRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RecoveryPasswordActivity.class);
                startActivity(intent);
//                LoginActivity.this.finish();
            }
        });

        TextView tvSignup = (TextView)findViewById(R.id.txt_signup);
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));

        etEmail = (EditText)findViewById(R.id.edt_email);
        etPassword = (EditText)findViewById(R.id.edt_password);

    }

    private boolean validate(){

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if (email.isEmpty()){
            etEmail.setError(getString(R.string.error_null_email));
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()){
            etPassword.setError(getString(R.string.error_null_password));
            etPassword.requestFocus();
            return false;
        }

        hideKeyboard();
        return true;
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void login(){

        progress.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Login service = retrofit.create(Login.class);

        Call<UserInfo> call = service.sendLoginRequest("marsxcv3@outlook.com", "mars12345");
        call.enqueue(new Callback<UserInfo>() {

            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.isSuccessful()) {
                    progress.dismiss();
                    UserInfo obj = response.body();
                    User.token = obj.token;
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                progress.dismiss();
                String strError = t.getMessage();
                Toast.makeText(LoginActivity.this, strError, Toast.LENGTH_LONG).show();
            }
        });
    }
}
