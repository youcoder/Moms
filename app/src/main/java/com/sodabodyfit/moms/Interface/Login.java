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

public interface Login {

    @FormUrlEncoded
    @POST(Constants.LOGIN_PAGE)
    Call<UserInfo> sendLoginRequest(@Field("email") String email,
                                    @Field("password") String password);
}
