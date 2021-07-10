package com.example.usiuparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.usiuparking.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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


    }

    public void login()
    {

        if(!STRING_EMPTY.equals(email_address.getText().toString()) && !STRING_EMPTY.equals(user_password.getText().toString())) {
            UserEmail = email_address.getText().toString();
            Password = user_password.getText().toString();
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
                                session.login(UserID);
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

}