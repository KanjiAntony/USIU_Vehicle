package com.example.usiuparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.example.usiuparking.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    private EditText email_address;
    private EditText user_password;
    private Button login_button;
    private TextView register_page;
    private ProgressDialog pBar;
    private static final String STRING_EMPTY = "";
    private static final String KEY_EMAIL_ADDRESS = "loginEmail";
    private static final String KEY_USER_PASSWORD = "loginPass";
    private String UserEmail;
    private String Password;
    private int success;
    private String UserID;
    private static final String BASE_URL = "https://carparking.twigahillfarm.co.ke/Mobile/";
    private SessionHandler session;

    private Spinner slotBuildingSpinner, abilitySpinner, locationSpinner;

    public ArrayAdapter<String> spinnerArrayAdapter, spinnerArrayAdapter2;
    public List<String> abilityList, locationList;
    public String UserAbilityStatus, ParkingLocation;

    private RequestQueue mRequestQueue;

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        session = new SessionHandler(getApplicationContext());

        email_address = (EditText) findViewById(R.id.txt_login_email);
        user_password = (EditText) findViewById(R.id.txt_login_pass);
        login_button = (Button) findViewById(R.id.btn_login);


        //Abilities spinner
            String[] abilitites = new String[]{
                "Normal","Disabled"
            };

            abilitySpinner = (Spinner) findViewById(R.id.ability_status_spinner);

            abilityList = new ArrayList<>(Arrays.asList(abilitites));
            // Initializing an ArrayAdapter
            spinnerArrayAdapter = new ArrayAdapter<String>(
                    this,R.layout.ability_spinner_item, abilityList);

            spinnerArrayAdapter.setDropDownViewResource(R.layout.ability_spinner_item);
            abilitySpinner.setAdapter(spinnerArrayAdapter);

        // abilities spinner

        String[] parking_location = new String[]{

        };

        locationSpinner = (Spinner) findViewById(R.id.parking_location_spinner);

        locationList = new ArrayList<>(Arrays.asList(parking_location));
        // Initializing an ArrayAdapter
        spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this,R.layout.ability_spinner_item, locationList);

        spinnerArrayAdapter2.setDropDownViewResource(R.layout.ability_spinner_item);
        locationSpinner.setAdapter(spinnerArrayAdapter2);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    login();
                } else {
                    Toast.makeText(LoginPage.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //automatic process
        try {

            volleyFetchCompaniesRequest(BASE_URL + "show_all_parking_locations.php");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void login()
    {

        if(!STRING_EMPTY.equals(email_address.getText().toString()) && !STRING_EMPTY.equals(user_password.getText().toString())) {
            UserEmail = email_address.getText().toString();
            Password = user_password.getText().toString();
            UserAbilityStatus = abilitySpinner.getSelectedItem().toString();
            ParkingLocation = locationSpinner.getSelectedItem().toString();
            volleyJsonObjectRequest(BASE_URL+"login.php");
        } else {
            Toast.makeText(LoginPage.this,"One of field is empty",Toast.LENGTH_LONG).show();
        }

    }

    public void load_dashboard()
    {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();

        Toast.makeText(LoginPage.this,"Welcome",Toast.LENGTH_LONG).show();
    }

    public void volleyJsonObjectRequest(String url){

        String  REQUEST_TAG = "USIU_Parking";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait as we log you in...");
        progressDialog.show();

        StringRequest jsonObjectReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(LoginPage.this,response,Toast.LENGTH_LONG).show();

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = (JSONObject)jsonArray.get(0);

                            success = Integer.parseInt(jsonObject.getString("success"));
                            UserID = jsonObject.getString("user_id");


                            if(success == 1) {
                                //set session
                                session.login(UserID,UserAbilityStatus, ParkingLocation);
                                load_dashboard();
                            } else {
                                Toast.makeText(LoginPage.this,"Failed to log you in.",Toast.LENGTH_LONG).show();
                            }

                        } catch(JSONException e) {
                            e.getMessage();
                        }


                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                progressDialog.hide();
            }
        }){

            @Override
            protected Map<String,String> getParams() {
                Map<String,String> httpParams = new HashMap<>();
                httpParams.put(KEY_EMAIL_ADDRESS,UserEmail);
                httpParams.put(KEY_USER_PASSWORD,Password);
                return httpParams;
            }

        };

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }

    public void volleyFetchCompaniesRequest(String url) throws JSONException {

        String  REQUEST_TAG = "USIU_Parking_Locations";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading parking locations...");
        progressDialog.show();

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(),1024*1024*5); // 5MB cache size

        // Setup the network to use HttpURLConnection as the HTTP client
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with cache and network
        mRequestQueue = new RequestQueue(cache,network);

        // Start the RequestQueue
        mRequestQueue.start();

        StringRequest jsonObjectReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        progressDialog.hide();

                        try {

                            //Toast.makeText(employeeRegister.this,response,Toast.LENGTH_LONG).show();

                            JSONArray jsonArray = new JSONArray(response);


                                for(int i=0; i< jsonArray.length(); i++) {
                                    JSONObject jObject2 = (JSONObject) jsonArray.get(i);
                                    locationList.add(jObject2.getString("parking_location"));
                                    spinnerArrayAdapter2.notifyDataSetChanged();
                                }


                        } catch(JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                progressDialog.hide();
            }
        });

        // Adding JsonObject request to request queue
        mRequestQueue.add(jsonObjectReq);

    }


}