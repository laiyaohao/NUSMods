package com.example.nusmods;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class NUSModsAdapter extends RecyclerView.Adapter<NUSModsAdapter.NUSModsViewHolder> implements Filterable {
    // class represent the data in recycler view
    public static class NUSModsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout containerView;
        private TextView moduleCodeView;
        private TextView titleView;
        NUSModsViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.module_row);
            moduleCodeView = view.findViewById(R.id.module_row_module_code);
            titleView = view.findViewById(R.id.module_row_title);
            // findViewById takes in int, but androidStudio and gradle are automatically generating unique ids
            // and they put in in this class called R
            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Module current = (Module) containerView.getTag();
                    // containerView.getTag() refers to holder.containerView.setTag(current) in onBindViewHolder method
                    // however, containerView.getTag() returns an Object, so need to cast it to Module object
                    Intent intent = new Intent(v.getContext(),ModuleActivity.class);
                    intent.putExtra("moduleCode",current.getModuleCode());
                    intent.putExtra("title",current.getTitle());
                    intent.putExtra("semesters",current.getSemesters());
                    // creating an intent is basically saying heres an activity i want to go to heres some data i wanna pass
                    // to that activity
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    private List<Module> modules = new ArrayList<>();
    private List<Module> modulesFull = new ArrayList<>();
    private RequestQueue requestQueue;

    NUSModsAdapter(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        loadModule();
    }

    public void loadModule() {
        String url = "https://api.nusmods.com/v2/2020-2021/moduleList.json";
        // JsonArrayRequest class is from Volley, and it gets api from the web
        // special request that is handling json response
        // first parameter is the type of request
        // second parameter is the url to get from
        // third para is the info sent to the url
        // 4th para is the function that is to be called when the request finishes
        // put Response Listener class, which is a class from Volley
        // it extends out an anon class, which takes in a JSONObject as a parameter, because the return from
        // the url is a JSONObject
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // look into the JSONArray, look into a key called results and parse it into a JSONArray
                // try catch block comes into play
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject result = response.getJSONObject(i);
                        JSONArray resultSemesters = result.getJSONArray("semesters");

                        int[] semesters = new int[4];
                        for (int j = 0; j < resultSemesters.length();j++) {
                            semesters[j] = resultSemesters.getInt(j);
                        }
                        modules.add(new Module(
                                result.getString("moduleCode"),
                                result.getString("title"),
                                semesters
                        ));

                        modulesFull.add(new Module(
                                result.getString("moduleCode"),
                                result.getString("title"),
                                semesters
                        ));
                    }

                    notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("nusmods", "Json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("nusmods","module list error");
            }
        });

        requestQueue.add(request);
    }
    @NonNull
    @Override
    public NUSModsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.module_row,parent,false);
        return new NUSModsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NUSModsViewHolder holder, int position) {
        // set the properties of view
        // going from model to view
        Module current = modules.get(position);
        holder.moduleCodeView.setText(current.getModuleCode());
        holder.titleView.setText(current.getTitle());
        holder.containerView.setTag(current);
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }



    @Override
    public Filter getFilter() {
        return listfilter;
    }
    private Filter listfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Module> filteredModules = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredModules.addAll(modulesFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Module module : modulesFull) {
                    if (module.getModuleCode().toLowerCase().contains(filterPattern) ||
                            module.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredModules.add(module);
                    }


                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredModules;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modules.clear();
            modules.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
