package com.farma.appfarmaenlace.data.api;

public class Imagen {
	private String nombre;
    private String tipo;
    private String base64;

    public Imagen() {
    }

    public Imagen(String nombre, String tipo, String base64) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.base64 = base64;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    @Override
    public String toString() {
        return "Imagen{" +
                "nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", base64='" + base64 + '\'' +
                '}';
    }
}
