package com.example.blubirch.myapplication_camera;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Date;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;

import static java.nio.charset.Charset.forName;


public class Imageupload extends AppCompatActivity implements View.OnClickListener {

    private Uri x;
    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonCapture;

    private EditText editText;
    private ImageView imageView;

    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_IMAGE_CAPTURE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageupload);

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonCapture = (Button) findViewById(R.id.buttonCapture);


        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonCapture.setOnClickListener(this);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }


    public void executeMultipartPost() throws Exception {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        Bitmap bitmap = drawable.getBitmap();
        new HttpAsyncTask(bitmap,x).execute();


    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {

            Uri filePath = data.getData();
            x=filePath;


            try {
                Bitmap imgBitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Toast.makeText(getBaseContext(), "sent!", Toast.LENGTH_LONG).show();
                System.out.println(filePath);
                imageView.setImageBitmap(imgBitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Before if block");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 100, 100, false);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
           imageBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "Imagename.jpg");
            try {
                f.createNewFile();
            } catch (Exception e) {
            }
            try {
                FileOutputStream fo = new FileOutputStream(f);

                fo.write(bytes.toByteArray());
                fo.close();
            } catch (Exception e) {
            }

            imageView.setImageBitmap(imageBitmap);
        }


    }


    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if (v == buttonCapture) {
            dispatchTakePictureIntent();
        }
        if (v == buttonUpload) {
            // startActivity(new Intent(this, SendToServer.class));
            try {
                executeMultipartPost();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private Bitmap bitmap;
        private Uri x;
        public HttpAsyncTask(Bitmap bitmap,Uri x){
            this.bitmap= bitmap;
            this.x=x;

        }




        @Override
        protected String doInBackground(String... urls) {
            try {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();


              //  BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

               // Bitmap bitmap = drawable.getBitmap();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);

                byte[] data = bos.toByteArray();

                HttpClient httpClient = new DefaultHttpClient();

                HttpPost postRequest = new HttpPost(

                        "http://10.0.2.2:3000/pictures");
                System.out.println("hello");
             //   Toast.makeText(getBaseContext(), filepath, Toast.LENGTH_LONG).show();
               // postRequest.addHeader("Content-type", "image/jpeg; charset=\"UTF-8\"");
                //heads.put("Content-Type", "image/png;charset=utf-8");
              //  postRequest.addHeader("Content-Type", "multipart/form-data");

                String fileName = "abcde.jpg";//String.format("File_%d.png", new Date().getTime());
                ByteArrayBody image = new ByteArrayBody(data,fileName);//,"image/jpg", fileName);

                // File file= new File("/mnt/sdcard/forest.png");

                // FileBody bin = new FileBody(file);

                MultipartEntity reqEntity = new MultipartEntity(


                      HttpMultipartMode.BROWSER_COMPATIBLE);// null//,  Charset.forName("UTF-8")//);

               // MultipartEntityBuilder multipartEntity  = MultipartEntityBuilder.create();
               // multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                // File file = new File(x.toString());
              // FileInputStream fileInputStream = new FileInputStream(file);
               // File file=new File(x.toString());
              //  multipartEntity.addBinaryBody("image",file, ContentType.create("image/jpeg"), fileName);

               reqEntity.addPart("image", image);
                //postRequest.addHeader("Content-Type", "multipart/form-data;");

                //StringBody contentString = new StringBody("multipart/form-data");
               // reqEntity.addPart("Content-Type",contentString);
               // reqEntity.addPart("image", new InputStreamBody(fileInputStream,"image/jpeg",fileName));

               postRequest.setEntity(reqEntity);
                //HttpEntity reqEntity = multipartEntity.build();
                //postRequest.setEntity();


                int timeoutConnection = 60000;
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        timeoutConnection);
                int timeoutSocket = 60000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                HttpConnectionParams.setTcpNoDelay(httpParameters, true);

                HttpResponse response = httpClient.execute(postRequest);



                BufferedReader reader = new BufferedReader(new InputStreamReader(

                        response.getEntity().getContent(), "UTF-8"));

                String sResponse;

                StringBuilder s = new StringBuilder();

                while ((sResponse = reader.readLine()) != null) {

                    s = s.append(sResponse);

                }

                System.out.println("Response: " + s);
                return sResponse;

            } catch (Exception e) {

                // handle exception here
                e.printStackTrace();
                return null;

                // Log.e(e.getClass().getName(), e.getMessage());

            }


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "sent!", Toast.LENGTH_LONG).show();

        }
    }
}

//utkarsh
















/*import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;


public class ImageUpload extends ReceiveFromServerActivity implements View.OnClickListener {

    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonCapture;

    private EditText editText;
    private ImageView imageView;

    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_IMAGE_CAPTURE = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageupload);

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonCapture = (Button) findViewById(R.id.buttonCapture);

        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonCapture.setOnClickListener(this);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK ) {



            Uri filePath = data.getData();


            try {
                Bitmap imgBitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(imgBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Before if block");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);


        }
    }



    @Override
    public void onClick(View v) {
        if(v == buttonChoose){
            showFileChooser();
        }
        if(v == buttonCapture){
            dispatchTakePictureIntent();
        }


    }

}*/