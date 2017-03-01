package com.app.user.effy;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.user.effy.Util.MySingleton;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class QuoteActivity extends AppCompatActivity {

    TextView quote_body;
    TextView quote_author;
    Context mcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext=this;
        setContentView(R.layout.activity_quote);
        quote_body=(TextView) findViewById(R.id.quote_body);
        quote_author=(TextView) findViewById(R.id.quote_author);
        new QuoteAsyncTaskLoader(mcontext).loadInBackground();


    }
    public  class QuoteAsyncTaskLoader extends AsyncTaskLoader
    {

        public QuoteAsyncTaskLoader(Context context) {
            super(context);
        }



        @Override
        public Void loadInBackground() {

            String url = "http://api.forismatic.com/api/1.0/?method=getQuote&lang=en&format=json";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.i("TAG", response.toString());
                            try
                            {
                                quote_body.setText(response.get("quoteText").toString());
                                quote_author.setText(response.get("quoteAuthor").toString());

                            } catch (Exception e)
                            {
                                Log.i("tag",e.toString());
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == 422) {
                                Log.i("TAG", networkResponse.headers.toString());

                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "text/html; charset=utf-8");
                    //  headers.put("User-agent", "My useragent");
                    return headers;
                }

            };
            MySingleton.getInstance(mcontext).addToRequestQueue(jsObjRequest);
        return  null;
        }




    }


}
