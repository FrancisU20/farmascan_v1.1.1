package com.farma.appfarmaenlace.ui.depdocumentos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.farma.appfarmaenlace.data.api.ApiResponseCatalogo;
import com.farma.appfarmaenlace.data.api.ApiResponseEnvioInformacion;
import com.farma.appfarmaenlace.data.api.EstrucDepartamentos;
import com.farma.appfarmaenlace.data.repository.DigitalizacionRepository;
import com.farma.appfarmaenlace.utils.Resource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DepDocumentoViewModel extends ViewModel {
    private final DigitalizacionRepository repository;
    private final MutableLiveData<Resource<List<EstrucDepartamentos>>> departamentosResponse;
    private final MutableLiveData<Resource<List<ApiResponseCatalogo>>> catalogoResponse;
    private final MutableLiveData<Resource<ApiResponseEnvioInformacion>> estructuraDocResponse;
    private final CompositeDisposable compositeDisposable;

    public DepDocumentoViewModel(DigitalizacionRepository repository) {
        this.repository = repository;
        this.departamentosResponse = new MutableLiveData<>();
        this.catalogoResponse=new MutableLiveData<>();
        this.estructuraDocResponse=new MutableLiveData<>();
        this.compositeDisposable = new CompositeDisposable();
    }

    public void getDepartamentos(String token) {
        departamentosResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                repository.Departamentos(token).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                listResponse -> {
                                    List<EstrucDepartamentos> listdep=listResponse.body();
                                    if(listResponse.isSuccessful()){
                                        departamentosResponse.postValue(Resource.success(listdep));
                                    }else {
                                        departamentosResponse.postValue(Resource.error("Hubo un problema al obtener departamentos.",null));
                                    }
                                },
                                throwable -> {
                                    departamentosResponse.postValue(Resource.error(throwable.getMessage(),null));
                                }
                        )
        );
    }

    public void getCatalogo(int id, int id2, String token) {
        catalogoResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                repository
                        .Catalogo(id, id2, token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listResponse -> {
                            if (listResponse.isSuccessful()) {
                                catalogoResponse.postValue(Resource.success(listResponse.body()));
                            } else {
                                catalogoResponse.postValue(Resource.error("Hubo un problema al obtener catálogo.", null));
                            }
                        }, throwable -> {
                            catalogoResponse.postValue(Resource.error(throwable.getMessage(), null));
                        })
        );
    }

    public void getEstructuraDocumento(int codigo, String token) {
        estructuraDocResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                repository
                        .EstructuraDocumento(codigo, token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiResponseEnvioInformacionResponse -> {
                            if (apiResponseEnvioInformacionResponse.isSuccessful()) {
                                estructuraDocResponse.postValue(Resource.success(apiResponseEnvioInformacionResponse.body()));
                            } else {
                                estructuraDocResponse.postValue(Resource.error("Hubo un problema al obtener estructura.", null));
                            }
                        }, throwable -> {
                            estructuraDocResponse.postValue(Resource.error(throwable.getMessage(), null));
                        })
        );
    }

    public LiveData<Resource<List<EstrucDepartamentos>>> getDepartamentosResponse(){
        return departamentosResponse;
    }

    public LiveData<Resource<List<ApiResponseCatalogo>>> getCatalogoResponse() {
        return catalogoResponse;
    }

    public LiveData<Resource<ApiResponseEnvioInformacion>> getEstructuraDocResponse() {
        return estructuraDocResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }
}
