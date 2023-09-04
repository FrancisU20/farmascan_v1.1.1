package com.farma.appfarmaenlace.data.repository;

import com.farma.appfarmaenlace.data.api.ApiResponse;
import com.farma.appfarmaenlace.data.api.Cookies;
import com.farma.appfarmaenlace.data.api.GeneralApiService;

import io.reactivex.Observable;
import retrofit2.Response;

public class MainRepository {
    private GeneralApiService service;

    public MainRepository(GeneralApiService service) {
        this.service = service;
    }

    public Observable<Response<ApiResponse>> AutentificarUsuarioFarmascan(String usuario, String contrasenia, String ipmovil, String ipmovil2){
        return  service.AutentificarUsuarioFarmascan(usuario,contrasenia,ipmovil,ipmovil2);
    }
    public Observable<Response<ApiResponse>> Digitalizacion(){
        return service.Digitalizacion();
    }
    public Observable<Response<ApiResponse>> TipoDocumento(String serie){return  service.TipoDocumento(serie);}
    public Observable<Response<ApiResponse>> VerificarFactura(String serie){
        return service.VerificarFactura(serie);
    }
    public Observable<Response<ApiResponse>> CerrarSesion(Cookies cookies){
        return service.CerrarSesion(cookies);
    }
    public void setService(GeneralApiService service) {
        this.service = service;
    }

}
