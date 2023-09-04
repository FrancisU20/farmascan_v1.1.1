package com.farma.appfarmaenlace.data.api;

public class ApiResponseCatalogo {
    private int cat_codigo;
    private String cat_nombre;
    private String cat_descripcion;
    private String cat_nombre_corto;
    private String cat_obligatorio;
    private String cat_movil;
    private int com_codigo;
    private int amb_codigo;
    private String cat_ocr;


    public ApiResponseCatalogo() {
    }

    public ApiResponseCatalogo(int cat_codigo, String cat_nombre, String cat_descripcion, String cat_nombre_corto, String cat_obligatorio, String cat_movil, int com_codigo, int amb_codigo, String cat_ocr) {
        this.cat_codigo = cat_codigo;
        this.cat_nombre = cat_nombre;
        this.cat_descripcion = cat_descripcion;
        this.cat_nombre_corto = cat_nombre_corto;
        this.cat_obligatorio = cat_obligatorio;
        this.cat_movil = cat_movil;
        this.com_codigo = com_codigo;
        this.amb_codigo = amb_codigo;
        this.cat_ocr = cat_ocr;
    }

    public int getCat_codigo() {
        return cat_codigo;
    }

    public void setCat_codigo(int cat_codigo) {
        this.cat_codigo = cat_codigo;
    }

    public String getCat_nombre() {
        return cat_nombre;
    }

    public void setCat_nombre(String cat_nombre) {
        this.cat_nombre = cat_nombre;
    }

    public String getCat_descripcion() {
        return cat_descripcion;
    }

    public void setCat_descripcion(String cat_descripcion) {
        this.cat_descripcion = cat_descripcion;
    }

    public String getCat_nombre_corto() {
        return cat_nombre_corto;
    }

    public void setCat_nombre_corto(String cat_nombre_corto) {
        this.cat_nombre_corto = cat_nombre_corto;
    }

    public String getCat_obligatorio() {
        return cat_obligatorio;
    }

    public void setCat_obligatorio(String cat_obligatorio) {
        this.cat_obligatorio = cat_obligatorio;
    }

    public String getCat_movil() {
        return cat_movil;
    }

    public void setCat_movil(String cat_movil) {
        this.cat_movil = cat_movil;
    }

    public int getCom_codigo() {
        return com_codigo;
    }

    public void setCom_codigo(int com_codigo) {
        this.com_codigo = com_codigo;
    }

    public int getAmb_codigo() {
        return amb_codigo;
    }

    public void setAmb_codigo(int amb_codigo) {
        this.amb_codigo = amb_codigo;
    }

    public String getCat_ocr() {
        return cat_ocr;
    }

    public void setCat_ocr(String cat_ocr) {
        this.cat_ocr = cat_ocr;
    }

    @Override
    public String toString() {
        return cat_nombre;
    }
}
