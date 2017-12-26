package fr.utt.peirun_zhongping.by_note;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
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
    private Menu menu;
    private String isStarred;
    private String folder_name;
    // take photo
    private String mCurrentPhotoPath;
    private String mCurrentVideoPath;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private static final String VIDEO_STORAGE_KEY = "viewvideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
    private VideoView mVideoView;
    private Uri mVideoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        valueOfFlag = getIntent().getStringExtra("flag");
        userId = getIntent().getIntExtra("userid",1);
        folder_name = getIntent().getStringExtra(NoteDB.FOLDER_NAME);
//        savebtn = (Button) findViewById(R.id.save);
//        deletebtn = (Button) findViewById(R.id.delete);
        editText = (EditText) findViewById(R.id.edit_text);
        c_img = (ImageView) findViewById(R.id.c_img);
        v_video = (VideoView) findViewById(R.id.c_video);
//        savebtn.setOnClickListener(this);
//        deletebtn.setOnClickListener(this);
        noteDB = new NoteDB(this);
        dbWriter = noteDB.getWritableDatabase();
        isStarred = "false";


        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.activity_addcontent_appbar, menu);
//        Toast.makeText(AddContent.this,folder_name,Toast.LENGTH_LONG).show();
        getSupportActionBar().setTitle(folder_name);
        if(folder_name.equals("Starred")){
            Drawable d = getResources().getDrawable(R.drawable.star_starred);
            menu.getItem(0).setIcon(d);
            isStarred = "true";
        }
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
    public void initView() {
        if (valueOfFlag.equals("content")) {
            c_img.setVisibility(View.GONE);
            v_video.setVisibility(View.GONE);
        }
        if (valueOfFlag.equals("photo")) {
            c_img.setVisibility(View.VISIBLE);
            v_video.setVisibility(View.GONE);
            dispatchTakePictureIntent();
//            Intent img = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            imageFile = new File(Environment.getExternalStorageDirectory()
//                    .getAbsoluteFile() + "/" + getTime() + ".jpg");
//            img.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
//            startActivityForResult(img, 1);
        }
        if (valueOfFlag.equals("video")) {
            c_img.setVisibility(View.GONE);
            v_video.setVisibility(View.VISIBLE);
            dispatchTakeVideoIntent();
//            Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//            videoFile = new File(Environment.getExternalStorageDirectory()
//                    .getAbsoluteFile() + "/" + getTime() + ".mp4");
//            video.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
//            startActivityForResult(video, 2);
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (videoFile != null) {
                Uri videoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        videoFile);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                startActivityForResult(takeVideoIntent, 2);
            }
        }
    }


    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(NoteDB.CONTENT, editText.getText().toString());
        cv.put(NoteDB.TIME, getTime());
        cv.put(NoteDB.IMAGE, imageFile + "");
        cv.put(NoteDB.VIDEO, videoFile + "");
        cv.put(NoteDB.STARRED, isStarred);
        cv.put(NoteDB.FOLDER_NAME, folder_name);
        cv.put(NoteDB.USERID, userId);
        dbWriter.insert(NoteDB.TABLE_NAME, null, cv);
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        imageFile = image;
        return image;
    }

    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "AVI_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File video = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentVideoPath = video.getAbsolutePath();
        videoFile = video;
        return video;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = c_img.getWidth()+1;
        int targetH = c_img.getHeight()+1;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        c_img.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            setPic();
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                c_img.setImageBitmap(imageBitmap);
//                setPic();
//                galleryAddPic();


//            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//            c_img.setImageBitmap(bitmap);
        }
        if (requestCode == 2) {
            Uri videoUri = data.getData();
            v_video.setVideoURI(videoUri);
//            v_video.setVideoURI(Uri.fromFile(videoFile));
            v_video.start();
        }
    }

    // Some lifecycle callbacks so that the image can survive orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null) );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.setVisibility(
                savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View view) {

    }
}
