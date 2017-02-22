package com.example.blubirch.myapplication_camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by blubirch on 10/1/17.
 */

public class ReceiveFromServerActivity extends MainActivity {
    EditText etResponse;
    TextView tvIsConnected;
    public String a, title;
    ListView myListView;
    public int i;
    public String name;
    public int count;
    public ArrayList<image> myString = new ArrayList<image>();

    public Button sync;
    public int inventory_id;
    private File file;
    MyCustomAdapter adapter;
    //Intent i=new Intent(ReceiveFromServerActivity.this, Imageupload.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.receivefromserver);
        count=0;
        name=null;

        Intent intent = getIntent();


        sync = (Button) findViewById(R.id.sync);
        sync.setOnClickListener(this);







        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        MyApp.mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };









        // get reference to the views
       // etResponse = (EditText) findViewById(R.id.etResponse);
       // tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        myListView = (ListView)findViewById(R.id.l1);
        // check if you are connected or not
      //  if (isConnected()) {
//            tvIsConnected.setBackgroundColor(0xFF00CC00);
           // tvIsConnected.setText("You are conncted");
      //  } else {
          //  tvIsConnected.setText("You are NOT conncted");
      //  }














        // call AsynTask to perform network operation on separate thread
        //new HttpAsyncTask().execute("http://192.168.6.83:3000/inventories.json");//("http://10.0.2.2:3000/inventories.json");
        new HttpAsyncTask().execute("http://192.168.6.83:3000/inventories.json");
    }

    public static void image_upload(Intent i)

    {
       // Intent i=new Intent(ReceiveFromServerActivity.this, Imageupload.class);
       // int a= inventory.get(c.name);
       // i.putExtra("inventory_id",Integer.toString(a));
       // i.putExtra("name",c.name);
      //  startActivityForResult(i,1);







    }













    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == 1){
                //String stredittext=data.getStringExtra("edittextvalue");
              //  Toast.makeText(getBaseContext(), "this is adapter information"+(adapter.countryList.get(1).name), Toast.LENGTH_LONG).show();

                name = data.getStringExtra("name");
                String a = data.getStringExtra("count");
                String s = data.getStringExtra("inventory_id");
                inventory_id = Integer.parseInt(s);
                ImageView imageview = adapter.findimage(name);
                if(imageview==null)

               // View i
                //Toast.makeText(getBaseContext(), a, Toast.LENGTH_LONG).show();
                if(a!=null)
                    count = Integer.parseInt(a);
                image image = new image(name,count);
                myString.add(image);
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + name + 1 + ".jpg");
                if(f.exists())
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    if(bitmap!=null)
                    imageview.setImageBitmap(bitmap);


                }
  imageview.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Toast.makeText(getBaseContext(), "this is adapter information"+(name), Toast.LENGTH_LONG).show();


      }
  });



            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == sync) {


            try {

                executeMultipartPost();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }}

    public void executeMultipartPost() throws Exception {
        //BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        //Bitmap bitmap = drawable.getBitmap();


        for (image im : myString) {
            int s = (im.count);

            //  Toast.makeText(getBaseContext(), Integer.toString(im.count), Toast.LENGTH_LONG).show();


            for (int i = 1; i <= im.count; i++) {


                    File f = new File(Environment.getExternalStorageDirectory()
                           + File.separator + im.name + i + ".jpg");
                boolean b=adapter.findCode(im.name);

                   // if (adapter.findCode(im.name) == true) {
               // Toast.makeText(getBaseContext(), "array list true or false"+b, Toast.LENGTH_LONG).show();

                        if (f.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath()); //MyApp.getBitmapFromMemCache(im.name + i);
                            Toast.makeText(getBaseContext(), bitmap.toString(), Toast.LENGTH_LONG).show();
                            //for(int i=1;i<=a;i++)
                            new HttpPostTask(bitmap, i, im.name).execute();
                            f.delete();
                        }
                   // }

            }


        }}



    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            a=result;
            ArrayList<Country> myStringArray1 = new ArrayList<Country>();
            myStringArray1.add(new Country(0,"something",true));
            // LinearLayout ll1=(LinearLayout)findViewById(R.id.ll1);
            // ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReceiveFromServerActivity.this,R.layout.simplrow,myStringArray1);

            //    myListView.setAdapter(adapter);
            // ArrayList<Country>  = new ArrayList<Country>();
            Country country;

            JSONArray json = null;

            try {
                json = new JSONArray(a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i=0;i<json.length();i++)
            {
                String s="";
                JSONObject e = null;
                try {
                    e = json.getJSONObject(i);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                try {
                    s=e.getString("name");
                    String a=e.getString("id");
                    Toast.makeText(getApplicationContext(),
                            "inventory_id" + a, Toast.LENGTH_LONG)
                            .show();
                    int id=Integer.parseInt(a);
                    myStringArray1.add(new Country(id,s,true));

                    //adapter.add( s );
                    inventory.put(s,id);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }
             adapter = new MyCustomAdapter(ReceiveFromServerActivity.this,
                    R.layout.simplrow,myStringArray1);

            myListView.setAdapter( adapter );
            /*myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId)
                {
                   /* Toast.makeText(getApplicationContext(),
                            "Click ListItem Number " + position, Toast.LENGTH_LONG)
                            .show();*/
                 /*   Intent i=new Intent(ReceiveFromServerActivity.this, Imageupload.class);
                    //String name = (String) listView.getItemAtPosition(position);
                    Country name = (Country) listView.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(),
                            name.name + position, Toast.LENGTH_LONG)
                            .show();
                    System.out.println(name.name);
                    counter++;
                   int a= inventory.get(name.name);
                    i.putExtra("inventory_id",Integer.toString(a));
                    i.putExtra("name",name.name);
                    startActivityForResult(i,1);  */
               // }
           // });

            /*a=result;
            JSONArray json = null;
            try {
                json = new JSONArray(a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //for(int i=0;i<json.length();i++)
            //{
            String s="";
            JSONObject e = null;
            try {
                e = json.getJSONObject(0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            try {
                s=e.getString("articleList");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


            //}

            etResponse.setText(s);*/

            /*try {
                JSONArray jArray = new JSONArray(result);
                for(i=0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                     title = jObject.getString("title");

                    //String tab1_text = jObject.getString("tab1_text");
                    //int active = jObject.getInt("active");

                } // End Loop
                //this.progressDialog.dismiss();
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }
            etResponse.setText(title); // catch (JSONException e)
        } */// protected void onPostExecute(Void v)


        }
    }









    private class HttpPostTask extends AsyncTask<String, Void, String> {
        private File f;
        private Bitmap bitmap;
        private int x;
        private String name;
        public HttpPostTask(Bitmap bitmap,int x,String name){
            this.name=name;
            this.bitmap= bitmap;
            this.x=x;
            this.f=f;

        }




        @Override
        protected String doInBackground(String... urls) {
            try {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();


                //  BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

                // Bitmap bitmap = drawable.getBitmap();
                //bitmap = getBitmapFromMemCache("image" + count);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);

                byte[] data = bos.toByteArray();

                HttpClient httpClient = new DefaultHttpClient();

                HttpPost postRequest = new HttpPost(

                        "http://192.168.6.83:3000/pictures");
                //  HttpPost postRequest = new HttpPost(

                //"http://192.168.6.83/pictures");

                System.out.println("hello");
                //   Toast.makeText(getBaseContext(), filepath, Toast.LENGTH_LONG).show();
                // postRequest.addHeader("Content-type", "image/jpeg; charset=\"UTF-8\"");
                //heads.put("Content-Type", "image/png;charset=utf-8");
                //  postRequest.addHeader("Content-Type", "multipart/form-data");

                String fileName = x+name+".jpg";//String.format("File_%d.png", new Date().getTime());
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
                content c=new content();
                c.b=image;
                c.s=name;
                //  reqEntity.addPart("picture",c);
                reqEntity.addPart("image", image);
                reqEntity.addPart("myString", new StringBody(name));
                reqEntity.addPart("inventory_id",new StringBody(Integer.toString(inventory_id)));


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

