package com.example.blubirch.myapplication_camera;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ImageViewer extends AppCompatActivity {
    private int count;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        Intent intent = getIntent();
         name=intent.getStringExtra("name");
         count= Integer.parseInt(intent.getStringExtra("ImageCount"));
        // Note that Gallery view is deprecated in Android 4.1---
        Gallery gallery = (Gallery) findViewById(R.id.gallery1);
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,long id)
            {
                Toast.makeText(getBaseContext(),"pic"  + " selected",
                        Toast.LENGTH_SHORT).show();
                int e=position+1;
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + name + e + ".jpg");
                // display the images selected
                ImageView imageView = (ImageView) findViewById(R.id.image1);

                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
                //imageView.setImageResource(imageIDs[position]);
            }
        });
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        public ImageAdapter(Context c)
        {
            context = c;
            // sets a grey background; wraps around the images
            TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();
        }
        // returns the number of images
        public int getCount() {
            //return imageIDs.length;
            return count-1;
        }
        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }
        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }
        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setMaxHeight(200);
            imageView.setMaxWidth(200);

            int e =position+1;

            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + name + e + ".jpg");
            if(f.exists())
            {
                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                if(bitmap!=null)
                    imageView.setImageBitmap(bitmap);


            }






          //  imageView.setImageResource(imageIDs[position]);
            imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
    }
}
