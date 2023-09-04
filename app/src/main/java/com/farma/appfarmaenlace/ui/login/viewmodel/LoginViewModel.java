package com.farma.appfarmaenlace.ui.login.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.farma.appfarmaenlace.data.api.ApiResponse;
import com.farma.appfarmaenlace.data.api.Cookies;
import com.farma.appfarmaenlace.data.api.GeneralApiService;
import com.farma.appfarmaenlace.data.repository.MainRepository;
import com.farma.appfarmaenlace.utils.Resource;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    private final MainRepository repository;
    private final MutableLiveData<Resource<ApiResponse>> loginResponse;
    private final MutableLiveData<Resource<ApiResponse>> saveLoginStateResponse;
    private final MutableLiveData<LoginViewState> loginViewState;
    private final CompositeDisposable compositeDisposable;

    public LoginViewModel(MainRepository repository) {
        this.repository = repository;
        this.loginResponse = new MutableLiveData<>();
        this.saveLoginStateResponse = new MutableLiveData<>();
        this.loginViewState=new MutableLiveData<>(new LoginViewState());
        this.compositeDisposable = new CompositeDisposable();
    }

    public void login(String usuario,String contrasenia, String ipmovil,String ipmovil2){
        loginResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                repository
                        .AutentificarUsuarioFarmascan(usuario, contrasenia, ipmovil, ipmovil2)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiResponseResponse -> {
                            ApiResponse apiResponse=apiResponseResponse.body();
                            if(apiResponseResponse.isSuccessful()){
                                if(apiResponse.getRespuesta().equalsIgnoreCase("ok")){
                                    loginResponse.postValue(Resource.success(apiResponse));
                                }else {
                                    loginResponse.postValue(Resource.error((String)apiResponse.getMensaje(),null));
                                }
                            }else {
                                loginResponse.postValue(Resource.error((String)apiResponse.getMensaje(),null));
                            }
                        }, throwable -> {
                            //loginResponse.postValue(Resource.error(throwable.getMessage(),null));
                            loginResponse.postValue(Resource.error("Ha excedido el tiempo de espera, verifique la IP ingresada, conexión a la red de la farmacia e intente nuevamente",null));
                        })
        );
    }

    public void saveLoginState(Cookies cookies){
       compositeDisposable.add(
               repository.CerrarSesion(cookies)
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
                       .subscribe(apiResponseResponse -> {
                           Log.i("saveLoginState",apiResponseResponse.message());
                           ApiResponse apiResponse=apiResponseResponse.body();
                           if(apiResponseResponse.isSuccessful()){
                               if(apiResponse.getRespuesta().equalsIgnoreCase("ok")){
                                   saveLoginStateResponse.postValue(Resource.success(apiResponse));
                               }else {
                                   saveLoginStateResponse.postValue(Resource.error((String)apiResponse.getMensaje(),null));
                               }
                           }else {
                               saveLoginStateResponse.postValue(Resource.error((String)apiResponse.getMensaje(),null));
                           }
                       },throwable -> {
                           Log.i("saveLoginState",throwable.getMessage());
                           saveLoginStateResponse.postValue(Resource.error(throwable.getMessage(),null));
                       })
       );
    }

    public void validateUsuario(String usuario){
        LoginViewState viewState=loginViewState.getValue();
        if(usuario.isEmpty()){
            viewState.setUsuarioState(Resource.error("Ingrese Usuario",null));
        }else{
            viewState.setUsuarioState(Resource.success(usuario));
        }
        loginViewState.postValue(viewState);
    }

    public void validateIp(String ip){
        LoginViewState viewState=loginViewState.getValue();
        if(ip.isEmpty()){
            viewState.setIpState(Resource.error("Ingrese IP",null));
        }else{
            viewState.setIpState(Resource.success(ip));
        }
        loginViewState.postValue(viewState);
    }

    public void validateClave(String clave){
        LoginViewState viewState=loginViewState.getValue();
        if(clave.isEmpty()){
            viewState.setClaveState(Resource.error("Ingrese Clave",null));
        }else{
            viewState.setClaveState(Resource.success(clave));
        }
        loginViewState.postValue(viewState);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    public LiveData<Resource<ApiResponse>> getLoginResponse(){
        return loginResponse;
    }

    public LiveData<Resource<ApiResponse>> getSaveLoginStateResponse(){
        return saveLoginStateResponse;
    }

    public LiveData<LoginViewState> getLoginViewState(){
        return loginViewState;
    }

    public void setService(GeneralApiService service){
        repository.setService(service);
    }


}
