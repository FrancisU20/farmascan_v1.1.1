package com.farma.appfarmaenlace.ui.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.farma.appfarmaenlace.data.api.ApiDigitalizacionService;
import com.farma.appfarmaenlace.data.api.ApiResponse;
import com.farma.appfarmaenlace.data.api.ApiResponseNotificacion;
import com.farma.appfarmaenlace.data.api.ApiResponseToken;
import com.farma.appfarmaenlace.data.repository.DigitalizacionRepository;
import com.farma.appfarmaenlace.data.repository.MainRepository;
import com.farma.appfarmaenlace.utils.Resource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private final MainRepository repository;
    private final DigitalizacionRepository digitalizacionRepository;
    private final MutableLiveData<Resource<ApiResponse>> digitalizacionResponse;
    private final MutableLiveData<Resource<ApiResponseToken>> tokenResponse;
    private final MutableLiveData<Resource<List<ApiResponseNotificacion>>> notificacionesResponse;
    private final CompositeDisposable compositeDisposable;

    public HomeViewModel(MainRepository repository, DigitalizacionRepository digitalizacionRepository){
        this.repository=repository;
        this.digitalizacionRepository=digitalizacionRepository;
        this.digitalizacionResponse=new MutableLiveData<>();
        this.tokenResponse=new MutableLiveData<>();
        this.notificacionesResponse=new MutableLiveData<>();
        this.compositeDisposable = new CompositeDisposable();
    }

    public void getDigitalizacion(){
        digitalizacionResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                repository
                        .Digitalizacion()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiResponseResponse -> {
                            ApiResponse apiResponse=apiResponseResponse.body();
                            if(apiResponseResponse.isSuccessful()){
                                if(apiResponse.getRespuesta().equalsIgnoreCase("ok")){
                                    digitalizacionResponse.postValue(Resource.success(apiResponse));
                                }else {
                                    digitalizacionResponse.postValue(Resource.error((String)apiResponse.getMensaje(),null));
                                }
                            }else {
                                digitalizacionResponse.postValue(Resource.error((String)apiResponse.getMensaje(),null));
                            }
                        }, throwable -> {
                            digitalizacionResponse.postValue(Resource.error(throwable.getMessage(),null));
                        })
        );
    }

    public void getToken(){
        tokenResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                digitalizacionRepository
                        .GenerarToken("password","6864fc4f-9298-4633-80c0-4d4ea23ae9d6","Dd5/f2$E")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiResponseTokenResponse  -> {
                            ApiResponseToken apiResponse=apiResponseTokenResponse.body();
                            if(apiResponseTokenResponse.isSuccessful()){
                                tokenResponse.postValue(Resource.success(apiResponse));
                            }else {
                                tokenResponse.postValue(Resource.error("Hubo un problemaa al obtener token.",null));
                            }
                        }, throwable -> {
                            tokenResponse.postValue(Resource.error(throwable.getMessage(),null));
                        })
        );
    }

    public void getNotificaciones(int oficina,String token){
        notificacionesResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                digitalizacionRepository
                        .Notificaciones(oficina, token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiResponseNotificaiones  -> {
                            List<ApiResponseNotificacion> apiResponse=apiResponseNotificaiones.body();
                            if(apiResponseNotificaiones.isSuccessful()){
                                notificacionesResponse.postValue(Resource.success(apiResponse));
                            }else {
                                notificacionesResponse.postValue(Resource.error("Hubo un problemaa al obtener notificaciones.",null));
                            }
                        }, throwable -> {
                            notificacionesResponse.postValue(Resource.error(throwable.getMessage(),null));
                        })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    public LiveData<Resource<ApiResponse>> getDigitalizacionResponse(){
        return digitalizacionResponse;
    }

    public LiveData<Resource<ApiResponseToken>> getTokenResponse(){
        return tokenResponse;
    }

    public LiveData<Resource<List<ApiResponseNotificacion>>> getNotificacionesResponse(){
        return notificacionesResponse;
    }

    public void setDigitalizacionService(ApiDigitalizacionService service){
        digitalizacionRepository.setService(service);
    }
}
