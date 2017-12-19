package fr.utt.peirun_zhongping.by_note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PEIRUN on 12/19/2017.
 */

public class NoteDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "notes";
    public static final String CONTENT = "content";
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";
    public static final String NOTE_ID = "note_id";
    public static final String TIME = "time";
    public static final String STARRED = "starred";
    public static final String FOLDER_NAME = "folderName";
    public static final String USERID = "user_id";

    public NoteDB(Context context) {
        super(context, TABLE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + NOTE_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTENT
                + " TEXT NOT NULL," + IMAGE + " TEXT NOT NULL," + VIDEO
                + " TEXT NOT NULL," + TIME + " TEXT NOT NULL," + STARRED + "TEXT NOT NULL,"
                + FOLDER_NAME + "TEXT NOT NULL," + USERID + "INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
