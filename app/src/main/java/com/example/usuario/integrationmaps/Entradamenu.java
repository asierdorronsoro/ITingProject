package com.example.usuario.integrationmaps;

/**
 * Created by Usuario on 16/09/2017.
 */

public class Entradamenu {

    String nombreplato, precio, tipo;

    public Entradamenu( String nombreplato, String precio, String tipo) {

        this.nombreplato = nombreplato;
        this.precio = precio;
        this.tipo  = tipo;
    }

    public String getNombreplato() {
        return nombreplato;
    }

    public void setNombreplato(String nombreplato) {
        this.nombreplato = nombreplato;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
