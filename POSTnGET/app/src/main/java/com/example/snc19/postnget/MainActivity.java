package com.example.snc19.postnget;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import MyVolley.JSONRequest;
import MyVolley.VolleyQueue;

/**
 * Created by snc19 on 1/10/17.
 */

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue; //sends request to the server
    private EditText editText;
    private Button get;
    private Button post;
    private String GET_URL = "http://ankitesh.pythonanywhere.com/getDetails/";
    private String POST_URL = "http://ankitesh.pythonanywhere.com/postDetails";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        queue = new VolleyQueue(this).getRequestQueue();
        editText = (EditText) findViewById(R.id.edit_text);
        get = (Button) findViewById(R.id.get);
        post = (Button) findViewById(R.id.post);
    }

    public void get_clicked(View view){
        //example url: http://ankitesh.pythonanywhere.com/getDetails/1
        String url = GET_URL + editText.getText().toString();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    Log.d("Got Response",response);
                }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Got Error",error.toString());
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,listener,errorListener);
        queue.add(stringRequest);
    }

    public void post_clicked(View view){
        String url = POST_URL;
        JSONObject postObject = new JSONObject();
        try {
            postObject.put("aadhar",editText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response",response.toString());
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error",error.getMessage().toString());
            }
        };
        JSONRequest request  = new JSONRequest(Request.Method.POST, url,
                postObject, responseListener,errorListener);
        queue.add(request);
    }


}

