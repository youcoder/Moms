package com.sodabodyfit.moms.Models;

/**
 * Created by DEVMAN86 on 4/6/2017.
 */

public class Language {
    // table name
    public static final String TABLE = "Language";

    // table columns names
    public static final String KEY_ID = "id";
    public static final String KEY_langname = "langname";
    public static final String KEY_selected = "selected";

    // property
    public Integer lang_id;
    public String lang_name;
    public Boolean selected;
}
