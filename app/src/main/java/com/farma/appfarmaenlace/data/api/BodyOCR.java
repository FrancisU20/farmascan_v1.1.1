package com.farma.appfarmaenlace.data.api;

import android.graphics.Bitmap;

public class BodyOCR {
    private String Base64;
    private int CodigoDocumento;
    private transient Bitmap bitmap;

    public BodyOCR() {
    }

    public BodyOCR(String base64, int codigoDocumento, Bitmap bitmap) {
        Base64 = base64;
        CodigoDocumento = codigoDocumento;
        this.bitmap = bitmap;
    }

    public String getBase64() {
        return Base64;
    }

    public void setBase64(String base64) {
        Base64 = base64;
    }

    public int getCodigoDocumento() {
        return CodigoDocumento;
    }

    public void setCodigoDocumento(int codigoDocumento) {
        CodigoDocumento = codigoDocumento;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "BodyOCR{" +
                "Base64='" + Base64 + '\'' +
                ", CodigoDocumento=" + CodigoDocumento +
                ", bitmap=" + bitmap +
                '}';
    }
}
