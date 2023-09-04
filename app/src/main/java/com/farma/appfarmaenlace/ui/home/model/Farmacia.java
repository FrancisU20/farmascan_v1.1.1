package com.farma.appfarmaenlace.ui.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Farmacia implements Parcelable {
    private String nombre;
    private String cedula;
    private String farmacia;
    private String  idbodega;
    private String sucursal;
    private String  compania;
    private String  centro_costo;
    private String NombreCorto;
    private List<Atribucion> atribuciones;

    public Farmacia(){}
    public Farmacia(String nombre,
                    String cedula,
                    String farmacia,
                    String idbodega,
                    String sucursal,
                    String compania,
                    String centro_costo,
                    String nombreCorto,
                    List<Atribucion> atribuciones) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.farmacia = farmacia;
        this.idbodega = idbodega;
        this.sucursal = sucursal;
        this.compania = compania;
        this.centro_costo = centro_costo;
        this.NombreCorto = nombreCorto;
        this.atribuciones=atribuciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getFarmacia() {
        return farmacia;
    }

    public void setFarmacia(String farmacia) {
        this.farmacia = farmacia;
    }

    public String getIdbodega() {
        return idbodega;
    }

    public void setIdbodega(String idbodega) {
        this.idbodega = idbodega;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getCompania() {
        return compania;
    }

    public void setCompania(String compania) {
        this.compania = compania;
    }

    public String getCentro_costo() {
        return centro_costo;
    }

    public void setCentro_costo(String centro_costo) {
        this.centro_costo = centro_costo;
    }

    public String getNombreCorto() {
        return NombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        NombreCorto = nombreCorto;
    }

    public List<Atribucion> getAtribuciones() {
        return atribuciones;
    }

    public void setAtribuciones(List<Atribucion> atribuciones) {
        this.atribuciones = atribuciones;
    }

    @Override
    public String toString() {
        return "Farmacia{" +
                "nombre='" + nombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", farmacia='" + farmacia + '\'' +
                ", idbodega='" + idbodega + '\'' +
                ", sucursal='" + sucursal + '\'' +
                ", compania='" + compania + '\'' +
                ", centro_costo='" + centro_costo + '\'' +
                ", NombreCorto='" + NombreCorto + '\'' +
                ", atribuciones=" + atribuciones +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nombre);
        dest.writeString(this.cedula);
        dest.writeString(this.farmacia);
        dest.writeString(this.idbodega);
        dest.writeString(this.sucursal);
        dest.writeString(this.compania);
        dest.writeString(this.centro_costo);
        dest.writeString(this.NombreCorto);
        dest.writeTypedList(this.atribuciones);
    }

    public void readFromParcel(Parcel source) {
        this.nombre = source.readString();
        this.cedula = source.readString();
        this.farmacia = source.readString();
        this.idbodega = source.readString();
        this.sucursal = source.readString();
        this.compania = source.readString();
        this.centro_costo = source.readString();
        this.NombreCorto = source.readString();
        this.atribuciones = source.createTypedArrayList(Atribucion.CREATOR);
    }

    protected Farmacia(Parcel in) {
        this.nombre = in.readString();
        this.cedula = in.readString();
        this.farmacia = in.readString();
        this.idbodega = in.readString();
        this.sucursal = in.readString();
        this.compania = in.readString();
        this.centro_costo = in.readString();
        this.NombreCorto = in.readString();
        this.atribuciones = in.createTypedArrayList(Atribucion.CREATOR);
    }

    public static final Parcelable.Creator<Farmacia> CREATOR = new Parcelable.Creator<Farmacia>() {
        @Override
        public Farmacia createFromParcel(Parcel source) {
            return new Farmacia(source);
        }

        @Override
        public Farmacia[] newArray(int size) {
            return new Farmacia[size];
        }
    };
}
