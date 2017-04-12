package com.sodabodyfit.moms.Interface;

import com.sodabodyfit.moms.Common.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.PUT;

/**
 * Created by DEVMAN86 on 4/12/2017.
 */

public interface ChangePassword {

    @FormUrlEncoded
    @PUT(Constants.CHANGE_PASSWORD_PAGE)
    Call<Object> sendChangePasswordRequest(@Header("Authorization") String credentials,
                                           @Field("old_password") String old_password,
                                           @Field("password") String password);
}
