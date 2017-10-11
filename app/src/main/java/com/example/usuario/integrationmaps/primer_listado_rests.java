package com.example.usuario.integrationmaps;
import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nightonke.boommenu.BoomMenuButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;


public class primer_listado_rests extends AppCompatActivity implements  View.OnClickListener, BoomMenuButton.OnSubButtonClickListener, SearchView.OnQueryTextListener{

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.primera_vista_general);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        loadRecyclerViewData();
        initViews();


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //https://stackoverflow.com/questions/26486730/in-android-app-toolbar-settitle-method-has-no-effect-application-name-is-shown
        getSupportActionBar().setTitle("ITing");

        nombre_restaurantes = new ArrayList<>();
        ciudad_restaurantes = new ArrayList<>();
        platos_restaurantes = new ArrayList<>();

        conteo=0;
        conteo1=0;
        reziklatu = (RecyclerView)findViewById(R.id.recyclerview1);
        layoutManager = new LinearLayoutManager(this);
        reziklatu.setLayoutManager(layoutManager);
        reziklatu.setHasFixedSize(true);

        parentLinearLayout = (LinearLayout) findViewById(R.id.listado_busqueda);

    }

    @Override
    public void onClick(View view) {

    }

    private void initViews() {
        boomMenuButton = (BoomMenuButton)findViewById(R.id.boom);
        initBoom();
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
                        // addMarkers();

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

    public void clear() {
        int size = this.listItems.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.listItems.remove(0);
            }

            adapter.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_items, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);


        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(View arg0) {
                // search was detached/closed

                System.out.println("porque coño no pasaaaaaaaaaaas!!");
                ScrollView sv = (ScrollView)findViewById(R.id.scroll);
                sv.scrollTo(0, sv.getTop());
                LinearLayout a = (LinearLayout) findViewById(R.id.listado_busqueda);
                a.setVisibility(LinearLayout.GONE);

                LinearLayout b = (LinearLayout)findViewById(R.id.bigarren);
                b.setVisibility(View.VISIBLE);
                b.setFocusable(false);

                BoomMenuButton bo = (BoomMenuButton)findViewById(R.id.boom);
                bo.setVisibility(View.VISIBLE);





            }

            @Override
            public void onViewAttachedToWindow(View arg0) {


            }
        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                LinearLayout a = (LinearLayout)findViewById(R.id.bigarren);
                a.setVisibility(View.GONE);
                BoomMenuButton bo = (BoomMenuButton)findViewById(R.id.boom);
                bo.setVisibility(View.GONE);
                LinearLayout b = (LinearLayout) findViewById(R.id.listado_busqueda);
                b.setVisibility(View.VISIBLE);
                return true;
            default:
                return true;
        }
    }


    public void getObjetosTexto_iting(final String newText){
        String URL_DATA = "http:/iting.es/php/Filtrado.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i =0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(!nombre_restaurantes.contains(jsonObject.getString("nombre"))){
                                    nombre_restaurantes.add(jsonObject.getString("nombre"));
                                    Ciudad a = new Ciudad(jsonObject.getString("nombre"), "http://iting.es/icon_filter/rest.png");
                                    arrayList.add(a);
                                }
                                if(!ciudad_restaurantes.contains(jsonObject.getString("direccion"))){
                                    ciudad_restaurantes.add(jsonObject.getString("direccion"));
                                    Ciudad a = new Ciudad(jsonObject.getString("direccion"), "http://iting.es/icon_filter/loc.png");
                                    arrayList.add(a);
                                }
                            }
                            conteo=1;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        egokitu = new SearchAdapter(arrayList, getApplicationContext());
                        reziklatu.setAdapter(egokitu);

                        String texto;
                        texto = newText.toLowerCase();
                        ArrayList<Ciudad> newList = new ArrayList<>();
                        for(Ciudad ciudad : arrayList){
                            String nombre = ciudad.getMunicipio().toLowerCase();
                            if (nombre.contains(texto) && !newList.contains(ciudad)) {
                                newList.add(ciudad);
                            }

                        }
                        egokitu.setFilter(newList);


                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Algo ha ido mal", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getPlatos(final String newText){
        String URL_DATA = "http:/iting.es/php/Filtrado_platos.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i =0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if(!platos_restaurantes.contains(jsonObject.getString("nombre"))){
                                    platos_restaurantes.add(jsonObject.getString("nombre"));
                                    arrayList.add(new Ciudad(platos_restaurantes.get(i), "http://iting.es/icon_filter/meal.png"));
                                }


                            }
                            //conteo=2;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        egokitu = new SearchAdapter(arrayList, getApplicationContext());
                        reziklatu.setAdapter(egokitu);

                        String texto;
                        texto = newText.toLowerCase();
                        ArrayList<Ciudad> newList = new ArrayList<>();
                        if(texto.equals("")){
                            newList = new ArrayList<>();
                            arrayList = new ArrayList<>();
                            actualizar_arrays();
                            platos_restaurantes = new ArrayList<>();
                            conteo=0;
                        }else {
                            for (Ciudad ciudad : arrayList) {
                                String nombre = ciudad.getMunicipio().toLowerCase();
                                if (nombre.contains(texto) && !newList.contains(ciudad)) {
                                    newList.add(ciudad);
                                }
                            }
                        }
                        egokitu.setFilter(newList);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Algo ha ido mal", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void actualizar_arrays(){

        for(int i = 0;i<nombre_restaurantes.size();i++){
            arrayList.add( new Ciudad(nombre_restaurantes.get(i), "http://iting.es/icon_filter/rest.png"));
        }
        for(int i = 0;i<ciudad_restaurantes.size();i++){
            arrayList.add( new Ciudad(ciudad_restaurantes.get(i), "http://iting.es/icon_filter/loc.png"));
        }

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
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, "Has querido buscar por tu cuenta!!!", Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if(conteo==0) {
            getObjetosTexto_iting(newText);
        }
        if(conteo==1){
            getPlatos(newText);
        }
        return false;
    }
}



