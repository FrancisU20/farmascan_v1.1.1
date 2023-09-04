package com.farma.appfarmaenlace.data.api;

public class ApiResponse {
   private String respuesta;
   private Object mensaje;

    public ApiResponse(String respuesta, String mensaje) {
        this.respuesta = respuesta;
        this.mensaje = mensaje;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Object getMensaje() {
        return mensaje;
    }

    public void setMensaje(Object mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "respuesta='" + respuesta + '\'' +
                ", mensaje=" + mensaje +
                '}';
    }
}
