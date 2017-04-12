package com.sodabodyfit.moms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.sodabodyfit.moms.Common.Constants;
import com.sodabodyfit.moms.Interface.ChangePassword;
import com.sodabodyfit.moms.Interface.RecoveryPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PasswordActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_password);

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    private void init()
    {
        TextView btnDone = (TextView)findViewById(R.id.tv_done);
        btnDone.setOnClickListener(this);

        TextView btnSave = (TextView)findViewById(R.id.tv_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int nResId = v.getId();
        switch (nResId)
        {
            case R.id.tv_done:
                OnDone();
                break;
            case R.id.tv_save:
                OnSave();
                break;
        }
    }

    private void OnDone()
    {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    private void OnSave()
    {
        changePassword();

        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    private void changePassword(){

        EditText etOldPwd = (EditText)findViewById(R.id.et_old_password);
        EditText etNewPwd = (EditText)findViewById(R.id.et_new_password);
        String oldPwd = etOldPwd.getText().toString();
        String newPwd = etNewPwd.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ChangePassword service = retrofit.create(ChangePassword.class);

        Call<Object> call = service.sendChangePasswordRequest("Token token=MtGLTpFQz5z9bp1NnmQoxbM3", oldPwd, newPwd);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    String temp = "success";
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                String strError = t.getMessage();
            }
        });
    }
}
