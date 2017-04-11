package com.sodabodyfit.moms.Common;

import android.os.Parcel;
import android.os.Parcelable;

import com.sodabodyfit.moms.Models.Workout;

import java.util.ArrayList;


/**
 * Created by MokRan on 2017-02-09.
 */

public class CardInfo {

    public int cardPhotoId;
    public int detailPhotoId;
    public ArrayList<Workout> listWorkout;

    public CardInfo(int resId, int detailPhotoId, ArrayList<Workout> list) {
        this.cardPhotoId = resId;
        this.detailPhotoId = detailPhotoId;
        this.listWorkout = list;
    }
}
