package com.sodabodyfit.moms.Provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_PATH = "/data/data/com.sodabodyfit.moms/databases/";
    private static final String DATABASE_NAME = "mom.db";

    private static final String TAG = DBHelper.class.getSimpleName();

    private final Context context;
    private final String dbPath;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

        this.context = context;
        this.dbPath = DATABASE_PATH + DATABASE_NAME;
        checkExists();
    }

    /**
     * Checks if the database asset needs to be copied and if so copies it to the
     * default location.
     */
    private void checkExists() {

        try {
            File dbFile = new File(dbPath);

            if (!dbFile.exists()) {

                Log.i(TAG, "creating database..");

                InputStream is = context.getAssets().open("databases/mom.db");

                File dbFolder = new File(DATABASE_PATH);
                if(!dbFolder.exists()) {
                    dbFolder.mkdirs();
                }

                dbFile.createNewFile();
                copyStream(is, new FileOutputStream(dbFile));

                Log.i(TAG, "databases has been copied to " + dbFile.getAbsolutePath());
            }
        }catch(IOException ex) {
            String temp = ex.getMessage();
        }
    }

    private void copyStream(InputStream is, OutputStream os) {

        try {
            byte buf[] = new byte[1024];
            int c = 0;
            while (true) {
                c = is.read(buf);
                if (c == -1)
                    break;
                os.write(buf, 0, c);
            }
            is.close();
            os.close();
        }catch(IOException ex) {
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
