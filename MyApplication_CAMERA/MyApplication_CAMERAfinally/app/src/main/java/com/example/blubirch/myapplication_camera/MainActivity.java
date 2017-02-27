package com.example.blubirch.myapplication_camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener

{


    public static Map<String, Integer> inventory = new HashMap<>();
    public int counter = 0;
    private static String logtag = "cameraApp";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    Button button_upload;
    Button button_recieve;
    Button button_UploadImage;
    EditText etResponse;
    TextView tvIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Button cameraButton = (Button) findViewById(R.id.button_camera);
      //  Button button_upload = (Button) findViewById(R.id.button_upload);
        Button button_recieve = (Button) findViewById(R.id.button_recieve);
       // Button button_UploadImage = (Button) findViewById(R.id.button_UploadImage);
       // etResponse = (EditText) findViewById(R.id.etResponse);
       // tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        //button_UploadImage.setOnClickListener(this);
        //button_upload.setOnClickListener(this);
        button_recieve.setOnClickListener(this);
       // cameraButton.setOnClickListener(this);

    }
    /*private View.OnClickListener cameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){


                case R.id.button_camera:
                    takephoto(v);
                    break;
                case R.id.button_upload:
                    new HttpAsyncTask();
                    break;
                case R.id.button_recieve:

                    startActivity(new Intent(this, ReceiveFromServerActivity.class));
                    break;
            }

        }
    };*/


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        /*    case R.id.button_camera:
                takephoto(v);
                break;
            case R.id.button_upload:
                new HttpAsyncTask();
                break;*/
            case R.id.button_recieve:

                startActivity(new Intent(this, ReceiveFromServerActivity.class));
                break;
           // case R.id.button_UploadImage:

               // break;
        }


    }


   /* private void takephoto(View v) {

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pictures.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);

    }
   /* public void postImage(){
        RequestParams params = new RequestParams();
        params.put("picture[name]","MyPictureName");
        params.put("picture[image]", new File(Environment.getExternalStorageDirectory().getPath() + "/Pictures/CameraApp/test.jpg"));
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://x.x.x.x:3000/pictures/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Log.w("async", "success!!!!");
            }
        });
    }*/


 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.e("Image", "intent: " + intent);
        if (resultCode == Activity.RESULT_OK) {
            Log.e("Image", "intent: " + intent);
            Uri selectedImage = imageUri;
            getContentResolver().notifyChange(selectedImage, null);

            ImageView imageView = (ImageView) findViewById(R.id.image_camera);
            ContentResolver cr = getContentResolver();
            Bitmap bitmap;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e(logtag, e.toString());
            }
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... urls) {

            // Creating HTTP client
            HttpClient httpClient = new DefaultHttpClient();
            // Creating HTTP Post
            HttpPost httpPost = new HttpPost(
                    "http://www.example.com/login");

            // Building post parameters
            // key and value pair
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("email", "user@gmail.com"));
            nameValuePair.add(new BasicNameValuePair("message",
                    "Hi, trying Android HTTP post!"));

            // Url Encoding the POST parameters
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // writing error to Log
                e.printStackTrace();
            }

            // Making HTTP Request
            try {
                HttpResponse response = httpClient.execute(httpPost);

                // writing response to log
                Log.d("Http Response:", response.toString());
            } catch (ClientProtocolException e) {
                // writing exception to log
                e.printStackTrace();
            } catch (IOException e) {
                // writing exception to log
                e.printStackTrace();

            }
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            etResponse.setText("dfwsrsdfsdfs");
        }
    } */



/*public class PostImage extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... urls) {

        RequestParams params = new RequestParams();
        params.put("picture[name]","MyPictureName");
        params.put("picture[image]", new File(Environment.getExternalStorageDirectory().getPath() + "/Pictures/CameraApp/test.jpg"));
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://x.x.x.x:3000/pictures/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Log.w("async", "success!!!!");
            }
        });


        return null ;
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Void result) {
        Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        etResponse.setText("dfwsrsdfsdfs");
    }
}*/
}