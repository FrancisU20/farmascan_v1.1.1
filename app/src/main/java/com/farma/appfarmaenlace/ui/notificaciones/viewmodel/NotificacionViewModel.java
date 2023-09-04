package com.farma.appfarmaenlace.ui.notificaciones.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.farma.appfarmaenlace.data.api.ApiResponseNotificacion;
import com.farma.appfarmaenlace.data.repository.DigitalizacionRepository;
import com.farma.appfarmaenlace.utils.Resource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NotificacionViewModel extends ViewModel {
    private final DigitalizacionRepository repository;
    private final MutableLiveData<Resource<List<ApiResponseNotificacion>>> notificacionesResponse;
    private final CompositeDisposable compositeDisposable;

    public NotificacionViewModel(DigitalizacionRepository repository){
        this.repository=repository;
        this.notificacionesResponse=new MutableLiveData<>();
        this.compositeDisposable = new CompositeDisposable();
    }

    public void getNotificaciones(int oficina,String token){
        notificacionesResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                repository
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

    public LiveData<Resource<List<ApiResponseNotificacion>>> getNotificacionesResponse(){
        return notificacionesResponse;
    }
}
