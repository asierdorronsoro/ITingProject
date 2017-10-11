package com.example.usuario.integrationmaps;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   private static final String URL_DATA = "http://iting.es/php/volley.php";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Listitem> listItems;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        loadRecyclerViewData();




    }
    private void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URL_DATA, null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();
                int count=0;
                while(count<response.length()){
                    try {
                        JSONObject jsonObject = response.getJSONObject(count);
                        Listitem item =  new Listitem(
                                jsonObject.getString("id"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("direccion"),
                                jsonObject.getString("precio_medio"),
                                jsonObject.getString("tipo"),
                                jsonObject.getString("valoracion"),
                                jsonObject.getString("imagen"),
                                jsonObject.getString("longi"),
                                jsonObject.getString("lat")
                        );
                        listItems.add(item);
                        count++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new MyAdapter(listItems, getApplicationContext());
                recyclerView.setAdapter(adapter);

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Algo ha ido mal", Toast.LENGTH_LONG).show();
                error.printStackTrace();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }
}
