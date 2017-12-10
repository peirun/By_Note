package fr.utt.peirun_zhongping.by_note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PEIRUN on 11/25/2017.
 */

public class UserDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "users";
    public static final String USER_ID = "idUser";
    public static final String USER_NAME = "userName";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String TIME = "time";

    public UserDB(Context context) {
        super(context, TABLE_NAME, null, 1);
    }
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + USER_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NAME
                + " TEXT NOT NULL," + EMAIL + " TEXT NOT NULL," + PASSWORD
                + " TEXT NOT NULL," + TIME + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
