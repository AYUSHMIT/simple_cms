package com.example.blubirch.myapplication_camera;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.R.attr.name;

/**
 * Created by blubirch on 10/1/17.
 */

public class ReceiveFromServerActivity extends MainActivity {
    EditText etResponse;
    TextView tvIsConnected;
    public String a, title;
    ListView myListView;
    public int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.receivefromserver);

        // get reference to the views
        etResponse = (EditText) findViewById(R.id.etResponse);
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        myListView = (ListView)findViewById(R.id.l1);
        // check if you are connected or not
        if (isConnected()) {
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        } else {
            tvIsConnected.setText("You are NOT conncted");
        }

        // call AsynTask to perform network operation on separate thread
        //new HttpAsyncTask().execute("http://192.168.6.83:3000/inventories.json");//("http://10.0.2.2:3000/inventories.json");
        new HttpAsyncTask().execute("http://10.0.2.2:3000/inventories.json");
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
            ArrayList<String> myStringArray1 = new ArrayList<String>();
            myStringArray1.add("something");
           // LinearLayout ll1=(LinearLayout)findViewById(R.id.ll1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReceiveFromServerActivity.this,R.layout.simplrow,myStringArray1);
            myListView.setAdapter(adapter);


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
                    adapter.add( s );
                    inventories.put(s,0);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }


            myListView.setAdapter( adapter );
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> listView, View itemView, int position, long itemId)
                {
                    Toast.makeText(getApplicationContext(),
                            "Click ListItem Number " + position, Toast.LENGTH_LONG)
                            .show();
                    Intent i=new Intent(ReceiveFromServerActivity.this, Imageupload.class);
                    String name = (String) listView.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(),
                            name + position, Toast.LENGTH_LONG)
                            .show();
                    System.out.println(name);
                    counter++;
                    i.putExtra("name",name);
                    startActivity(i);
                }
            });

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
}

