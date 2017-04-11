package com.sodabodyfit.moms.Interface;


import com.sodabodyfit.moms.Common.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by owner on 4/1/2017.
 */

public interface RecoveryPassword {

    @FormUrlEncoded
    @POST(Constants.RECOVERY_PASSWORD_PAGE)
    Call<Object> sendRecoveryPasswordRequest(@Field("email") String email);
}
