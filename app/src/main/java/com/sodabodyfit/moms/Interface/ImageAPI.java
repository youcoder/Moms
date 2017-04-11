package com.sodabodyfit.moms.Interface;

import com.sodabodyfit.moms.Common.Constants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by DEVMAN86 on 4/10/2017.
 */

public interface ImageAPI {
    @GET(Constants.IMAGE_PAGE)
    Call<ResponseBody> getImageRequest(@Header("Authorization") String credentials,
                                       @Query("name") String name);
}
