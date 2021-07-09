package com.example.popinclient;

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
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

    InventoryEventTicketAdapter adapter;
    RecyclerView recyclerView;
    BottomNavigationView bottom_view;
    private static final String BASE_URL = "https://popin.co.ke/Mobile/Client/";
    String active_user_id;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;
    public SearchView searchView;

    List<eventData> list;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_recycler_view);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle(null);

        list = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.events_recycler);
        adapter = new InventoryEventTicketAdapter(list, getApplication());

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

            volleyJsonObjectRequest(BASE_URL+"show_all_events.php");
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
                    volleyJsonObjectRequest(BASE_URL+"show_all_events.php");
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

        bottom_view = (BottomNavigationView)findViewById(R.id.event_navigation);
        botton_nav_clicked();

        searchView =(SearchView)findViewById(R.id.search_bar); //(SearchView) searchItem.getActionView();
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
        });

        //if back button is pressed
        ImageButton back_btn = (ImageButton)findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }*/

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

            list.add(new eventData(jObject.getString("event_name"),jObject.getString("event_start_date"), jObject.getString("event_stop_date"),
                    jObject.getString("event_start_time"),jObject.getString("event_stop_time"),jObject.getString("event_venue"),
                    jObject.getString("event_poster"),jObject.getString("event_id")));
        }

    }

    public void volleyJsonObjectRequest(String url) throws JSONException {

        String  REQUEST_TAG = "popin.co.ke";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Showing Events...");
        progressDialog.show();

        StringRequest jsonObjectReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {

                           // Toast.makeText(ReserveActivity.this,response,Toast.LENGTH_LONG).show();

                            load_recycler(response);

                        } catch(JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {

                    volleyJsonObjectRequest(BASE_URL+"show_all_events.php");
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

                    case R.id.navigation_events:
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);

                        break;

                    case R.id.navigation_reserves:
                        //Intent i2 = new Intent(getApplicationContext(), ReserveActivity.class);
                        //startActivity(i2);

                        break;

                    case R.id.navigation_home :
                        Intent i3 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i3);

                        break;

                }

                return true;

            }
        });
    }
}
