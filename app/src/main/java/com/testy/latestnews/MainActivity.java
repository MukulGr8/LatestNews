package com.testy.latestnews;

        import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    EditText editText;
    TextView textView;
    String weatherOk,title;
    List<String> list = new ArrayList<String>();
    ListView simpleList;
    ProgressBar progressBar;
    ArrayList<Item> imageList=new ArrayList<>();
    ProgressDialog dialog;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    public class DownlaodTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            alertDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Done Bro",Toast.LENGTH_LONG).show();
            for (int i=0; i<list.size(); i++) {
                System.out.println(list.get(i));
                weatherOk = list.get(i);
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.text2);
        simpleList = (ListView) findViewById(R.id.simpleListView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Loading.. Please wait...");
        alertDialog = builder.create();
        alertDialog.show();

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
           .cacheInMemory(true)
                .cacheOnDisk(true)
           .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
           .defaultDisplayImageOptions(defaultOptions)
           .build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        DownlaodTask task = new DownlaodTask();
        String result = null;
        try {
            //Encoding The Big URL like Sans Fransico Which HAve Space In Between Them

            result = task.execute("https://newsapi.org/v2/top-headlines?country=us&apiKey=67dfd152c97349e0b2a7228deea96063").get();
            //Creating New Json Object And Store The Value Of Result to jsonObject
            JSONObject jsonObject = new JSONObject(result);
            //Find The Weather String in jsonOBject and Store
            String weatherInfo = jsonObject.getString("articles");
            //Creating A Array To Loop Through The All The Json Values Which weatherInfo String has in it
            Log.i("Content Found", weatherInfo);
            JSONArray arr = new JSONArray(weatherInfo);;
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonobject = arr.getJSONObject(i);
                weatherInfo = jsonobject.getString("urlToImage");
                title = jsonobject.getString("title");
                list.add(weatherInfo);
                imageList.add(new Item(weatherInfo,title));
                MyAdapter myAdapter=new MyAdapter(this,R.layout.list_view_items,imageList);
                simpleList.setAdapter(myAdapter);
                simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(),String.valueOf(i+1), Toast.LENGTH_LONG).show();
                    }
                });
            }
           // textView.setText(weatherInfo);//Sowing The Weather
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}