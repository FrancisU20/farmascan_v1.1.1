package com.farma.appfarmaenlace.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesApp {
    public final static String PREFERENCES_APP="APP_DATA";
    public final static String IP_FARMACIA_KEY="ip_farmacia";
    public final static String URL_DIGITALIZACION_KEY="url_digitalizacion";
    public final static String TOKEN_APP_KEY="token";
    public final static String IMAGENES_ESC_KEY="imagenes_esc";
    public final static String ESTRUCTURA_DOC_KEY ="estructura_doc";
    public final static String PROPIEDADES_DOC_KEY="propiedades_doc";
    public final static String PROPIEDADES_CENTROC_KEY="propiedades_centrocostos";
    public final static String PROPIEDADES_USUARIO_KEY="propiedades_usuario";
    public final static String PROPIEDADES_IP_KEY="propiedades_ip";
    public final static String PROPIETARIO_CEDULA_KEY="propiedades_cedula";
    public final static String CODIGO_ESCANER_KEY="codigo_escaner";
    public final static String FARMACIA_KEY="farmacia";
    public final static String TIPO_DOC_KEY="tipo_doc";
    public final static String IP_INGRESADA_USUARIO="ip_usuario";
    public final static String IMAGENES_TABLERO="img_tablero";






    public static SharedPreferences getPreferences(Context context){
        SharedPreferences preferences=context.getSharedPreferences(PREFERENCES_APP,Context.MODE_PRIVATE);
        return preferences;
    }

    public static SharedPreferences.Editor getEditor(Context context){
        return getPreferences(context).edit();
    }
}
