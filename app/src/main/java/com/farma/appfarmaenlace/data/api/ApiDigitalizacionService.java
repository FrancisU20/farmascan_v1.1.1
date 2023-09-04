package com.farma.appfarmaenlace.data.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiDigitalizacionService {

    @FormUrlEncoded
    @POST("oauth/token")
    Observable<Response<ApiResponseToken>> GenerarToken(@Field("grant_type") String grantType, @Field("client_id") String clientId, @Field("client_secret") String clientSecret);

    //Trae la estructura del json a llenar en digitalizacion
    @GET("api/Documento/{codigo}")
    Observable<Response<ApiResponseEnvioInformacion>> EstructuraDocumento(@Path("codigo") int codigo, @Header("Authorization") String token);

    //Trae la consulta sql para poder pasar al metodo verificarfactura.
    //@Headers("Content-Type: application/json")
    @GET("api/Catalogo/")
    Observable<Response<List<ApiResponseCatalogo>>> Catalogo(@Query("id") int id, @Query("id2") int id2, @Header("Authorization") String token);

    //Servicio para enviar la información llenada al sistema de digitalización.
    @POST("api/Documento/{codigo}")
    Observable<Response<Object>> SubirInformacion(@Path("codigo") int codigo, @Body ApiResponseEnvioInformacion body, @Header("Authorization") String token);

    //Metodo para cargar departamentos
    @GET("api/Departamentos/4")
    Observable<Response<List<EstrucDepartamentos>>> Departamentos(@Header("Authorization") String token);

    //Metodo para generar notificaciones
    @GET("api/Notificacion/{oficina}")
    Observable<Response<List<ApiResponseNotificacion>>> Notificaciones(@Path("oficina") int oficina, @Header("Authorization") String token);


}

