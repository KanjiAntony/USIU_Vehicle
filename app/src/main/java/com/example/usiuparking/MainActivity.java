package com.example.usiuparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    InventorySlotsAdapter adapter;
    RecyclerView recyclerView;
    BottomNavigationView bottom_view;
    private static final String BASE_URL = "https://carparking.twigahillfarm.co.ke/Mobile/";
    String active_user_id;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;
    public SearchView searchView;

    List<slotData> list;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_recycler_view);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle(null);

        list = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.slots_recycler);
        adapter = new InventorySlotsAdapter(list, getApplication());

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

        } else {
            // Permission has already been granted
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        //requestQueue = Volley.newRequestQueue(this); // 'this' is the Context

        try {

            volleyJsonObjectRequest(BASE_URL+"show_all_slots.php");
        } catch(JSONException e) {
            e.printStackTrace();
        }

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                try {
                    adapter.clear();
                    volleyJsonObjectRequest(BASE_URL+"show_all_slots.php");
                    swipeContainer.setRefreshing(false);
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        bottom_view = (BottomNavigationView)findViewById(R.id.slots_navigation);
        botton_nav_clicked();

/*        searchView =(SearchView)findViewById(R.id.search_bar); //(SearchView) searchItem.getActionView();
       // searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });*/


    }

    public void load_recycler(String json) throws JSONException
    {
        JSONArray jsonArray = new JSONArray(json);
        //list = getData();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        for(int i=0; i<jsonArray.length();i++) {
            JSONObject jObject = (JSONObject)jsonArray.get(i);

            if(jObject.getInt("success") == 1) {
                progressDialog.hide();
            } else {
                progressDialog.show();
            }

            list.add(new slotData(jObject.getString("slot_name"),jObject.getString("slot_availability"), jObject.getString("slot_id")));
        }

    }

    public void volleyJsonObjectRequest(String url) throws JSONException {

        String  REQUEST_TAG = "USIU_Parking_Slots";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Showing parking slots...");
        progressDialog.show();

        StringRequest jsonObjectReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {

                            load_recycler(response);

                        } catch(JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {

                    volleyJsonObjectRequest(BASE_URL+"show_all_slots.php");
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                VolleyLog.d("Error: " + error.getMessage());
                progressDialog.hide();
            }
        });

        // Adding JsonObject request to request queue
          //  requestQueue.add(jsonObjectReq);
       AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }

    public void botton_nav_clicked() {

        bottom_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.navigation_home:
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);

                        break;


                }

                return true;

            }
        });
    }
}
