package com.sodabodyfit.moms;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sodabodyfit.moms.Common.Constants;
import com.sodabodyfit.moms.Common.User;
import com.sodabodyfit.moms.Interface.ChangePassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordActivity extends AppCompatActivity{

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chage_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnSave = (Button)findViewById(R.id.btn_change);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    changePassword();
                }
            }
        });

        etOldPassword = (EditText)findViewById(R.id.edt_old_pwd);
        etNewPassword = (EditText)findViewById(R.id.edt_new_pwd);
        etConfirmPassword = (EditText)findViewById(R.id.edt_confirm_pwd);

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean validate(){

        oldPassword = etOldPassword.getText().toString();
        newPassword = etNewPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();

        if (oldPassword.isEmpty()){
            etOldPassword.setError(getString(R.string.error_null_password));
            etOldPassword.requestFocus();
            return false;
        }

        if (newPassword.isEmpty()){
            etNewPassword.setError(getString(R.string.error_null_password));
            etNewPassword.requestFocus();
            return false;
        }


        if (confirmPassword.isEmpty()){
            etConfirmPassword.setError(getString(R.string.error_null_password));
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!newPassword.equals(confirmPassword)){
            etNewPassword.setError(getString(R.string.error_null_password));
            etNewPassword.requestFocus();
            return false;
        }

        hideKeyboard();
        return true;
    }

    private void changePassword(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ChangePassword service = retrofit.create(ChangePassword.class);

        Call<Object> call = service.sendChangePasswordRequest("Token token=" + User.token, oldPassword, newPassword);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Updated your password successfully.", Toast.LENGTH_SHORT).show();
                    ChangePasswordActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

                Toast.makeText(ChangePasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            ChangePasswordActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
