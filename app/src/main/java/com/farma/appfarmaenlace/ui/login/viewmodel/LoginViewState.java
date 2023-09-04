package com.farma.appfarmaenlace.ui.login.viewmodel;

import com.farma.appfarmaenlace.utils.Resource;
import com.farma.appfarmaenlace.utils.Status;

public class LoginViewState {
    private Resource<String> ipState;
    private Resource<String> usuarioState;
    private Resource<String> claveState;

    public LoginViewState() {
        this.ipState = Resource.error("Ingrese IP",null);
        this.usuarioState =  Resource.error("Ingrese Usuario",null);
        this.claveState =  Resource.error("Ingrese Contraseña",null);
    }

    public boolean isValidForm(){
        if(ipState.getStatus()== Status.ERROR){
            return  false;
        }
        if(usuarioState.getStatus()== Status.ERROR){
            return  false;
        }
        return claveState.getStatus() != Status.ERROR;
    }

    public Resource<String> getIpState() {
        return ipState;
    }

    public void setIpState(Resource<String> ipState) {
        this.ipState = ipState;
    }

    public Resource<String> getUsuarioState() {
        return usuarioState;
    }

    public void setUsuarioState(Resource<String> usuarioState) {
        this.usuarioState = usuarioState;
    }

    public Resource<String> getClaveState() {
        return claveState;
    }

    public void setClaveState(Resource<String> claveState) {
        this.claveState = claveState;
    }
}
