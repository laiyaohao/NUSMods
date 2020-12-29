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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


public class ModuleActivity extends AppCompatActivity {
    private String moduleCode;
    private String title;
    private int[] semesters;
    private TextView moduleCodeTextView;
    private TextView moduleTitleTextView;
    private TextView moduleSemestersTextView;
    private TextView moduleDepFacCredTextView;
    private TextView moduleDescriptionTextView;
    private TextView modulePreCoReqPrecluTextView;
    private String url;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        moduleCode = getIntent().getStringExtra("moduleCode");
        title = getIntent().getStringExtra("title");
        semesters = getIntent().getIntArrayExtra("semesters");
        url = "https://api.nusmods.com/v2/2020-2021/modules/" + moduleCode + ".json";

        moduleCodeTextView = findViewById(R.id.module_code);
        moduleCodeTextView.setText(moduleCode);

        moduleTitleTextView = findViewById(R.id.module_title);
        moduleTitleTextView.setText(title);

        moduleSemestersTextView = findViewById(R.id.module_semesters);
        StringBuilder semesterString = new StringBuilder();
        for (int semester : semesters) {
            if (semester != 0) {
                semesterString.append("Semester ").append(Integer.toString(semester)).append(" ");
            }
        }
        moduleSemestersTextView.setText(semesterString.toString());

        moduleDepFacCredTextView = findViewById(R.id.module_department_faculty_credit);

        modulePreCoReqPrecluTextView = findViewById(R.id.module_precoreqpreclusion);

        moduleDescriptionTextView = findViewById(R.id.module_description);



        loadModuleInfo();
    }
    public void loadModuleInfo() {
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
                try {
                    // look into the JSONObject, look into a key called results and parse it into a JSONArray
                    // try catch block comes into play
                    String depFacCred = response.getString("department") + " | "
                            + response.getString("faculty") + " | "
                            + response.getString("moduleCredit") + " MCs";
                    String description = "Module Description:\n" + "\n" + response.getString("description");
                    moduleDepFacCredTextView.setText(depFacCred);
                    moduleDescriptionTextView.setText(description);
                    StringBuilder preCoReqPrecluText = new StringBuilder();
                    preCoReqPrecluText.append("Pre-Requisite(s):\n");
                    if (response.has("prerequisite")) {
                        // module has pre-requisites
                        preCoReqPrecluText.append("\n");
                        preCoReqPrecluText.append(response.getString("prerequisite"));
                        preCoReqPrecluText.append("\n");
                    }
                    preCoReqPrecluText.append("\nPreclusion(s):\n");
                    if (response.has("preclusion")) {
                        // module has pre-requisites
                        preCoReqPrecluText.append("\n");
                        preCoReqPrecluText.append(response.getString("preclusion"));
                        preCoReqPrecluText.append("\n");
                    }
                    preCoReqPrecluText.append("\nCo-Requisite(s):\n");
                    if (response.has("corequisite")) {
                        // module has pre-requisites
                        preCoReqPrecluText.append("\n");
                        preCoReqPrecluText.append(response.getString("preclusion"));
                        preCoReqPrecluText.append("\n");
                    }
                    modulePreCoReqPrecluTextView.setText(preCoReqPrecluText.toString());
                } catch (JSONException e) {
                    Log.e("module_info", "json error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("module_info","module information error");
            }
        });
        requestQueue.add(request);
    }

}