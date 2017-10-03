package com.example.usuario.integrationmaps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Usuario on 14/09/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Listitem> listItem;
    private Context context;
    String devolver;
    String porfin;

    public MyAdapter(List<Listitem> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
        System.out.println(listItem.size()+"?????????????????");
    }

    //Este metodo es llamado cada vez que se genera una instancia de clase de Abajo
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent,false);

        return new ViewHolder(v);
    }

    //Este segundo método se llama una vez el anterior ha sido llamado. Enseña el dato al recyclerviw
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Listitem listitem = listItem.get(position);
        holder.nombre.setText(listitem.getNombre());
        holder.direccion.setText(listitem.getDireccion());
        holder.tipo.setText(listitem.getTipo());
        holder.valoracion.setText(listitem.getValoracion());
        holder.precio_medio.setText(listitem.getPrecio_medio());

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });

        System.out.println("VAMOOOOOOOOOOOOOOOOOOOS"+porfin);
        //cargarimagen(builder, listitem.getId(), holder);
        cargarimagen(builder, listitem.getId(), holder);


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Id: "+ listitem.getId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(context, SliderActivity.class);

                Bundle b = new Bundle();
                b.putString("restaurante_id", listitem.getId());
                b.putString("nombre", listitem.getNombre());
                b.putString("direccion", listitem.getDireccion());
                b.putString("tipo", listitem.getTipo());
                b.putString("valoracion", listitem.getValoracion());
                b.putString("precio_medio", listitem.getPrecio_medio());
                b.putString("longi", listitem.getLongi());
                b.putString("lati", listitem.getLati());

                intent.putExtras(b);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

      /*  Picasso.with(context)
        //.setLoggingEnabled(true);
                .load("http://localhost/img/".concat(listitem.getImageURL()))
       .into(holder.imageView, new Callback() {
           @Override
           public void onSuccess() {
               System.out.println("EXITOOOOOO");
           }

           @Override
           public void onError() {
               System.out.println("Esta claro que fallat");
           }
       });*/


    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView direccion, tipo;
        public EditText nombre, valoracion, precio_medio;
        public ImageView imageView;
        public LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            direccion = (TextView)itemView.findViewById(R.id.direccion);
            tipo = (TextView)itemView.findViewById(R.id.tipo);
            nombre = (EditText)itemView.findViewById(R.id.nombre);
            valoracion = (EditText)itemView.findViewById(R.id.valoracion);
            precio_medio = (EditText)itemView.findViewById(R.id.precio_medio);
            imageView = (ImageView)itemView.findViewById(R.id.imagen);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout);

            nombre.setFocusable(false);
            nombre.setClickable(false);

            valoracion.setFocusable(false);
            valoracion.setClickable(false);

            precio_medio.setFocusable(false);
            precio_medio.setClickable(false);
        }
    }

    public void cargarimagen(final Picasso.Builder builder, final String id, final ViewHolder holder){

        String login_url= "http://iting.es/php/imagen_portada.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            if(code.equals("Log in fallido")){

                               System.out.println("Log in fallido!!");
                            }else{
                                devolver = "http://iting.es/img/".concat(id+"/"+jsonObject.getString("nombre"));
                                builder.build().load(devolver).into(holder.imageView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_restaurante", id );
                System.out.println("pppppppppppppppp"+id);
                return params;
            }
        };
        Mysingleton.getnInstance(context).addToRequestQue(stringRequest);




    }

}
