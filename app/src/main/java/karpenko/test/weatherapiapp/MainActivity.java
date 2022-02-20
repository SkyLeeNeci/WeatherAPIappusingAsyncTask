package karpenko.test.weatherapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText shirota, dolgota;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shirota = findViewById(R.id.editTextS);
        dolgota = findViewById(R.id.editTextD);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);


        button.setOnClickListener(view -> {
            double s = Double.parseDouble(shirota.getText().toString());
            double d = Double.parseDouble(dolgota.getText().toString());

            new WeatherTask(s,d).execute();
        });
    }

    private class WeatherTask extends AsyncTask<Void,Void,String>{

        private double s;
        private double d;
        private static final String KEY = "1f0e85761a20a77fe46d51594ca392bd" ;

        public WeatherTask(double s, double d){
            this.d = d;
            this.s = s;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String content = getConnect("https://api.openweathermap.org/data/2.5/weather?lat=" + s +"&lon=" + d +"&appid="+ KEY);
            return content;
        }

        @Override
        protected void onPostExecute(String content) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                double temp = jsonObject.getJSONObject("main").getDouble("temp");
                textView.setText("Температура : " + temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private String getConnect(String path){
            try {
                URL url = new URL(path);
                HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(2000);
                c.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                String content = "";
                String line = "";
                while ((line = reader.readLine()) != null){
                    content +=line + "\n";
                }
                return  content;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  "";
        }

    }

}