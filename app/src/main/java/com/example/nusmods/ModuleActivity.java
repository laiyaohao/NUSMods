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
    private TextView moduleAddInfoTextView;
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

        moduleAddInfoTextView = findViewById(R.id.module_addinfo);

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
                // look into the JSONObject, look into a key called results and parse it into a JSONArray
                // try catch block comes into play
                loadDepFacCred(response);
                loadDescription(response);
                loadPreCoReqPreclu(response);
                loadAddInfo(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("module_info","module information error");
            }
        });
        requestQueue.add(request);
    }

    public void loadDepFacCred(JSONObject response) {
        try {
            String depFacCred = response.getString("department") + " | "
                    + response.getString("faculty") + " | "
                    + response.getString("moduleCredit") + " MCs";
            moduleDepFacCredTextView.setText(depFacCred);
        } catch (JSONException e) {
            Log.e("module_depFacCred","depFacCred error");
        }

    }

    public void loadDescription(JSONObject response) {
        try {
            String description = "Module Description:\n" + "\n" + response.getString("description");
            moduleDescriptionTextView.setText(description);
        } catch (JSONException e) {
            Log.e("module_description","description error");
        }
    }

    public void loadPreCoReqPreclu(JSONObject response) {
        try {
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
                // module has preclusions
                preCoReqPrecluText.append("\n");
                preCoReqPrecluText.append(response.getString("preclusion"));
                preCoReqPrecluText.append("\n");
            }
            preCoReqPrecluText.append("\nCo-Requisite(s):\n");
            if (response.has("corequisite")) {
                // module has co-requisites
                preCoReqPrecluText.append("\n");
                preCoReqPrecluText.append(response.getString("preclusion"));
                preCoReqPrecluText.append("\n");
            }
            modulePreCoReqPrecluTextView.setText(preCoReqPrecluText.toString());
        } catch (JSONException e) {
            Log.e("module_PreCoReqPreclu","PreCoReqPreclu error");
        }
    }

    public void loadAddInfo(JSONObject response) {
        StringBuilder addInfoText = new StringBuilder();
        addInfoText.append("Additional Information:\n");
        if (response.has("attributes")) {
            try {
                JSONObject attributes = response.getJSONObject("attributes");
                if (attributes.has("su")) {
                    addInfoText.append("\nHas S/U option for Undergraduate students only\n");
                }
                if (attributes.has("grsu")) {
                    addInfoText.append("\nHas S/U option for Graduate students only\n");
                }
                if (attributes.has("lab")) {
                    addInfoText.append("\nLab based module\n");
                }
                if (attributes.has("year")) {
                    addInfoText.append("\nYear Long module\n");
                }
                if (attributes.has("fyp")) {
                    addInfoText.append("\nHonours / Final Year Project\n");
                }
                if (attributes.has("ism")) {
                    addInfoText.append("\nIndependent study module\n");
                }
                if (attributes.has("urop")) {
                    addInfoText.append("\nUndergraduate Research Opportunities Program\n");
                }
                if (attributes.has("ssgf")) {
                    addInfoText.append("\nSkillsFuture funded\n");
                }
                if (attributes.has("sfs")) {
                    addInfoText.append("\nSkillsFuture series\n");
                }
            }
            catch (JSONException e) {
                Log.e("module_AddInfo","AddInfo error");
            }

        }
        moduleAddInfoTextView.setText(addInfoText.toString());
    }
}