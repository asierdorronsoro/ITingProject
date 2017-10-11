package com.example.usuario.integrationmaps;

/**
 * Created by Usuario on 04/10/2017.
 */

public class Ciudad {

    String municipio;
    String icono;

    public Ciudad(String municipio, String icono){
        this.municipio = municipio;
        this.icono = icono;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
