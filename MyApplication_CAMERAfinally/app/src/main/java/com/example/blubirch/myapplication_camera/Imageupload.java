package com.example.blubirch.myapplication_camera;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Imageupload extends Activity implements  View.OnClickListener {
    private  String name;

    private Uri x;
    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonCapture;
    private LruCache<String, Bitmap> mMemoryCache;
   private File file;
    private EditText editText;
    private ImageView imageView;

    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_IMAGE_CAPTURE = 2;
    private int count=0;
    private int a=0;
    private int inventory_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageupload);
        a=0;

       // buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonCapture = (Button) findViewById(R.id.buttonCapture);


       // editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
//        buttonUpload.setOnClickListener(this);
        buttonCapture.setOnClickListener(this);
        Intent intent = getIntent();
         name = intent.getStringExtra("name");
        inventory_id=Integer.parseInt(intent.getStringExtra("inventory_id"));


        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        final int cacheSize = maxMemory / 8;


    }







    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }








    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("Image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }


   /* public void executeMultipartPost() throws Exception {
        //BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        //Bitmap bitmap = drawable.getBitmap();
        for(int i=1;i<=a;i++){
       Bitmap bitmap = getBitmapFromMemCache("Image"+i );
        //for(int i=1;i<=a;i++)
        new HttpAsyncTask(bitmap,i,name).execute();}


    }*/


    private void dispatchTakePictureIntent() {
        //ImageCount=MainActivity.inventories.get(name)+1;
      // MainActivity.inventories.put(name,ImageCount);


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

           a++;
            try {
                Bitmap imgBitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Toast.makeText(getBaseContext(), "sent!", Toast.LENGTH_LONG).show();


                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
               // imgBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator +name+a+ ".jpg");
                // Toast.makeText(getBaseContext(), f.toString(), Toast.LENGTH_LONG).show();

                try {
                    //f.createNewFile();
                } catch (Exception e) {
                }
                try {
                    FileOutputStream fo = new FileOutputStream(f);

                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (Exception e) {
                }

                showFileChooser();



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       // System.out.println("Before if block");
       else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { a++;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 100, 100, false);
           // MyApp.addBitmapToMemoryCache(name+a, imageBitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator +name+a+ ".jpg");
           // Toast.makeText(getBaseContext(), f.toString(), Toast.LENGTH_LONG).show();

            try {
                //f.createNewFile();
            } catch (Exception e) {
            }
            try {
                FileOutputStream fo = new FileOutputStream(f);

                fo.write(bytes.toByteArray());
                fo.close();
            } catch (Exception e) {
            }
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
           // imageView.setImageBitmap(imageBitmap);
        }
       else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode != RESULT_OK)
        {

            Intent i=new Intent();

            i.putExtra("name",name);
            i.putExtra("inventory_id",Integer.toString(inventory_id));
            i.putExtra("ImageCount",Integer.toString(a));
          //  i.putExtra("lru",mMemoryCache.toString());
           // String s = mMemoryCache.toString();
            Toast.makeText(getBaseContext(), "hellooo"+name, Toast.LENGTH_LONG).show();
            setResult(1, i);
            finish();




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
           /* Intent i=new Intent();

            i.putExtra("name",name);
            i.putExtra("ImageCount",Integer.toString(a));
            i.putExtra("lru",mMemoryCache.toString());
            String s = mMemoryCache.toString();

            setResult(1, i);
            finish();*/





        }

     /*   if (v == buttonUpload) {
            // startActivity(new Intent(this, SendToServer.class));
            try {
                executeMultipartPost();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }*/


    }

   private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private Bitmap bitmap;
        private int x;
        private String name;
        public HttpAsyncTask(Bitmap bitmap,int x,String name){
            this.name=name;
            this.bitmap= bitmap;
            this.x=x;

        }




        @Override
        protected String doInBackground(String... urls) {
            try {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();


              //  BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

               // Bitmap bitmap = drawable.getBitmap();
                //bitmap = getBitmapFromMemCache("Image" + ImageCount);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);

                byte[] data = bos.toByteArray();

                HttpClient httpClient = new DefaultHttpClient();

                HttpPost postRequest = new HttpPost(

                        "http://10.0.2.2:3000/pictures");
              //  HttpPost postRequest = new HttpPost(

                        //"http://192.168.6.83/pictures");

                System.out.println("hello");
             //   Toast.makeText(getBaseContext(), filepath, Toast.LENGTH_LONG).show();
               // postRequest.addHeader("Content-type", "Image/jpeg; charset=\"UTF-8\"");
                //heads.put("Content-Type", "Image/png;charset=utf-8");
              //  postRequest.addHeader("Content-Type", "multipart/form-data");

                String fileName = x+name+".jpg";//String.format("File_%d.png", new Date().getTime());
                ByteArrayBody image = new ByteArrayBody(data,fileName);//,"Image/jpg", fileName);

                // File file= new File("/mnt/sdcard/forest.png");

                // FileBody bin = new FileBody(file);

                MultipartEntity reqEntity = new MultipartEntity(


                      HttpMultipartMode.BROWSER_COMPATIBLE);// null//,  Charset.forName("UTF-8")//);

               // MultipartEntityBuilder multipartEntity  = MultipartEntityBuilder.create();
               // multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                // File file = new File(x.toString());
              // FileInputStream fileInputStream = new FileInputStream(file);
               // File file=new File(x.toString());
              //  multipartEntity.addBinaryBody("Image",file, ContentType.create("Image/jpeg"), fileName);
                Content c=new Content();
                c.b=image;
                c.s=name;
                //  reqEntity.addPart("picture",c);
               reqEntity.addPart("image", image);
                reqEntity.addPart("myString", new StringBody(name));

                
                //postRequest.addHeader("Content-Type", "multipart/form-data;");

                //StringBody contentString = new StringBody("multipart/form-data");
               // reqEntity.addPart("Content-Type",contentString);
               // reqEntity.addPart("Image", new InputStreamBody(fileInputStream,"Image/jpeg",fileName));

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

