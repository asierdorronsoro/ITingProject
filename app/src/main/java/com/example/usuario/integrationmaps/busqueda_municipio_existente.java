package com.example.usuario.integrationmaps;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Usuario on 06/10/2017.
 */

public class busqueda_municipio_existente {


    String nombre_municipio;
    Context context;
    private List<Listitem> listItems;

    public busqueda_municipio_existente(String nombre, Context context){
        this.nombre_municipio = nombre;
        this.context= context;
        listItems = new ArrayList<>();
        busqueda_por_municipio(nombre_municipio);
    }

    public void busqueda_por_municipio(final String nombre_municipio){

        String URL_DATA = "http://iting.es/php/filtrado_rest.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i =0;i<jsonArray.length();i++) {
                                System.out.println(jsonArray.length());
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Algo ha ido mal", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ciudad_nombre", nombre_municipio );
                return params;
            }
        };

        Mysingleton.getnInstance(context).addToRequestQue(stringRequest);

    }
}
