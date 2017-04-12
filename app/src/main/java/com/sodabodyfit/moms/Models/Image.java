package com.sodabodyfit.moms.Models;

/**
 * Created by DEVMAN86 on 4/2/2017.
 */

public class Image {
    // table name
    public static final String TABLE = "Images";

    // table columns names
    public static final String KEY_ID = "id";
    public static final String KEY_name = "name";
    public static final String KEY_isInAssets = "isInAssets";
    public static final String KEY_path = "path";

    // property
    public int image_id;
    public String name;
    public Boolean isInAssets;
    public Boolean path;
}
