package com.sodabodyfit.moms.Interface;

import com.sodabodyfit.moms.Common.Constants;
import com.sodabodyfit.moms.Common.UserInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by owner on 4/1/2017.
 */

public interface SignUp {

    @FormUrlEncoded
    @POST(Constants.SIGNUP_PAGE)
    Call<UserInfo> sendSignUpRequest(@Field("name") String name, @Field("email") String email, @Field("password") String password,
                                     @Field("password_confirmation") String confirmation_password, @Field("birthday") String birthday,
                                     @Field("training experience") String experience, @Field("gender") String gender,
                                     @Field("weight") String weight, @Field("height") String height);


}
