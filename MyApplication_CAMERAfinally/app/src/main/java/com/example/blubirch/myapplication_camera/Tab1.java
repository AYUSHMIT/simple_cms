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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by blubirch on 23/2/17.
 */

public class Tab1 extends Fragment implements View.OnClickListener {

    EditText etResponse;
    TextView tvIsConnected;
    public String a, title;
    ListView myListView;
    public int i;
    public static Map<String, Integer> inventory = new HashMap<>();
    public String name;
    public int count;
    public ArrayList<Image> myString = new ArrayList<Image>();
    public Button sync;
    public int inventory_id;
    private File file;
    MyCustomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);
        LayoutInflater layoutInflater = inflater.from(getContext());
        count = 0;
        name = null;

        Intent intent = getActivity().getIntent();


        sync = (Button) view.findViewById(R.id.sync);
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
        myListView = (ListView) view.findViewById(R.id.l1);
        new HttpAsyncTask().execute("http://192.168.6.83:3000/inventories.json");
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {


                name = data.getStringExtra("name");
                Toast.makeText(getContext(), "On recieve activity", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity().getBaseContext(), "sent!"+name, Toast.LENGTH_LONG).show();
                final String a = data.getStringExtra("ImageCount");
                //a++;

                String s = data.getStringExtra("inventory_id");
                inventory_id = Integer.parseInt(s);
                ImageView imageview = adapter.findimage(name);
                if (imageview == null)

                    // View i
                    Toast.makeText(getActivity().getBaseContext(), a, Toast.LENGTH_LONG).show();
                if (a != null)
                    count = Integer.parseInt(a);
                //Toast.makeText(getBaseContext(), "counter"+Integer.toString(ImageCount), Toast.LENGTH_LONG).show();
                Image Image = new Image(name, count);
                myString.add(Image);
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + name + 2 + ".jpg");
                if (f.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    if (bitmap != null)
                        imageview.setImageBitmap(bitmap);


                }
                imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(getBaseContext(), "this is adapter information"+(name), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), ImageViewer.class);
                        i.putExtra("name", name);
                        i.putExtra("inventory_id", inventory_id);

                        i.putExtra("ImageCount", a);
                        startActivity(i);

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
        }
    }

    public void executeMultipartPost() throws Exception {
        for (Image im : myString) {
            int s = (im.ImageCount);
            for (int i = 1; i <= im.ImageCount; i++) {
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + im.name + i + ".jpg");
                boolean b = adapter.findCode(im.name);

                if (f.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    //MyApp.getBitmapFromMemCache(im.name + i);
                    if(b==true)
                    new HttpPostTask(bitmap, i, im.name).execute();
                    f.delete();
                }}
        }
    }


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
        while ((line = bufferedReader.readLine()) != null) result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
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

            // Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            a = result;
            ArrayList<Country> myStringArray1 = new ArrayList<Country>();
            myStringArray1.add(new Country(0, "something", true));
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
            for (int i = 0; i < json.length(); i++) {
                String s = "";
                JSONObject e = null;
                try {
                    e = json.getJSONObject(i);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                try {
                    s = e.getString("name");
                    String a = e.getString("id");
                    //  Toast.makeText(getApplicationContext(),
                    //   "inventory_id" + a, Toast.LENGTH_LONG)
                    // .show();
                    int id = Integer.parseInt(a);
                    myStringArray1.add(new Country(id, s, true));

                    //adapter.add( s );
                    inventory.put(s, id);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }
            adapter = new MyCustomAdapter(getActivity(),
                    R.layout.simplrow, myStringArray1);

            myListView.setAdapter(adapter);


        }
    }


    private class HttpPostTask extends AsyncTask<String, Void, String> {
        private File f;
        private Bitmap bitmap;
        private int x;
        private String name;

        public HttpPostTask(Bitmap bitmap, int x, String name) {
            this.name = name;
            this.bitmap = bitmap;
            this.x = x;
            this.f = f;

        }


        @Override
        protected String doInBackground(String... urls) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                byte[] data = bos.toByteArray();
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(
                        "http://192.168.6.83:3000/pictures");
                System.out.println("hello");
                String fileName = x + name + ".jpg";//String.format("File_%d.png", new Date().getTime());
                ByteArrayBody image = new ByteArrayBody(data, fileName);//,"Image/jpg", fileName);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);// null//,  Charset.forName("UTF-8")//);
                Content c = new Content();
                c.b = image;
                c.s = name;
                //  reqEntity.addPart("picture",c);
                reqEntity.addPart("image", image);
                reqEntity.addPart("myString", new StringBody(name));
                reqEntity.addPart("inventory_id", new StringBody(Integer.toString(inventory_id)));
                postRequest.setEntity(reqEntity);
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
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity().getBaseContext(), "sent!", Toast.LENGTH_LONG).show();

        }
    }
}
