package ruchajoshi.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView weatherNews;
    Button findWeather;


    public void findWeather(View view){

        InputMethodManager mgr= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);

        try {
            String encodedCityName= URLEncoder.encode(cityName.getText().toString(),"UTF-8");
            Downloadtask downloadtask= new Downloadtask();
            downloadtask.execute("https://samples.openweathermap.org/data/2.5/weather?q="+encodedCityName);
            Toast.makeText(getApplicationContext(),""+encodedCityName,Toast.LENGTH_LONG);

        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
        }


    }


    public class Downloadtask extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... strings) {

            String result= "";
            URL url;
            HttpURLConnection httpURLConnection=null;


            try {
                url=new URL(strings[0]);

                httpURLConnection= (HttpURLConnection) url.openConnection();

                InputStream in= httpURLConnection.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);

                int data =reader.read();

                while(data !=-1){

                    char current= (char) data;
                    result +=current;
                    data=reader.read();

                }
                return  result;


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
            }


            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                String message = " ";

                JSONObject jsonObject= new JSONObject(result);

                String weatherInfo= jsonObject.getString("weather");
                Log.i("weather Content", weatherInfo);


                JSONArray array= new JSONArray(weatherInfo);

                for(int i=0; i< array.length();i++)
                {

                    JSONObject jsonObj= array.getJSONObject(i);
                    String main="";
                    String description="";

                    main=jsonObj.getString("main");
                    description=jsonObj.getString("description");

                    if(main!= "" && description!=""){

                        message += main + ":" +description + " ";

                    }

                   // Log.i("Main", jsonObj.getString("main"));
                   // Log.i("Description", jsonObj.getString("description"));

                }


                if(message != ""){

                    weatherNews.setText(message);

                }
                else{
                    Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);


                }




            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_LONG);
            }




        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName= findViewById(R.id.cityName);
        weatherNews=findViewById(R.id.weatherNews);
        findWeather=findViewById(R.id.findWeather);
    }



}
