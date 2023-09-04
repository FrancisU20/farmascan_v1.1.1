package com.farma.appfarmaenlace.ui.dibujadopropiedades.model;

import java.util.ArrayList;
import java.util.List;

public class ItemPropiedad {
    private int prd_codigo;
    private String prd_nombre;
    private int prd_propiedad_raiz;
    private int tpd_tipo_dato;
    private List<String> opciones;
    private String dato;

    public  ItemPropiedad(){
        this.opciones=new ArrayList<>();
    }

    public int getPrd_codigo() {
        return prd_codigo;
    }

    public void setPrd_codigo(int prd_codigo) {
        this.prd_codigo = prd_codigo;
    }

    public String getPrd_nombre() {
        return prd_nombre;
    }

    public void setPrd_nombre(String prd_nombre) {
        this.prd_nombre = prd_nombre;
    }

    public int getPrd_propiedad_raiz() {
        return prd_propiedad_raiz;
    }

    public void setPrd_propiedad_raiz(int prd_propiedad_raiz) {
        this.prd_propiedad_raiz = prd_propiedad_raiz;
    }

    public int getTpd_tipo_dato() {
        return tpd_tipo_dato;
    }

    public void setTpd_tipo_dato(int tpd_tipo_dato) {
        this.tpd_tipo_dato = tpd_tipo_dato;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    @Override
    public String toString() {
        return "ItemPropiedad{" +
                "prd_codigo=" + prd_codigo +
                ", prd_nombre='" + prd_nombre + '\'' +
                ", prd_propiedad_raiz=" + prd_propiedad_raiz +
                ", tpd_tipo_dato=" + tpd_tipo_dato +
                ", opciones=" + opciones +
                ", dato='" + dato + '\'' +
                '}';
    }
}
