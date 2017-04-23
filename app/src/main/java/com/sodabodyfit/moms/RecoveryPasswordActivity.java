package com.sodabodyfit.moms;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sodabodyfit.moms.Common.Constants;
import com.sodabodyfit.moms.Interface.RecoveryPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecoveryPasswordActivity extends AppCompatActivity {

    private String email;
    private EditText etEmail;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);

        Button btnRecoveryPassword = (Button)findViewById(R.id.btn_recovery_password);
        btnRecoveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    recoveryPassword();
                }
            }
        });

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

        etEmail = (EditText)findViewById(R.id.edt_email);
    }

    private boolean validate(){

        email = etEmail.getText().toString();

        if (email.isEmpty()){
            etEmail.setError(getString(R.string.error_null_email));
            etEmail.requestFocus();
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

    private void recoveryPassword(){

        progress.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecoveryPassword service = retrofit.create(RecoveryPassword.class);

        Call<Object> call = service.sendRecoveryPasswordRequest(email);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    progress.dismiss();
                    showDialog();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                progress.dismiss();
                String strError = t.getMessage();
            }
        });
    }

    private void showDialog() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .content("Please check your email.")
                .positiveText("OK")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        RecoveryPasswordActivity.this.finish();
                    }
                }).build();

        mDialog.show();
    }
}
