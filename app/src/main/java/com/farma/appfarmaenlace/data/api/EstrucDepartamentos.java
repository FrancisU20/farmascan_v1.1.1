package com.farma.appfarmaenlace.data.api;

public class EstrucDepartamentos {
    public int com_codigo;
    public int amb_codigo;
    public String amb_ambiente;
    public String amb_centro_costo;

    public EstrucDepartamentos() {
    }

    public EstrucDepartamentos(int com_codigo, int amb_codigo, String amb_ambiente, String amb_centro_costo) {
        this.com_codigo = com_codigo;
        this.amb_codigo = amb_codigo;
        this.amb_ambiente = amb_ambiente;
        this.amb_centro_costo = amb_centro_costo;
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

    public String getAmb_ambiente() {
        return amb_ambiente;
    }

    public void setAmb_ambiente(String amb_ambiente) {
        this.amb_ambiente = amb_ambiente;
    }

    public String getAmb_centro_costo() {
        return amb_centro_costo;
    }

    public void setAmb_centro_costo(String amb_centro_costo) {
        this.amb_centro_costo = amb_centro_costo;
    }

    @Override
    public String toString() {
        return amb_ambiente;
    }
}
