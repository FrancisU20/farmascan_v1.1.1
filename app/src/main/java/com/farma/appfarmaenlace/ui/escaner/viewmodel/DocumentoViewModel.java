package com.farma.appfarmaenlace.ui.escaner.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.farma.appfarmaenlace.data.api.ApiResponse;
import com.farma.appfarmaenlace.data.api.ApiResponseCatalogo;
import com.farma.appfarmaenlace.data.api.ApiResponseEnvioInformacion;
import com.farma.appfarmaenlace.data.repository.DigitalizacionRepository;
import com.farma.appfarmaenlace.data.repository.MainRepository;
import com.farma.appfarmaenlace.utils.Resource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DocumentoViewModel extends ViewModel {
    private final MainRepository repository;
    private final DigitalizacionRepository digitalizacionRepository;
    private final CompositeDisposable compositeDisposable;
    private final MutableLiveData<Resource<ApiResponse>> tipoDocumentoResponse;
    private final MutableLiveData<Resource<List<ApiResponseCatalogo>>> catalogoResponse;
    private final MutableLiveData<Resource<ApiResponseEnvioInformacion>> estructuraDocResponse;
    private final MutableLiveData<Resource<ApiResponse>> verificarFacResponse;

    public DocumentoViewModel(MainRepository repository, DigitalizacionRepository digitalizacionRepository) {
        this.repository = repository;
        this.digitalizacionRepository = digitalizacionRepository;
        this.compositeDisposable = new CompositeDisposable();
        this.tipoDocumentoResponse = new MutableLiveData<>();
        this.catalogoResponse = new MutableLiveData<>();
        this.estructuraDocResponse = new MutableLiveData<>();
        this.verificarFacResponse = new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }

    public void getTipoDocumento(String serie) {
        tipoDocumentoResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                repository
                        .TipoDocumento(serie)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiResponseResponse -> {
                            ApiResponse apiResponse = apiResponseResponse.body();
                            if (apiResponseResponse.isSuccessful()) {
                                if (apiResponse.getRespuesta().equalsIgnoreCase("ok")) {
                                    tipoDocumentoResponse.postValue(Resource.success(apiResponse));
                                } else {
                                    tipoDocumentoResponse.postValue(Resource.error((String) apiResponse.getMensaje(), null));
                                }
                            } else {
                                tipoDocumentoResponse.postValue(Resource.error((String) apiResponse.getMensaje(), null));
                            }
                        }, throwable -> {
                            tipoDocumentoResponse.postValue(Resource.error(throwable.getMessage(), null));
                        })
        );
    }

    public void getCatalogo(int id, int id2, String token) {
        catalogoResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                digitalizacionRepository
                        .Catalogo(id, id2, token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(listResponse -> {
                            if (listResponse.isSuccessful()) {
                                catalogoResponse.postValue(Resource.success(listResponse.body()));
                            } else {
                                catalogoResponse.postValue(Resource.error("Hubo un problemaa al obtener catálogo.", null));
                            }
                        }, throwable -> {
                            catalogoResponse.postValue(Resource.error(throwable.getMessage(), null));
                        })
        );
    }

    public void getEstructuraDocumento(int codigo, String token) {
        estructuraDocResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                digitalizacionRepository
                        .EstructuraDocumento(codigo, token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiResponseEnvioInformacionResponse -> {
                            if (apiResponseEnvioInformacionResponse.isSuccessful()) {
                                estructuraDocResponse.postValue(Resource.success(apiResponseEnvioInformacionResponse.body()));
                            } else {
                                estructuraDocResponse.postValue(Resource.error("Hubo un problemaa al obtener estructura.", null));
                            }
                        }, throwable -> {
                            estructuraDocResponse.postValue(Resource.error(throwable.getMessage(), null));
                        })
        );
    }

    public void verificarFactura(String serie) {
        verificarFacResponse.postValue(Resource.loading(null));
        compositeDisposable.add(
                repository
                        .VerificarFactura(serie)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiResponseResponse -> {
                            ApiResponse apiResponse = apiResponseResponse.body();
                            if (apiResponseResponse.isSuccessful()) {
                                if (apiResponse.getRespuesta().equalsIgnoreCase("ok")) {
                                    verificarFacResponse.postValue(Resource.success(apiResponse));
                                } else {
                                    verificarFacResponse.postValue(Resource.error((String) apiResponse.getMensaje(), null));
                                }
                            } else {
                                verificarFacResponse.postValue(Resource.error((String) apiResponse.getMensaje(), null));
                            }
                        }, throwable -> {
                            verificarFacResponse.postValue(Resource.error(throwable.getMessage(), null));
                        })
        );
    }

    public LiveData<Resource<ApiResponse>> getTipoDocumentoResponse() {
        return tipoDocumentoResponse;
    }

    public LiveData<Resource<List<ApiResponseCatalogo>>> getCatalogoResponse() {
        return catalogoResponse;
    }

    public LiveData<Resource<ApiResponseEnvioInformacion>> getEstructuraDocResponse() {
        return estructuraDocResponse;
    }

    public LiveData<Resource<ApiResponse>> getVerificarFacResponse() {
        return verificarFacResponse;
    }
}
