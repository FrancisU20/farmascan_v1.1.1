package com.farma.appfarmaenlace.data.api;

import java.util.List;

public class ApiResponseEnvioInformacion {
    private int empresaCodigo;
    private int departamentoCodigo;
    private String nombre;
    private int codigo;
    //null centro de costos
    private String centroCosto;
    
    private String devolverPropietario;
    private String origen;
    private String usuarioRegistra;
    private String ipRegistra;
    private List<Propiedades> propiedades;
    private Propietario propietario;
    private List<Imagen>imagenes;
    private List<EstrucOrdenamiento> farmascanML;

    public ApiResponseEnvioInformacion() {
    }

    public ApiResponseEnvioInformacion(int empresaCodigo, int departamentoCodigo, String nombre, int codigo, String centroCosto, String devolverPropietario, String origen, String usuarioRegistra, String ipRegistra, List<Propiedades> propiedades, Propietario propietario, List<Imagen> imagenes, List<EstrucOrdenamiento> farmascanML) {
        this.empresaCodigo = empresaCodigo;
        this.departamentoCodigo = departamentoCodigo;
        this.nombre = nombre;
        this.codigo = codigo;
        this.centroCosto = centroCosto;
        this.devolverPropietario = devolverPropietario;
        this.origen = origen;
        this.usuarioRegistra = usuarioRegistra;
        this.ipRegistra = ipRegistra;
        this.propiedades = propiedades;
        this.propietario = propietario;
        this.imagenes = imagenes;
        this.farmascanML = farmascanML;
    }

    public int getEmpresaCodigo() {
        return empresaCodigo;
    }

    public void setEmpresaCodigo(int empresaCodigo) {
        this.empresaCodigo = empresaCodigo;
    }

    public int getDepartamentoCodigo() {
        return departamentoCodigo;
    }

    public void setDepartamentoCodigo(int departamentoCodigo) {
        this.departamentoCodigo = departamentoCodigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(String centroCosto) {
        this.centroCosto = centroCosto;
    }

    public String getDevolverPropietario() {
        return devolverPropietario;
    }

    public void setDevolverPropietario(String devolverPropietario) {
        this.devolverPropietario = devolverPropietario;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getUsuarioRegistra() {
        return usuarioRegistra;
    }

    public void setUsuarioRegistra(String usuarioRegistra) {
        this.usuarioRegistra = usuarioRegistra;
    }

    public String getIpRegistra() {
        return ipRegistra;
    }

    public void setIpRegistra(String ipRegistra) {
        this.ipRegistra = ipRegistra;
    }

    public List<Propiedades> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(List<Propiedades> propiedades) {
        this.propiedades = propiedades;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public List<EstrucOrdenamiento> getFarmascanML() {
        return farmascanML;
    }

    public void setFarmascanML(List<EstrucOrdenamiento> farmascanML) {
        this.farmascanML = farmascanML;
    }

    @Override
    public String toString() {
        return "ApiResponseEnvioInformacion{" +
                "empresaCodigo=" + empresaCodigo +
                ", departamentoCodigo=" + departamentoCodigo +
                ", nombre='" + nombre + '\'' +
                ", codigo=" + codigo +
                ", centroCosto='" + centroCosto + '\'' +
                ", devolverPropietario='" + devolverPropietario + '\'' +
                ", origen='" + origen + '\'' +
                ", usuarioRegistra='" + usuarioRegistra + '\'' +
                ", ipRegistra='" + ipRegistra + '\'' +
                ", propiedades=" + propiedades +
                ", propietario=" + propietario +
                ", imagenes=" + imagenes +
                ", farmascanML=" + farmascanML +
                '}';
    }
}
