package com.example.usuario.integrationmaps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Usuario on 04/10/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    ArrayList<Ciudad>arrayList = new ArrayList<>();
    private Context context;

    public SearchAdapter (ArrayList<Ciudad> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
        System.out.println("NO SE VE PUTOOO NADAA!!");
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.componete_fila_busqueda, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.texto.setText(arrayList.get(position).getMunicipio());

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });


        builder.build().load(arrayList.get(position).getIcono()).resize(100,100).centerCrop().into(holder.imagen);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Has Clickado"+arrayList.get(position).getMunicipio(), Toast.LENGTH_LONG).show();
                if(arrayList.get(position).getIcono().contains("loc")){
                    Toast.makeText(context, "Es un municipio", Toast.LENGTH_LONG).show();

                    Bundle b = new Bundle();
                    b.putString("restaurante_ciudad", arrayList.get(position).getMunicipio());
                    System.out.println(arrayList.get(position).getMunicipio()+"En el bundle");
                    Intent intent = new Intent();
                    intent.setClass(context, segundo_listado_rests.class);

                    intent.putExtras(b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }if(arrayList.get(position).getIcono().contains("meal")){
                    Toast.makeText(context, "Es una comida", Toast.LENGTH_LONG).show();
                }
                if(arrayList.get(position).getIcono().contains("rest")){
                    Toast.makeText(context, "Es un restaurante", Toast.LENGTH_LONG).show();

                    busqueda_restaurante_existente a = new busqueda_restaurante_existente(arrayList.get(position).getMunicipio(), context);


                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imagen;
        TextView texto;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            imagen = (ImageView)itemView.findViewById(R.id.argazki);
            texto = (TextView)itemView.findViewById(R.id.izena);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearlayout);
        }
    }
    //Modificado -- para searchview!!
    public void setFilter(ArrayList<Ciudad> newList){
        arrayList = new ArrayList<>();
        arrayList.addAll(newList);
        notifyDataSetChanged();
    }


}
