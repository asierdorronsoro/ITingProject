package com.example.usuario.integrationmaps;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, BoomMenuButton.OnSubButtonClickListener
{

   private GoogleMap mMap;
    Button bmapa;
    Button bterreno;
    Button bhibrido;
    Button binteriores;
    //---------------------
    private static final String URL_DATA = "http://iting.es/php/volley.php";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Listitem> listItems;

    TextView distancia;

    private BoomMenuButton boomMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        //--------------------------


        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        loadRecyclerViewData();

     /*  distancia = (TextView)findViewById(R.id.distancia);

        Localizacion a = new Localizacion(getApplicationContext());


        double longitud = a.getLongitud();
        double latitud = a.getLatitud();

        distancia.setText("Longitud: " + String.valueOf(longitud) + " Latitud: "+ String.valueOf(latitud));*/

     //Boommenubutton
        initViews();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

       /* codigo que antes había mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng asador = new LatLng(43.269818, -2.023312);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(asador));
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.0f ) );*/



        /*mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.avion)).anchor(0.0f, 1.0f).position(latLng));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Toast.makeText(getApplicationContext(), "Has pulsado una marca", Toast.LENGTH_LONG).show();
                //Tambien se podría poner MapsActivity.this en vez de la función!!

                return false;
            }
        });*/
    }

    public void addMarkers(){
        LatLng sydney = new LatLng(-33.86997, 151.2089);
        LatLng asador = new LatLng(43.269818, -2.023312);
        LatLng oianume = new LatLng(43.237351, -1.996456);

        double media_long =0;
        double media_lat=0 ;


        ArrayList<LatLng> loca = new ArrayList<>();
        for(int i =0;i<listItems.size();i++){
            loca.add(new LatLng(Double.parseDouble(listItems.get(i).getLati()), Double.parseDouble(listItems.get(i).getLongi())));
            mMap.addMarker(new MarkerOptions().position(loca.get(i)).title(listItems.get(i).getNombre()));
            media_lat=media_lat+ Double.parseDouble(listItems.get(i).getLati());
            media_long=media_long+ Double.parseDouble(listItems.get(i).getLongi());
        }

        media_lat=media_lat/(listItems.size());
        System.out.println(media_lat);
        media_long=media_long/(listItems.size());
        System.out.println(media_long);

        LatLng med = new LatLng(media_lat, media_long);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(med));
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 8.0f ) );
    }

    @Override
    public void onClick(View view) {

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

    private void initViews() {
        boomMenuButton = (BoomMenuButton)findViewById(R.id.boom);
        initBoom();

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
}
