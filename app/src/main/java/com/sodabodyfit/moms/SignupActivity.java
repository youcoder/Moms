package com.sodabodyfit.moms;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sodabodyfit.moms.Common.Constants;
import com.sodabodyfit.moms.Interface.SignUp;
import com.sodabodyfit.moms.Common.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private final int PAGE_COUNT = 3;
    private ViewPager m_ViewPagar;
    private ImageView[] m_PageIndicatorDots;
    private LinearLayout m_PageIndicator;
    private ProgressDialog progress;

    private static String firstName = "", lastName = "", birthday = "", gender = "", weight = "",
            height = "", experience = "", email = "", password = "", confirmPassword = "";

    private EditText etFirstName, etLastName, etBirthday, etWeight, etHeight,
            etEmail, etPassword, etConfirmPassword;

    private RadioButton rbMale, rbFemale, rbBeginner, rbIntermediate, rbAdvanced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }

    private void init() {

        m_ViewPagar = (ViewPager)findViewById(R.id.pager_signup);
        m_ViewPagar.setAdapter(new ViewPagerAdapter(this));
        m_ViewPagar.addOnPageChangeListener(this);
        m_PageIndicator = (LinearLayout)findViewById(R.id.page_indicator);

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));

        setUiPageViewController();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        for (int i = 0; i < PAGE_COUNT; i++) {
            m_PageIndicatorDots[i].setImageResource(R.drawable.nonselecteditem_dot);
        }

        m_PageIndicatorDots[position].setImageResource(R.drawable.selecteditem_dot);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setUiPageViewController() {

        m_PageIndicatorDots = new ImageView[PAGE_COUNT];

        for (int i = 0; i < PAGE_COUNT; i++) {

            m_PageIndicatorDots[i] = new ImageView(this);
            m_PageIndicatorDots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(8, 0, 8, 0);

            m_PageIndicator.addView(m_PageIndicatorDots[i], params);
        }

        m_PageIndicatorDots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater mInflater;

        public ViewPagerAdapter(Context c){
            super();
            mInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup pager, int position) {

            View v = null;

            if(position == 0) {
                v = mInflater.inflate(R.layout.signup_page_1, null);
            } else if(position == 1) {
                v = mInflater.inflate(R.layout.signup_page_2, null);
            } else {
                v = mInflater.inflate(R.layout.signup_page_3, null);
            }
            v.setTag(position);

            if( position == 0) {
                // first page
                etFirstName = (EditText)v.findViewById(R.id.edt_first_name);
                etFirstName.setText(firstName);
                etLastName = (EditText)v.findViewById(R.id.edt_last_name);
                etLastName.setText(lastName);
                etBirthday = (EditText)v.findViewById(R.id.edt_birthday);
                etBirthday.setText(birthday);

                rbMale = (RadioButton)v.findViewById(R.id.rad_male);
                rbFemale = (RadioButton)v.findViewById(R.id.rad_female);

                if (gender.equals("male")) {
                    rbMale.setChecked(true);
                } else {
                    rbFemale.setChecked(true);
                }

            } else if(position == 1) {
                // second page
                etWeight = (EditText)v.findViewById(R.id.edt_weight);
                etWeight.setText(weight);
                etHeight = (EditText)v.findViewById(R.id.edt_height);
                etHeight.setText(height);

                rbIntermediate = (RadioButton)v.findViewById(R.id.rad_intermediate);
                rbAdvanced = (RadioButton)v.findViewById(R.id.rad_advanced);
                rbBeginner = (RadioButton) v.findViewById(R.id.rad_beginner);

                if(experience == "Intermediate") {
                    rbIntermediate.setChecked(true);
                } else if(experience == "Advanced") {
                    rbAdvanced.setChecked(true);
                } else {
                    rbBeginner.setChecked(true);
                }
            } else {
                // third page
                etEmail = (EditText)v.findViewById(R.id.edt_email);
                etEmail.setText(email);
                etPassword = (EditText)v.findViewById(R.id.edt_password);
                etPassword.setText(password);
                etConfirmPassword = (EditText)v.findViewById(R.id.edt_confirm_password);
                etConfirmPassword.setText(confirmPassword);

                Button btnSignup = (Button)v.findViewById(R.id.btn_signup);
                btnSignup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        email = etEmail.getText().toString();
                        password = etPassword.getText().toString();
                        confirmPassword = etConfirmPassword.getText().toString();
                        weight = etWeight.getText().toString();
                        height = etHeight.getText().toString();

                        if (rbIntermediate.isChecked()) {
                            experience = "Intermediate";
                        } else if(rbAdvanced.isChecked()) {
                            experience = "Advanced";
                        } else {
                            experience = "Beginner";
                        }
                        if (validate()) {
                            acceptDialog();
                        }

                    }
                });
            }

            ((ViewPager)pager).addView(v, 0);
            return v;
        }

        @Override
        public void destroyItem(View pager, int position, Object view) {
            View v = (View)view;
            Object obj =  v.getTag();
            int tag = (int)obj;
            if( tag == 0) {
                // first page
                firstName = etFirstName.getText().toString();
                lastName = etLastName.getText().toString();
                birthday = etBirthday.getText().toString();

                if (rbMale.isChecked()) {
                    gender = "male";
                } else {
                    gender = "female";
                }

            } else if(tag == 1) {
                // second page
                weight = etWeight.getText().toString();
                height = etHeight.getText().toString();

                if (rbIntermediate.isChecked()) {
                    experience = "Intermediate";
                } else if(rbAdvanced.isChecked()) {
                    experience = "Advanced";
                } else {
                    experience = "Beginner";
                }
            } else {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();
            }

            ((ViewPager)pager).removeView((View)view);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }
    }

    private void signUp() {

        progress.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SignUp service = retrofit.create(SignUp.class);

        Call<UserInfo> call = service.sendSignUpRequest(firstName+lastName, email, password, password, birthday, experience,
                gender, weight, height);
        call.enqueue(new Callback<UserInfo>() {

            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.isSuccessful()) {
                    progress.dismiss();
                    UserInfo obj = response.body();
                    SignupActivity.this.finish();
                } else {
                    progress.dismiss();
                    Toast.makeText(SignupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                progress.dismiss();
                String strError = t.getMessage();
                Toast.makeText(SignupActivity.this, strError, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate() {

        if (firstName.isEmpty()) {
            m_ViewPagar.setCurrentItem(0);
            etFirstName.setError("Please enter your first name.");
            etFirstName.requestFocus();
            return false;
        }
        if (lastName.isEmpty()) {
            m_ViewPagar.setCurrentItem(0);
            etLastName.setError("Please enter your last name.");
            etLastName.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            etEmail.setError("Please enter your email address.");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Invalid email address.");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Please enter your password.");
            etPassword.requestFocus();
            return false;
        }

        if (password.compareTo(confirmPassword) != 0){
            etPassword.setError("Dismatch password.");
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

    private void acceptDialog() {

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_disclaimer, true)
                .cancelable(false)
                .build();

        final Button btContinue = (Button)dialog.getCustomView().findViewById(R.id.btn_continue);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                signUp();
            }
        });
        CheckBox cbAccept = (CheckBox)dialog.getCustomView().findViewById(R.id.chk_accept);
        cbAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    btContinue.setBackgroundColor(ContextCompat.getColor(SignupActivity.this, R.color.buttonEnable));
                    btContinue.setEnabled(true);
                } else {
                    btContinue.setBackgroundColor(ContextCompat.getColor(SignupActivity.this, R.color.buttonDisable));
                    btContinue.setEnabled(false);
                }
            }
        });

        dialog.show();
    }
}
