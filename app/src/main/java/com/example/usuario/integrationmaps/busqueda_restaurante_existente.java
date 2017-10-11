package com.example.usuario.integrationmaps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Usuario on 06/10/2017.
 */

public class busqueda_restaurante_existente {

    String nombre_rest;
    Context context;
    Listitem item;

    public busqueda_restaurante_existente(String nombre, Context context){
        this.nombre_rest = nombre;
        this.context= context;
        busqueda_rest(nombre_rest);
    }

    public void busqueda_rest(final String nombre_rest){
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
                                item =  new Listitem(
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
                            }

                            empezar_newactividad();


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
                params.put("restaurante_nombre", nombre_rest );
                return params;
            }
        };

        Mysingleton.getnInstance(context).addToRequestQue(stringRequest);

    }

    public void empezar_newactividad(){
        Bundle b = new Bundle();
        b.putString("restaurante_id", item.getId());
        b.putString("nombre", item.getNombre());
        b.putString("direccion", item.getDireccion());
        b.putString("tipo", item.getTipo());
        b.putString("valoracion", item.getValoracion());
        b.putString("precio_medio", item.getPrecio_medio());
        b.putString("longi", item.getLongi());
        b.putString("lati", item.getLati());

        Intent intent = new Intent();
        intent.setClass(context, SliderActivity.class);

        intent.putExtras(b);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
