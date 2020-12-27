package com.example.nusmods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.SystemClock;


public class ModuleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String moduleCode;
    private String title;
    private int[] semesters;
//    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        moduleCode = getIntent().getStringExtra("moduleCode");
        title = getIntent().getStringExtra("title");
        semesters = getIntent().getIntArrayExtra("semesters");
        recyclerView = findViewById(R.id.recycler_view2);
//        requestQueue = Volley.newRequestQueue(getApplicationContext());
        adapter = new ModuleAdapter(getApplicationContext(),
                moduleCode,
                title,
                semesters);
        SystemClock.sleep(4000);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

    }

}