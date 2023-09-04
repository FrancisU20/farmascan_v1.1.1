package com.farma.appfarmaenlace.data.api;

import java.util.Date;

public class ApiResponseNotificacion {
    public Integer not_codigo;
    public Integer not_codigo_documental;
    public String not_documento;
    public String not_estado;
    public String not_fecha_fin;
    public String not_fecha_inicio;
    public String not_fecha_registro;
    public String not_mensaje;
    public String not_procesar_documento;
    public String not_titulo;
    public String not_usuario_registro;

    public ApiResponseNotificacion() {
    }

    @Override
    public String toString() {
        return "ApiResponseNotificacion{" +
                "not_codigo=" + not_codigo +
                ", not_codigo_documental=" + not_codigo_documental +
                ", not_documento='" + not_documento + '\'' +
                ", not_estado='" + not_estado + '\'' +
                ", not_fecha_fin=" + not_fecha_fin +
                ", not_fecha_inicio=" + not_fecha_inicio +
                ", not_fecha_registro=" + not_fecha_registro +
                ", not_mensaje='" + not_mensaje + '\'' +
                ", not_procesar_documento='" + not_procesar_documento + '\'' +
                ", not_titulo='" + not_titulo + '\'' +
                ", not_usuario_registro='" + not_usuario_registro + '\'' +
                '}';
    }
}
