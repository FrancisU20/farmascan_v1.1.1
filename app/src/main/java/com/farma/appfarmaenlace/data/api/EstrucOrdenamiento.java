package com.farma.appfarmaenlace.data.api;

public class EstrucOrdenamiento {
    private int fml_orden;
    private String fml_mensaje;
    private String fml_validar_modelo_prediccion;
    private String fml_documento_predecir;
    private String fml_validar_reglas_ocr;
    private String fml_reglas_ocr;
    private int fml_numero_valido_reglas_ocr;
    private int fml_documento_codigo;

    public EstrucOrdenamiento() {
    }

    public EstrucOrdenamiento(int fml_orden, String fml_mensaje, String fml_validar_modelo_prediccion, String fml_documento_predecir, String fml_validar_reglas_ocr, String fml_reglas_ocr, int fml_numero_valido_reglas_ocr, int fml_documento_codigo) {
        this.fml_orden = fml_orden;
        this.fml_mensaje = fml_mensaje;
        this.fml_validar_modelo_prediccion = fml_validar_modelo_prediccion;
        this.fml_documento_predecir = fml_documento_predecir;
        this.fml_validar_reglas_ocr = fml_validar_reglas_ocr;
        this.fml_reglas_ocr = fml_reglas_ocr;
        this.fml_numero_valido_reglas_ocr = fml_numero_valido_reglas_ocr;
        this.fml_documento_codigo = fml_documento_codigo;
    }

    public int getFml_orden() {
        return fml_orden;
    }

    public void setFml_orden(int fml_orden) {
        this.fml_orden = fml_orden;
    }

    public String getFml_mensaje() {
        return fml_mensaje;
    }

    public void setFml_mensaje(String fml_mensaje) {
        this.fml_mensaje = fml_mensaje;
    }

    public String getFml_validar_modelo_prediccion() {
        return fml_validar_modelo_prediccion;
    }

    public void setFml_validar_modelo_prediccion(String fml_validar_modelo_prediccion) {
        this.fml_validar_modelo_prediccion = fml_validar_modelo_prediccion;
    }

    public String getFml_documento_predecir() {
        return fml_documento_predecir;
    }

    public void setFml_documento_predecir(String fml_documento_predecir) {
        this.fml_documento_predecir = fml_documento_predecir;
    }

    public String getFml_validar_reglas_ocr() {
        return fml_validar_reglas_ocr;
    }

    public void setFml_validar_reglas_ocr(String fml_validar_reglas_ocr) {
        this.fml_validar_reglas_ocr = fml_validar_reglas_ocr;
    }

    public String getFml_reglas_ocr() {
        return fml_reglas_ocr;
    }

    public void setFml_reglas_ocr(String fml_reglas_ocr) {
        this.fml_reglas_ocr = fml_reglas_ocr;
    }

    public int getFml_numero_valido_reglas_ocr() {
        return fml_numero_valido_reglas_ocr;
    }

    public void setFml_numero_valido_reglas_ocr(int fml_numero_valido_reglas_ocr) {
        this.fml_numero_valido_reglas_ocr = fml_numero_valido_reglas_ocr;
    }

    public int getFml_documento_codigo() {
        return fml_documento_codigo;
    }

    public void setFml_documento_codigo(int fml_documento_codigo) {
        this.fml_documento_codigo = fml_documento_codigo;
    }

    @Override
    public String toString() {
        return "EstrucOrdenamiento{" +
                "fml_orden=" + fml_orden +
                ", fml_mensaje='" + fml_mensaje + '\'' +
                ", fml_validar_modelo_prediccion='" + fml_validar_modelo_prediccion + '\'' +
                ", fml_documento_predecir='" + fml_documento_predecir + '\'' +
                ", fml_validar_reglas_ocr='" + fml_validar_reglas_ocr + '\'' +
                ", fml_reglas_ocr='" + fml_reglas_ocr + '\'' +
                ", fml_numero_valido_reglas_ocr=" + fml_numero_valido_reglas_ocr +
                ", fml_documento_codigo=" + fml_documento_codigo +
                '}';
    }
}
