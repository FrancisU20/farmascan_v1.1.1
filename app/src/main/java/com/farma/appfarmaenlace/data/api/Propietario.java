package com.farma.appfarmaenlace.data.api;

public class Propietario {
    private String tipoPropietario;
    private String  ciPasRuc;
    private String  nombresRazonSocial;
    private String  direccion;
    private String  telefono;
    private String  telefono2;

    public Propietario() {
    }

    public Propietario(String tipoPropietario, String ciPasRuc, String nombresRazonSocial, String direccion, String telefono, String telefono2) {
        this.tipoPropietario = tipoPropietario;
        this.ciPasRuc = ciPasRuc;
        this.nombresRazonSocial = nombresRazonSocial;
        this.direccion = direccion;
        this.telefono = telefono;
        this.telefono2 = telefono2;
    }

    public String getTipoPropietario() {
        return tipoPropietario;
    }

    public void setTipoPropietario(String tipoPropietario) {
        this.tipoPropietario = tipoPropietario;
    }

    public String getCiPasRuc() {
        return ciPasRuc;
    }

    public void setCiPasRuc(String ciPasRuc) {
        this.ciPasRuc = ciPasRuc;
    }

    public String getNombresRazonSocial() {
        return nombresRazonSocial;
    }

    public void setNombresRazonSocial(String nombresRazonSocial) {
        this.nombresRazonSocial = nombresRazonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    @Override
    public String toString() {
        return "Propietario{" +
                "tipoPropietario='" + tipoPropietario + '\'' +
                ", ciPasRuc='" + ciPasRuc + '\'' +
                ", nombresRazonSocial='" + nombresRazonSocial + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", telefono2='" + telefono2 + '\'' +
                '}';
    }
}
