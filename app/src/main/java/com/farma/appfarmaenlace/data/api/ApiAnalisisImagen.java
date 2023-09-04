package com.farma.appfarmaenlace.data.api;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiAnalisisImagen {

    @POST("predecir")
    Observable<Response<ApiResponse>> PredecirDocumento(@Body BodyOCR Base64);
}
