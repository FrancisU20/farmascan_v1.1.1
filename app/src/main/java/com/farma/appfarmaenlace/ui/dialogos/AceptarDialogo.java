package com.farma.appfarmaenlace.ui.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class AceptarDialogo extends DialogFragment {
    private String mensaje;
    private DialogInterface.OnClickListener aceptar;

    public AceptarDialogo(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mensaje)
                .setPositiveButton("ACEPTAR", aceptar);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public DialogInterface.OnClickListener getAceptar() {
        return aceptar;
    }

    public void setAceptar(DialogInterface.OnClickListener aceptar) {
        this.aceptar = aceptar;
    }
}
