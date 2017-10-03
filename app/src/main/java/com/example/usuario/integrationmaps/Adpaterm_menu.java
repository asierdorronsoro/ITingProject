package com.example.usuario.integrationmaps;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import java.util.List;


/**
 * Created by Usuario on 16/09/2017.
 */

public class Adpaterm_menu extends RecyclerView.Adapter<Adpaterm_menu.ViewHolder> {

    private List<Entradamenu> listItem;
    private Context context;

    public Adpaterm_menu(List<Entradamenu> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
        System.out.println(listItem.size()+"?????????????????");
    }

    //Este metodo es llamado cada vez que se genera una instancia de clase de Abajo


    @Override
    public Adpaterm_menu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.componente_carta, parent,false);

        return new Adpaterm_menu.ViewHolder(v);
    }


    //Este segundo método se llama una vez el anterior ha sido llamado. Enseña el dato al recyclerviw
    @Override
    public void onBindViewHolder(Adpaterm_menu.ViewHolder holder, int position) {
System.out.println("ppppppppppppppppppppppppp" + position);
        final Entradamenu listitem = listItem.get(position);
        if(position == 0){
            holder.titu.setText("Primeros");
            holder.platera.setText(listitem.getNombreplato());
        }else if (position == 1){
            holder.titu.setText("Segundos");
            holder.platera.setText(listitem.getTipo());
        }else if(position == 2){
            holder.titu.setText("Postres");
            holder.platera.setText(listitem.getPrecio());
        }
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public EditText titu;
        public TextView platera;


        public ViewHolder(View itemView) {

            super(itemView);
            System.out.println("00000000000000000000");

            platera = (TextView) itemView.findViewById(R.id.plato3);
            titu = (EditText)itemView.findViewById(R.id.titulo3);

        }
    }

}
