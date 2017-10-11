package com.example.usuario.integrationmaps;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class segundo_listado_rests extends AppCompatActivity implements View.OnClickListener, BoomMenuButton.OnSubButtonClickListener{


    private static final String URL_DATA = "http://iting.es/php/volley.php";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Listitem> listItems;

    TextView distancia;

    private BoomMenuButton boomMenuButton;

    Picasso.Builder bildu;
    //Iconos

    ArrayList<String> nombre_restaurantes;
    ArrayList<String> ciudad_restaurantes;
    ArrayList<String> platos_restaurantes;

    Toolbar toolbar;

    RecyclerView reziklatu;
    SearchAdapter egokitu;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Ciudad> arrayList = new ArrayList<>();

    //
    int conteo;

    int conteo1;

    private LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();
        String parametro = bundle.getString("restaurante_ciudad");

        System.out.println("Lo recibo "+bundle.getString("restaurante_ciudad"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundo_listado_rests);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        busqueda_por_municipio(bundle.getString("restaurante_ciudad"));
        initViews();


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //https://stackoverflow.com/questions/26486730/in-android-app-toolbar-settitle-method-has-no-effect-application-name-is-shown
        getSupportActionBar().setTitle("ITing");


    }

    private void initViews() {
        boomMenuButton = (BoomMenuButton)findViewById(R.id.boom2);
        initBoom();
    }

    public void busqueda_por_municipio(final String nombre_municipio){

        String URL_DATA = "http://iting.es/php/filtrado_municipio.php";
        System.out.println(nombre_municipio +"Me ha llegado estoooo");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i =0;i<jsonArray.length();i++) {
                                System.out.println(jsonArray.length());
                                System.out.println(response+"**********");
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

                        adapter = new MyAdapter(listItems, getApplicationContext());
                        recyclerView.setAdapter(adapter);
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
                params.put("ciudad_nombre", nombre_municipio );
                return params;
            }
        };

        Mysingleton.getnInstance(this).addToRequestQue(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_items, menu);
        //MenuItem menuItem = menu.findItem(R.id.action_search);
        //final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        return true;
    }


    //Tema boomMenu!!!

    private void initBoom() {
        int number = 3;

        Drawable[] drawables = new Drawable[number];
        int[] drawablesResource = new int[]{
                R.drawable.distance,
                R.drawable.money,
                R.drawable.star
        };
        for (int i = 0; i < number; i++)
            drawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);

        String[] STRINGS = new String[]{
                "",
                "",
                ""
        };
        String[] strings = new String[number];
        for (int i = 0; i < number; i++)
            strings[i] = STRINGS[i];

        int[][] colors = new int[number][2];
        for (int i = 0; i < number; i++) {
            colors[i][1] = getRandomColor();
            colors[i][0] = Util.getInstance().getPressedColor(colors[i][1]);
        }

        // Now with Builder, you can init BMB more convenient
        new BoomMenuButton.Builder()
                .subButtons(drawables, colors, strings)
                .button(ButtonType.CIRCLE)
                .button(ButtonType.CIRCLE)
                .boom(BoomType.HORIZONTAL_THROW_2)
                .place(getPlaceType())
                .boomButtonShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .shareStyle(3f, getRandomColor(), getRandomColor())
                .onSubButtonClick(new BoomMenuButton.OnSubButtonClickListener() {
                    @Override
                    public void onClick(int buttonIndex) {
                        if (buttonIndex == 0) {
                            Toast.makeText(getApplicationContext(),"Ordenacion por direccion", Toast.LENGTH_SHORT).show();
                        } else if (buttonIndex == 1) {

                            Collections.sort(listItems, new Comparator<Listitem>() {
                                @Override
                                public int compare(Listitem t0, Listitem t1) {
                                    return Double.compare(Double.parseDouble(t0.getPrecio_medio()), Double.parseDouble(t1.getPrecio_medio()));
                                }
                            });
                            adapter.notifyDataSetChanged();

                        } else if (buttonIndex == 2) {
                            adapter.notifyDataSetChanged();

                            Collections.sort(listItems, new Comparator<Listitem>() {
                                @Override
                                public int compare(Listitem t0, Listitem t1) {
                                    return Double.compare(Double.parseDouble(t0.getValoracion()), Double.parseDouble(t1.getValoracion()));
                                }
                            });

                        }
                    }
                })

                .init(boomMenuButton);
    }

    private PlaceType getPlaceType() {
        return PlaceType.SHARE_3_3;
    }

    private String[] Colors = {
            "#F44336",
            "#E91E63",
            "#9C27B0",
            "#2196F3",
            "#03A9F4",
            "#00BCD4",
            "#009688",
            "#4CAF50",
            "#8BC34A",
            "#CDDC39",
            "#FFEB3B",
            "#FFC107",
            "#FF9800",
            "#FF5722",
            "#795548",
            "#9E9E9E",
            "#607D8B"};

    public int getRandomColor() {
        Random random = new Random();
        int p = random.nextInt(Colors.length);
        return Color.parseColor(Colors[p]);
    }


    @Override
    public void onClick(int buttonIndex) {

    }

    @Override
    public void onClick(View view) {

    }
}
