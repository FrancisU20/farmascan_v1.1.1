package com.farma.appfarmaenlace.ui.escaner.model;

public class ItemPropiedad {
    private String etiqueta;
    private String valor;

    public ItemPropiedad(String etiqueta, String valor) {
        this.etiqueta = etiqueta;
        this.valor = valor;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
