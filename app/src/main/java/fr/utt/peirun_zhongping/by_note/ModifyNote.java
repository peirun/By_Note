package fr.utt.peirun_zhongping.by_note;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModifyNote extends AppCompatActivity {

    private int note_id;
    private EditText editText;
    private ImageView m_img;
    private VideoView v_video;
    private NoteDB noteDB;
    private SQLiteDatabase dbWriter;
    private File imageFile, videoFile;
    private int userId;
    private Menu menu;
    private String isStarred;
    private SQLiteDatabase dbReader;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        note_id = getIntent().getIntExtra(NoteDB.NOTE_ID,0);
        isStarred = getIntent().getStringExtra(NoteDB.STARRED);
        userId = getIntent().getIntExtra("userid",0);
        editText = (EditText) findViewById(R.id.m_et);
        m_img = (ImageView) findViewById(R.id.m_img);
        v_video = (VideoView) findViewById(R.id.m_video);
        noteDB = new NoteDB(this);
        dbReader = noteDB.getReadableDatabase();
        dbWriter = noteDB.getWritableDatabase();
        selectDB(note_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.activity_addcontent_appbar2, menu);
        if(isStarred.equals("true")){
            Drawable d = getResources().getDrawable(R.drawable.star_starred);
            menu.getItem(0).setIcon(d);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = new Intent(ModifyNote.this, MainActivity.class);
                startActivity(upIntent);
                break;
            case R.id.save_note:
                updateDB();
                finish();
                break;
            case R.id.delete_note:
                deleteDB();
                finish();
                break;
            case R.id.note_starred:
                if(isStarred.equals("true")){
                    Drawable d = getResources().getDrawable(R.drawable.star);
                    menu.getItem(0).setIcon(d);
                    isStarred = "false";
                }else{
                    Drawable d = getResources().getDrawable(R.drawable.star_starred);
                    menu.getItem(0).setIcon(d);
                    isStarred = "true";
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteDB() {
        dbWriter.delete(NoteDB.TABLE_NAME,
                "note_id=" + note_id, null);
    }

    public void initView(String content, String imageUri, String videoUri, String folder_name) {

        if(imageUri == null){
            m_img.setVisibility(View.GONE);
        }
        if(videoUri == null){
            v_video.setVisibility(View.GONE);
        }
        editText.setText(content);
        Bitmap bitmap = BitmapFactory.decodeFile(imageUri);
        m_img.setImageBitmap(bitmap);
        v_video.setVideoURI(Uri.parse(videoUri));
        v_video.start();

    }

    public void updateDB(){
        ContentValues cv = new ContentValues();
        cv.put(NoteDB.CONTENT, editText.getText().toString());
        cv.put(NoteDB.TIME, getTime());
        dbWriter.update(NoteDB.TABLE_NAME, cv, "note_id="+note_id, null);
    }
    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NoteDB.CONTENT, editText.getText().toString());
        cv.put(NoteDB.TIME, getTime());
        cv.put(NoteDB.IMAGE, imageFile + "");
        cv.put(NoteDB.VIDEO, videoFile + "");
        cv.put(NoteDB.STARRED, "true");
        cv.put(NoteDB.FOLDER_NAME, "personal");
        cv.put(NoteDB.USERID, userId);
        dbWriter.insert(NoteDB.TABLE_NAME, null, cv);
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            m_img.setImageBitmap(bitmap);
        }
        if (requestCode == 2) {
            v_video.setVideoURI(Uri.fromFile(videoFile));
            v_video.start();
        }
    }
    public void selectDB(int noteId) {
        cursor = dbReader.query(NoteDB.TABLE_NAME, null, NoteDB.NOTE_ID + "=?", new String[] {String.valueOf(noteId)}, null,
                null, null);

        if(cursor.moveToFirst())
        {
            String content = cursor.getString(cursor.getColumnIndex(NoteDB.CONTENT));
            String imageUri = cursor.getString(cursor.getColumnIndex(NoteDB.IMAGE));
            String videoUri = cursor.getString(cursor.getColumnIndex(NoteDB.VIDEO));
            String folder_name = cursor.getString(cursor.getColumnIndex(NoteDB.FOLDER_NAME));
            initView(content, imageUri, videoUri, folder_name);

        }
        cursor.close();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
