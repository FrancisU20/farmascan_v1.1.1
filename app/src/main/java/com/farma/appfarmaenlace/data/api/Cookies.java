package com.farma.appfarmaenlace.data.api;

public class Cookies {
    private String cookie;
    private String nombreUsu;
    private String estado;
    private String ipDispositivo;

    public Cookies() {
    }

    public Cookies(String cookie, String nombreUsu, String estado, String ipDispositivo) {
        this.cookie = cookie;
        this.nombreUsu = nombreUsu;
        this.estado = estado;
        this.ipDispositivo = ipDispositivo;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIpDispositivo() {
        return ipDispositivo;
    }

    public void setIpDispositivo(String ipDispositivo) {
        this.ipDispositivo = ipDispositivo;
    }

    @Override
    public String toString() {
        return "Cookies{" +
                "cookie='" + cookie + '\'' +
                ", nombreUsu='" + nombreUsu + '\'' +
                ", estado='" + estado + '\'' +
                ", ipDispositivo='" + ipDispositivo + '\'' +
                '}';
    }
}
