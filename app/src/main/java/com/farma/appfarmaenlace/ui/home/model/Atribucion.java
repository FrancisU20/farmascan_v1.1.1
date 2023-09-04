package com.farma.appfarmaenlace.ui.home.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Atribucion implements Parcelable {
    private String Aplicacion;
    private String Modulo;
    private String Transaccion;

    public Atribucion() {
    }

    public Atribucion(String aplicacion, String modulo, String transaccion) {
        Aplicacion = aplicacion;
        Modulo = modulo;
        Transaccion = transaccion;
    }

    public String getAplicacion() {
        return Aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        Aplicacion = aplicacion;
    }

    public String getModulo() {
        return Modulo;
    }

    public void setModulo(String modulo) {
        Modulo = modulo;
    }

    public String getTransaccion() {
        return Transaccion;
    }

    public void setTransaccion(String transaccion) {
        Transaccion = transaccion;
    }

    @Override
    public String toString() {
        return "Atribucion{" +
                "Aplicacion='" + Aplicacion + '\'' +
                ", Modulo='" + Modulo + '\'' +
                ", Transaccion='" + Transaccion + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Aplicacion);
        dest.writeString(this.Modulo);
        dest.writeString(this.Transaccion);
    }

    public void readFromParcel(Parcel source) {
        this.Aplicacion = source.readString();
        this.Modulo = source.readString();
        this.Transaccion = source.readString();
    }

    protected Atribucion(Parcel in) {
        this.Aplicacion = in.readString();
        this.Modulo = in.readString();
        this.Transaccion = in.readString();
    }

    public static final Parcelable.Creator<Atribucion> CREATOR = new Parcelable.Creator<Atribucion>() {
        @Override
        public Atribucion createFromParcel(Parcel source) {
            return new Atribucion(source);
        }

        @Override
        public Atribucion[] newArray(int size) {
            return new Atribucion[size];
        }
    };
}
