package com.farma.appfarmaenlace.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.farma.appfarmaenlace.data.api.ApiDigitalizacionService;
import com.farma.appfarmaenlace.data.api.GeneralApiService;
import com.farma.appfarmaenlace.data.repository.DigitalizacionRepository;
import com.farma.appfarmaenlace.data.repository.MainRepository;
import com.farma.appfarmaenlace.ui.depdocumentos.viewmodel.DepDocumentoViewModel;
import com.farma.appfarmaenlace.ui.escaner.viewmodel.DocumentoViewModel;
import com.farma.appfarmaenlace.ui.home.viewmodel.HomeViewModel;
import com.farma.appfarmaenlace.ui.login.viewmodel.LoginViewModel;
import com.farma.appfarmaenlace.ui.notificaciones.viewmodel.NotificacionViewModel;

import java.lang.reflect.InvocationTargetException;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private GeneralApiService generalApiService;
    private ApiDigitalizacionService digitalizacionService;

    public ViewModelFactory(GeneralApiService generalApiService) {
        this.generalApiService = generalApiService;
    }

    public ViewModelFactory(ApiDigitalizacionService digitalizacionService){
        this.digitalizacionService=digitalizacionService;
    }

    public ViewModelFactory(GeneralApiService generalApiService,ApiDigitalizacionService digitalizacionService){
        this.generalApiService=generalApiService;
        this.digitalizacionService=digitalizacionService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            try {
                return modelClass.getConstructor(MainRepository.class)
                        .newInstance(new MainRepository(generalApiService));
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (modelClass.isAssignableFrom(DepDocumentoViewModel.class)||
                modelClass.isAssignableFrom(NotificacionViewModel.class)) {
            try {
                return modelClass.getConstructor(DigitalizacionRepository.class)
                        .newInstance(new DigitalizacionRepository(digitalizacionService));
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (modelClass.isAssignableFrom(HomeViewModel.class)||
                modelClass.isAssignableFrom(DocumentoViewModel.class)) {
            try {
                return modelClass.getConstructor(MainRepository.class,DigitalizacionRepository.class)
                        .newInstance(new MainRepository(generalApiService),new DigitalizacionRepository(digitalizacionService));
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
