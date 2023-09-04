package com.farma.appfarmaenlace.data.repository;


import com.farma.appfarmaenlace.data.api.ApiDigitalizacionService;
import com.farma.appfarmaenlace.data.api.ApiResponseCatalogo;
import com.farma.appfarmaenlace.data.api.ApiResponseEnvioInformacion;
import com.farma.appfarmaenlace.data.api.ApiResponseNotificacion;
import com.farma.appfarmaenlace.data.api.ApiResponseToken;
import com.farma.appfarmaenlace.data.api.EstrucDepartamentos;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public class DigitalizacionRepository {
    private ApiDigitalizacionService service;

    public DigitalizacionRepository(ApiDigitalizacionService service){this.service=service;}

    public Observable<Response<ApiResponseToken>> GenerarToken(String grantType, String clientId, String clientSecrets){
        return service.GenerarToken(grantType,clientId,clientSecrets);
    }
    public Observable<Response<List<ApiResponseCatalogo>>> Catalogo(int id, int id2, String token){
        return service.Catalogo(id, id2, token);
    }
    public Observable<Response<ApiResponseEnvioInformacion>> EstructuraDocumento(int codigo,String token){
        return service.EstructuraDocumento(codigo,token);
    }
    public Observable<Response<List<EstrucDepartamentos>>> Departamentos(String token){
        return service.Departamentos(token);
    }
    public void setService(ApiDigitalizacionService service) {
        this.service = service;
    }

    public Observable<Response<List<ApiResponseNotificacion>>> Notificaciones(int oficina,String token){
        return service.Notificaciones(oficina,token);
    }
}
