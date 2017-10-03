package com.example.usuario.integrationmaps;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 14/09/2017.
 */

public final class Listitem{

    private String id, nombre, direccion,precio_medio, tipo, valoracion, imageURL, longi, lati;

    public Listitem(String id, String nombre, String direccion, String precio_medio, String tipo, String valoracion, String imageURL, String longi, String lati) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.precio_medio = precio_medio;
        this.tipo = tipo;
        this.valoracion = valoracion;
        this.imageURL = imageURL;
        this.longi = longi;
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPrecio_medio() {
        return precio_medio;
    }

    public void setPrecio_medio(String precio_medio) {
        this.precio_medio = precio_medio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }


}
