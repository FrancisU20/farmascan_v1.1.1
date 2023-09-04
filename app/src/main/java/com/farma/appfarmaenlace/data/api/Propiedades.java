package com.farma.appfarmaenlace.data.api;

public class Propiedades {
    private int cat_codigo;
    private int prd_codigo;
    private String prd_nombre;
    private int tpd_tipo_dato;
    private String datos;
    private int prd_propiedad_raiz;


    public Propiedades() {
    }

    public Propiedades(int cat_codigo, int prd_codigo, String prd_nombre, int tpd_tipo_dato, String datos, int prd_propiedad_raiz) {
        this.cat_codigo = cat_codigo;
        this.prd_codigo = prd_codigo;
        this.prd_nombre = prd_nombre;
        this.tpd_tipo_dato = tpd_tipo_dato;
        this.datos = datos;
        this.prd_propiedad_raiz = prd_propiedad_raiz;
    }

    public int getCat_codigo() {
        return cat_codigo;
    }

    public void setCat_codigo(int cat_codigo) {
        this.cat_codigo = cat_codigo;
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

    public int getTpd_tipo_dato() {
        return tpd_tipo_dato;
    }

    public void setTpd_tipo_dato(int tpd_tipo_dato) {
        this.tpd_tipo_dato = tpd_tipo_dato;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public int getPrd_propiedad_raiz() {
        return prd_propiedad_raiz;
    }

    public void setPrd_propiedad_raiz(int prd_propiedad_raiz) {
        this.prd_propiedad_raiz = prd_propiedad_raiz;
    }

    @Override
    public String toString() {
        return "Propiedades{" +
                "cat_codigo=" + cat_codigo +
                ", prd_codigo=" + prd_codigo +
                ", prd_nombre='" + prd_nombre + '\'' +
                ", tpd_tipo_dato=" + tpd_tipo_dato +
                ", datos='" + datos + '\'' +
                ", prd_propiedad_raiz=" + prd_propiedad_raiz +
                '}';
    }
}
