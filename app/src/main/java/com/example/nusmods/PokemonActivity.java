package com.example.nusmods;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView numberTextView;
    private TextView type1TextView;
    private TextView type2TextView;
    private String url;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        url = getIntent().getStringExtra("url");

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        nameTextView = findViewById(R.id.pokemon_name);
        numberTextView = findViewById(R.id.pokemon_number);
        type1TextView = findViewById(R.id.pokemon_type1);
        type2TextView = findViewById(R.id.pokemon_type2);

        load();
    }

    public void load () {
        type1TextView.setText("");
        type2TextView.setText("");
        // JsonObjectRequest class is from Volley, and it gets api from the web
        // special request that is handling json response
        // first parameter is the type of request
        // second parameter is the url to get from
        // third para is the info sent to the url
        // 4th para is the function that is to be called when the request finishes
        // put Response Listener class, which is a class from Volley
        // it extends out an anon class, which takes in a JSONObject as a parameter, because the return from
        // the url is a JSONObject
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // look into the JSONObject, look into a key called results and parse it into a JSONArray
                // try catch block comes into play
                try {
                    nameTextView.setText(response.getString("name"));
                    numberTextView.setText(String.format("#%03d",response.getInt("id")));
                    JSONArray typeEntries = response.getJSONArray("types");
                    for (int i = 0;i<typeEntries.length();i++) {
                        JSONObject typeEntry = typeEntries.getJSONObject(i);
                        int slot = typeEntry.getInt("slot");
                        String type = typeEntry.getJSONObject("type").getString("name");

                        if (slot==1) {
                            type1TextView.setText(type);
                        }
                        else if (slot ==2) {
                            type2TextView.setText(type);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("CS50", "pokemon json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CS50","Pokemon details error");
            }
        });
        requestQueue.add(request);
    }
}