package com.example.nusmods;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder>{
    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout containerView;
        private TextView moduleCodeTextView;
        private TextView moduleTitleTextView;
        private TextView moduleSemestersTextView;
        private TextView moduleDepFacCredTextView;
        private TextView moduleDescriptionTextView;
        ModuleViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.module_information);
            moduleCodeTextView = view.findViewById(R.id.module_code);
            moduleTitleTextView = view.findViewById(R.id.module_title);
            moduleSemestersTextView = view.findViewById(R.id.module_semesters);
            moduleDepFacCredTextView = view.findViewById(R.id.module_department_faculty_credit);
            moduleDescriptionTextView = view.findViewById(R.id.module_description);
        }
    }
    private List<ModuleInformation> moduleInformation = new ArrayList<>();
    private RequestQueue requestQueue;

    ModuleAdapter(Context context, String moduleCode, String title, int[] semesters) {
        requestQueue = Volley.newRequestQueue(context);
//        this.context = context;

        loadModuleInfo(moduleCode,title,semesters);
    }

    public void loadModuleInfo(String moduleCode,String title, int[] semesters) {
        String url = "https://api.nusmods.com/v2/2020-2021/modules/" + moduleCode + ".json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String depFacCred = response.getString("department") + " | "
                            + response.getString("faculty") + " | "
                            + response.getString("moduleCredit") + " MCs";
                    String description = response.getString("description");
                    moduleInformation.add(new ModuleInformation(moduleCode,title,semesters,depFacCred,description));
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
    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.module_information,parent,false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleAdapter.ModuleViewHolder holder, int position) {
        ModuleInformation current = moduleInformation.get(position);
        holder.moduleCodeTextView.setText(current.getModuleCode());
        holder.moduleTitleTextView.setText(current.getModuleTitle());
        holder.moduleSemestersTextView.setText(current.getModuleSemesters());
        holder.moduleDepFacCredTextView.setText(current.getModuleDepFacCred());
        holder.moduleDescriptionTextView.setText(current.getModuleDescription());
        holder.containerView.setTag(current);
    }

    @Override
    public int getItemCount() {
        return moduleInformation.size();
    }
}
