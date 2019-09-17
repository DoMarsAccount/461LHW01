package com.example.hw01weatherapp;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private GoogleMap googleMap;

    public void searchLocation(View view) {
        // Get the text view
        final TextView locationTextView = (TextView) findViewById(R.id.editText);

        // Get the value of the text view.
        String location = locationTextView.getText().toString();

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyBHw3QHkohfaXixBI1D1n3wSk5-i02ie98\n";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject results = (JSONObject) response.getJSONArray("results").get(0);
                    JSONObject geometry = (JSONObject) results.get("geometry");
                    JSONObject location = (JSONObject) geometry.get("location");

                    String latitude = location.get("lat").toString();
                    String longitude = location.get("lng").toString();

                    // Get the text view
                    TextView temperatureTextView = (TextView) findViewById(R.id.tempField);
                    temperatureTextView.setText(location.toString());


                    // Poll Dark Sky API with longitude and latitude
                    String weatherURL = "https://api.darksky.net/forecast/bf8803cc5df58912a929bc9f692d03d5/" + latitude + "," + longitude;

                    JsonObjectRequest weatherObjRequest = new JsonObjectRequest
                            (Request.Method.GET, weatherURL, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    // Get the text view
                                    TextView temperatureTextView = (TextView) findViewById(R.id.tempField);
                                    TextView precipTextView = (TextView) findViewById(R.id.textView5);
                                    TextView humidityTextView = (TextView) findViewById(R.id.humidityField);
                                    TextView windSpeedTextView = (TextView) findViewById(R.id.windSpeedField);

                                    try {

                                        JSONObject currently = (JSONObject) response.get("currently");

                                        temperatureTextView.setText("Temperature: " + currently.get("temperature").toString());
                                        precipTextView.setText("Precipitation: " + currently.get("precipProbability").toString());
                                        humidityTextView.setText("Humidity: " + currently.get("humidity").toString());
                                        windSpeedTextView.setText("Wind Speed: " + currently.get("windSpeed").toString());

                                    } catch(Exception e) {

                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error

                                }
                            });

                    queue.add(weatherObjRequest);

                } catch(Exception e) {

                }

                // Update MapView
                MapView mapView = (MapView) findViewById(R.id.mapView);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Update MapView
        MapView mapView = (MapView) findViewById(R.id.mapView);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
