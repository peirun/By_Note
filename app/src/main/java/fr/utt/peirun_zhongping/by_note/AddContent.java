package fr.utt.peirun_zhongping.by_note;

import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContent extends AppCompatActivity implements View.OnClickListener {

    private String valueOfFlag;
    private Button savebtn, deletebtn;
    private EditText editText;
    private ImageView c_img;
    private VideoView v_video;
    private NoteDB noteDB;
    private SQLiteDatabase dbWriter;
    private File imageFile, videoFile;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        valueOfFlag = getIntent().getStringExtra("flag");
        userId = getIntent().getIntExtra("userid",1);
//        savebtn = (Button) findViewById(R.id.save);
//        deletebtn = (Button) findViewById(R.id.delete);
        editText = (EditText) findViewById(R.id.edit_text);
        c_img = (ImageView) findViewById(R.id.c_img);
        v_video = (VideoView) findViewById(R.id.c_video);
//        savebtn.setOnClickListener(this);
//        deletebtn.setOnClickListener(this);
        noteDB = new NoteDB(this);
        dbWriter = noteDB.getWritableDatabase();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_addcontent_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = new Intent(AddContent.this, MainActivity.class);
                startActivity(upIntent);
                break;
            case R.id.save_note:
                addDB();
                finish();
                break;
            case R.id.back_from_note:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void initView() {
        if (valueOfFlag.equals("content")) {
            c_img.setVisibility(View.GONE);
            v_video.setVisibility(View.GONE);
        }
        if (valueOfFlag.equals("photo")) {
            c_img.setVisibility(View.VISIBLE);
            v_video.setVisibility(View.GONE);
            Intent img = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile() + "/" + getTime() + ".jpg");
            img.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(img, 1);
        }
        if (valueOfFlag.equals("video")) {
            c_img.setVisibility(View.GONE);
            v_video.setVisibility(View.VISIBLE);
            Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile() + "/" + getTime() + ".mp4");
            video.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            startActivityForResult(video, 2);
        }
    }

    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NoteDB.CONTENT, editText.getText().toString());
        cv.put(NoteDB.TIME, getTime());
        cv.put(NoteDB.IMAGE, imageFile + "");
        cv.put(NoteDB.VIDEO, videoFile + "");
        cv.put(NoteDB.STARRED, true);
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
            c_img.setImageBitmap(bitmap);
        }
        if (requestCode == 2) {
            v_video.setVideoURI(Uri.fromFile(videoFile));
            v_video.start();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View view) {

    }
}
