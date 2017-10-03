package com.example.usuario.integrationmaps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SliderActivity extends AppCompatActivity {

    ViewPager viewPager;
    ScrrenshotAdapter adapter;
    private static final String URL_DATA = "http://iting.es/php/load_menu.php";
    private RecyclerView recyclerView;
    private List<Entradamenu> listItems;
    private List<Entradamenu> segundos;
    private List<Entradamenu> postres;

    String restaurante_id, restaurante_nombre, restaurante_direccion, restaurante_tipo, restaurante_precio_medio, restaurante_valoracion, restaurante_longi, restaurante_lati;
    AlertDialog.Builder builder;
    Listitem restauran;

    //-------------------------------------------------------
    EditText titulo1, titulo2, titulo3;
    TextView plato1, plato2, plato3;
    //------------------------------------------------------
    EditText nombre, valoracion;
    TextView tipo, precio, direccion;

    private LinearLayout parentLinearLayout;

    public SliderActivity(Context context) {


    }
    public SliderActivity(){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        restaurante_id = bundle.getString("restaurante_id");
        restaurante_nombre = bundle.getString("nombre");
        restaurante_direccion = bundle.getString("direccion");
        restaurante_tipo = bundle.getString("tipo");
        restaurante_precio_medio = bundle.getString("precio_medio");
        restaurante_valoracion = bundle.getString("valoracion");
        restaurante_longi = bundle.getString("longi");
        restaurante_lati = bundle.getString("lati");

        restauran = new Listitem(restaurante_id, restaurante_nombre, restaurante_direccion, restaurante_precio_medio,restaurante_tipo, restaurante_valoracion,"", restaurante_longi, restaurante_lati );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider1);

        builder = new AlertDialog.Builder(SliderActivity.this);

        viewPager = (ViewPager)findViewById(R.id.screenshot);
        adapter = new ScrrenshotAdapter(this, restaurante_id);
        viewPager.setAdapter(adapter);

        listItems = new ArrayList<>();
        segundos = new ArrayList<>();
        postres = new ArrayList<>();
        loadRecyclerViewData();

        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);


    }

    public void aniadir_conbucle(String nombre, List<Entradamenu> variable){
        System.out.println("VAMOOOOOOOOOOOOOOOOOOOOOOOOONO"+variable.size());

        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView1 = inflater1.inflate(R.layout.titulos, null);

        EditText z =(EditText) rowView1.findViewById(R.id.Seccion);
        z.setText(nombre);
        z.setFocusable(false);
        z.setClickable(false);

        parentLinearLayout.addView(rowView1, parentLinearLayout.getChildCount());

        for(int i=0;i<variable.size();i++){

            System.out.println(i);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.field, null);
            TextView a =(TextView) rowView.findViewById(R.id.platotitulo);
            a.setText(variable.get(i).getNombreplato());
            System.out.println(variable.get(i).getNombreplato());
            TextView b =(TextView) rowView.findViewById(R.id.platoprecio);
            b.setText(variable.get(i).getPrecio());
            // Add the new row before the add field button.
            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
        }
    }

    private void loadRecyclerViewData(){



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        rellenarcampos_rest();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            //String code = jsonObject.getString("code");
                            for(int i =0;i<jsonArray.length();i++) {
                                System.out.println(jsonArray.length());
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                System.out.println(jsonObject + "TIRAAAAAAAAAAAAAAAAAA");
                                Entradamenu entrada = new Entradamenu(
                                        jsonObject.getString("nombreplato"),
                                        jsonObject.getString("precio"),
                                        jsonObject.getString("tipoplato")
                                );

                                if(entrada.getTipo().equals("Primero")){
                                    listItems.add(entrada);
                                    System.out.println(entrada.getNombreplato()+"añadido");
                                }else if(entrada.getTipo().equals("Segundo")){
                                    segundos.add(entrada);
                                }else if(entrada.getTipo().equals("Postre")){
                                    postres.add(entrada);
                                }else{
                                    System.out.println("TIPO NO ASIGNADO!");
                                }

                            }

                            aniadir_conbucle("Primeros", listItems);
                            aniadir_conbucle("Segundo", segundos);
                            aniadir_conbucle("Postres", postres);

                        } catch (JSONException e) {
                                e.printStackTrace();
                            }


                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Algo ha ido mal", Toast.LENGTH_LONG).show();
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurante_id", restaurante_id );
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void displayAlert(String message){
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void rellenarcampos_rest(){

        nombre = (EditText)findViewById(R.id.nombre);
        nombre.setText(restauran.getNombre());
        valoracion = (EditText)findViewById(R.id.valoracion);
        valoracion.setText(restauran.getValoracion());

        direccion = (TextView)findViewById(R.id.direccion);
        direccion.setText(restauran.getDireccion());
        tipo = (TextView)findViewById(R.id.tipo);
        tipo.setText(restauran.getTipo());
        precio = (TextView)findViewById(R.id.importe);
        precio.setText(restauran.getPrecio_medio());

        nombre.setFocusable(false);
        nombre.setClickable(false);

        valoracion.setFocusable(false);
        valoracion.setClickable(false);



    }

    public void rellenarcampos_menu(){

        titulo1 = (EditText)findViewById(R.id.titulo1);
        titulo1.setText("Primeros");
        titulo2 = (EditText)findViewById(R.id.titulo2);
        titulo2.setText("Segundos");
        titulo3 = (EditText)findViewById(R.id.titulo4);
        titulo3.setText("Postres");

        plato1 = (TextView)findViewById(R.id.plato1);
        plato1.setText(listItems.get(0).getTipo());
        plato2 = (TextView)findViewById(R.id.plato2);
        plato2.setText(listItems.get(1).getNombreplato());
        plato3 = (TextView)findViewById(R.id.plato3);
        plato3.setText(listItems.get(2).getPrecio());


        titulo1.setFocusable(false);
        titulo1.setClickable(false);

        titulo2.setFocusable(false);
        titulo2.setClickable(false);

        titulo3.setFocusable(false);
        titulo3.setClickable(false);



    }



    }

