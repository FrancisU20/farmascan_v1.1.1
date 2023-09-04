package com.farma.appfarmaenlace.data.api;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeneralApiService {
    @GET("ws_plataformamovil/Service1.svc/AutentificarUsuarioFarmascan")
    Observable<Response<ApiResponse>> AutentificarUsuarioFarmascan(@Query("usuario") String usuario,
                                                                   @Query("contrasenia") String contrasenia,
                                                                   @Query("ipmovil") String ipmovil,
                                                                   @Query("ipmovil2") String ipmovil2);

    @GET("ws_plataformamovil/Service1.svc/Digitalizacion")
    Observable<Response<ApiResponse>> Digitalizacion();

    @GET("ws_plataformamovil/Service1.svc/TipoDocumento")
    Observable<Response<ApiResponse>> TipoDocumento(@Query("serie") String serie);


    @GET("ws_plataformamovil/Service1.svc/VerificarFactura")
    Observable<Response<ApiResponse>> VerificarFactura(@Query(value = "serie",encoded = true) String serie);


    @Headers("Content-Type: application/json")
    @POST("ws_plataformamovil/Service1.svc/Cookie")
    Observable<Response<ApiResponse>> CerrarSesion(@Body Cookies cookies);



}
