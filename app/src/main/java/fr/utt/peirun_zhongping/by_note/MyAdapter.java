package fr.utt.peirun_zhongping.by_note;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by PEIRUN on 12/20/2017.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private LinearLayout layout;

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    // set view
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.cell, null);
        TextView contenttv = (TextView) layout.findViewById(R.id.list_content);
        TextView timetv = (TextView) layout.findViewById(R.id.list_time);
        ImageView imgiv = (ImageView) layout.findViewById(R.id.list_img);
        ImageView videoiv = (ImageView) layout.findViewById(R.id.list_video);
        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String url_photo = cursor.getString(cursor.getColumnIndex("image"));
        String url_video = cursor.getString(cursor.getColumnIndex("video"));
        contenttv.setText(content);
        timetv.setText(time);
        imgiv.setImageBitmap(getImageThumbnail(url_photo, 200, 200));
        videoiv.setImageBitmap(getVideoThumbnail(url_video, 200, 200,
                MediaStore.Images.Thumbnails.MICRO_KIND));
        return layout;
    }

    public Bitmap getImageThumbnail(String uri, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth / width;
        int beHeight = options.outHeight / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private Bitmap getVideoThumbnail(String uri, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return bitmap;
    }
}
