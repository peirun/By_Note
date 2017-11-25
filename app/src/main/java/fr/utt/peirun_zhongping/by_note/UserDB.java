package fr.utt.peirun_zhongping.by_note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PEIRUN on 11/25/2017.
 */

public class UserDB extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "users";
    private static final String ID = "idUser";
    private static final String USER_NAME = "userName";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String TIME = "time";

    public UserDB(Context context) {
        super(context, TABLE_NAME, null, 1);
    }
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NAME
                + " TEXT NOT NULL," + EMAIL + " TEXT NOT NULL," + PASSWORD
                + " TEXT NOT NULL," + TIME + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
